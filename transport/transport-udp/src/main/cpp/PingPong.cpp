/*
 * Copyright 2014-2021 Real Logic Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#include <cstdint>
#include <cstdio>
#include <thread>
#include <csignal>

extern "C"
{
#include <hdr_histogram.h>
}

#include "Configuration.h"
#include "concurrent/BusySpinIdleStrategy.h"
#include "util/CommandOptionParser.h"
#include "FragmentAssembler.h"
#include "Aeron.h"

using namespace std::chrono;
using namespace aeron::util;
using namespace aeron;

std::atomic<bool> running(true);

void sigIntHandler(int)
{
    running = false;
}

static const char optHelp           = 'h';
static const char optPrefix         = 'p';
static const char optPingChannel    = 'c';
static const char optPongChannel    = 'C';
static const char optPingStreamId   = 's';
static const char optPongStreamId   = 'S';
static const char optFrags          = 'f';
static const char optMessages       = 'm';
static const char optLength         = 'L';
static const char optWarmupMessages = 'w';

struct Settings
{
    std::string dirPrefix;
    std::string pingChannel = samples::configuration::DEFAULT_PING_CHANNEL;
    std::string pongChannel = samples::configuration::DEFAULT_PONG_CHANNEL;
    std::int32_t pingStreamId = samples::configuration::DEFAULT_PING_STREAM_ID;
    std::int32_t pongStreamId = samples::configuration::DEFAULT_PONG_STREAM_ID;
    long long numberOfWarmupMessages = samples::configuration::DEFAULT_NUMBER_OF_WARM_UP_MESSAGES;
    long long numberOfMessages = samples::configuration::DEFAULT_NUMBER_OF_MESSAGES;
    int messageLength = samples::configuration::DEFAULT_MESSAGE_LENGTH;
    int fragmentCountLimit = samples::configuration::DEFAULT_FRAGMENT_COUNT_LIMIT;
};

Settings parseCmdLine(CommandOptionParser &cp, int argc, char **argv)
{
    cp.parse(argc, argv);
    if (cp.getOption(optHelp).isPresent())
    {
        cp.displayOptionsHelp(std::cout);
        exit(0);
    }

    Settings s;

    s.dirPrefix = cp.getOption(optPrefix).getParam(0, s.dirPrefix);
    s.pingChannel = cp.getOption(optPingChannel).getParam(0, s.pingChannel);
    s.pongChannel = cp.getOption(optPongChannel).getParam(0, s.pongChannel);
    s.pingStreamId = cp.getOption(optPingStreamId).getParamAsInt(0, 1, INT32_MAX, s.pingStreamId);
    s.pongStreamId = cp.getOption(optPongStreamId).getParamAsInt(0, 1, INT32_MAX, s.pongStreamId);
    s.numberOfMessages = cp.getOption(optMessages).getParamAsLong(0, 0, INT64_MAX, s.numberOfMessages);
    s.messageLength = cp.getOption(optLength).getParamAsInt(0, sizeof(std::int64_t), INT32_MAX, s.messageLength);
    s.fragmentCountLimit = cp.getOption(optFrags).getParamAsInt(0, 1, INT32_MAX, s.fragmentCountLimit);
    s.numberOfWarmupMessages = cp.getOption(optWarmupMessages).getParamAsLong(0, 0, INT64_MAX, s.numberOfWarmupMessages);

    return s;
}

void sendPingAndReceivePong(
    const fragment_handler_t &fragmentHandler,
    Publication &publication,
    Subscription &subscription,
    const Settings &settings)
{
    std::unique_ptr<std::uint8_t[]> buffer(new std::uint8_t[settings.messageLength]);
    concurrent::AtomicBuffer srcBuffer(buffer.get(), static_cast<size_t>(settings.messageLength));
    BusySpinIdleStrategy idleStrategy;

    while (!subscription.isConnected())
    {
        std::this_thread::yield();
    }

    std::shared_ptr<Image> imageSharedPtr = subscription.imageByIndex(0);
    Image &image = *imageSharedPtr;

    for (std::int64_t i = 0; i < settings.numberOfMessages; i++)
    {
        std::int64_t position;

        do
        {
            // timestamps in the message are relative to this app, so just send the timestamp directly.
            steady_clock::time_point start = steady_clock::now();
            srcBuffer.putBytes(0, (std::uint8_t *)&start, sizeof(steady_clock::time_point));
        }
        while ((position = publication.offer(srcBuffer, 0, settings.messageLength)) < 0L);

        while (image.position() < position)
        {
            int fragments = image.poll(fragmentHandler, settings.fragmentCountLimit);
            idleStrategy.idle(fragments);
        }
    }
}

std::shared_ptr<Subscription> findSubscription(std::shared_ptr<Aeron> aeron, std::int64_t id)
{
    std::shared_ptr<Subscription> subscription = aeron->findSubscription(id);

    while (!subscription)
    {
        std::this_thread::yield();
        subscription = aeron->findSubscription(id);
    }

    return subscription;
}

std::shared_ptr<Publication> findPublication(std::shared_ptr<Aeron> aeron, std::int64_t id)
{
    std::shared_ptr<Publication> publication = aeron->findPublication(id);

    while (!publication)
    {
        std::this_thread::yield();
        publication = aeron->findPublication(id);
    }

    return publication;
}

int main(int argc, char **argv)
{
    CommandOptionParser cp;
    cp.addOption(CommandOption(optHelp,           0, 0, "                Displays help information."));
    cp.addOption(CommandOption(optPrefix,         1, 1, "dir             Prefix directory for aeron driver."));
    cp.addOption(CommandOption(optPingChannel,    1, 1, "channel         Ping Channel."));
    cp.addOption(CommandOption(optPongChannel,    1, 1, "channel         Pong Channel."));
    cp.addOption(CommandOption(optPingStreamId,   1, 1, "streamId        Ping Stream ID."));
    cp.addOption(CommandOption(optPongStreamId,   1, 1, "streamId        Pong Stream ID."));
    cp.addOption(CommandOption(optMessages,       1, 1, "number          Number of Messages."));
    cp.addOption(CommandOption(optLength,         1, 1, "length          Length of Messages."));
    cp.addOption(CommandOption(optFrags,          1, 1, "limit           Fragment Count Limit."));
    cp.addOption(CommandOption(optWarmupMessages, 1, 1, "number          Number of Messages for warmup."));

    signal(SIGINT, sigIntHandler);

    std::shared_ptr<std::thread> pongThread;

    try
    {
        Settings settings = parseCmdLine(cp, argc, argv);

        std::cout << "Pong at " << settings.pongChannel << " on Stream ID " << settings.pongStreamId << std::endl;
        std::cout << "Ping at " << settings.pingChannel << " on Stream ID " << settings.pingStreamId << std::endl;

        aeron::Context context;
        std::atomic<int> countDown(1);
        std::int64_t pongSubscriptionId, pingPublicationId, pingSubscriptionId, pongPublicationId;

        if (!settings.dirPrefix.empty())
        {
            context.aeronDir(settings.dirPrefix);
        }

        context.newSubscriptionHandler(
            [](const std::string &channel, std::int32_t streamId, std::int64_t correlationId)
            {
                std::cout << "Subscription: " << channel << " " << correlationId << ":" << streamId << std::endl;
            });

        context.newPublicationHandler(
            [](const std::string &channel, std::int32_t streamId, std::int32_t sessionId, std::int64_t correlationId)
            {
                std::cout << "Publication: " << channel << " " << correlationId << ":" << streamId << ":" << sessionId << std::endl;
            });

        context.availableImageHandler(
            [&](Image &image)
            {
                std::cout << "Available image correlationId=" << image.correlationId() << " sessionId=" << image.sessionId();
                std::cout << " at position=" << image.position() << " from " << image.sourceIdentity() << std::endl;

                if (image.subscriptionRegistrationId() == pongSubscriptionId)
                {
                    countDown--;
                }
            });

        context.unavailableImageHandler([](Image &image)
        {
            std::cout << "Unavailable image on correlationId=" << image.correlationId() << " sessionId=" << image.sessionId();
            std::cout << " at position=" << image.position() << " from " << image.sourceIdentity() << std::endl;
        });

        context.preTouchMappedMemory(true);

        std::shared_ptr<Aeron> aeron = Aeron::connect(context);

        pongSubscriptionId = aeron->addSubscription(settings.pongChannel, settings.pongStreamId);
        pingPublicationId = aeron->addPublication(settings.pingChannel, settings.pingStreamId);
        pingSubscriptionId = aeron->addSubscription(settings.pingChannel, settings.pingStreamId);
        pongPublicationId = aeron->addPublication(settings.pongChannel, settings.pongStreamId);

        std::shared_ptr<Subscription> pongSubscription, pingSubscription;
        std::shared_ptr<Publication> pingPublication, pongPublication;

        pongSubscription = findSubscription(aeron, pongSubscriptionId);
        pingSubscription = findSubscription(aeron, pingSubscriptionId);
        pingPublication = findPublication(aeron, pingPublicationId);
        pongPublication = findPublication(aeron, pongPublicationId);

        while (countDown > 0)
        {
            std::this_thread::yield();
        }

        Publication &pongPublicationRef = *pongPublication;
        Subscription &pingSubscriptionRef = *pingSubscription;
        BusySpinIdleStrategy idleStrategy;
        BusySpinIdleStrategy pingHandlerIdleStrategy;
        FragmentAssembler pingFragmentAssembler(
            [&](AtomicBuffer &buffer, index_t offset, index_t length, const Header &header)
            {
                if (pongPublicationRef.offer(buffer, offset, length) > 0L)
                {
                    return;
                }

                while (pongPublicationRef.offer(buffer, offset, length) < 0L)
                {
                    pingHandlerIdleStrategy.idle();
                }
            });

        fragment_handler_t ping_handler = pingFragmentAssembler.handler();

        pongThread = std::make_shared<std::thread>(
            [&]()
            {
                while (!pingSubscriptionRef.isConnected())
                {
                    std::this_thread::yield();
                }

                std::shared_ptr<Image> imageSharedPtr = pingSubscriptionRef.imageByIndex(0);
                Image &image = *imageSharedPtr;

                while (running)
                {
                    idleStrategy.idle(image.poll(ping_handler, settings.fragmentCountLimit));
                }
            });

        aeron::util::OnScopeExit tidy(
            [&]()
            {
                running = false;

                if (nullptr != pongThread && pongThread->joinable())
                {
                    pongThread->join();
                    pongThread = nullptr;
                }
            });

        if (settings.numberOfWarmupMessages > 0)
        {
            Settings warmupSettings = settings;
            warmupSettings.numberOfMessages = warmupSettings.numberOfWarmupMessages;

            const steady_clock::time_point start = steady_clock::now();

            std::cout << "Warming up the media driver with "
                      << toStringWithCommas(warmupSettings.numberOfWarmupMessages) << " messages of length "
                      << toStringWithCommas(warmupSettings.messageLength) << std::endl;

            sendPingAndReceivePong(
                [](AtomicBuffer &, index_t, index_t, Header &){},
                *pingPublication,
                *pongSubscription,
                warmupSettings);

            std::int64_t nanoDuration = duration<std::int64_t, std::nano>(steady_clock::now() - start).count();

            std::cout << "Warmed up the media driver in " << nanoDuration << " [ns]" << std::endl;
        }

        hdr_histogram *histogram;
        hdr_init(1, 10 * 1000 * 1000 * 1000LL, 3, &histogram);

        do
        {
            hdr_reset(histogram);

            FragmentAssembler fragmentAssembler(
                [&](const AtomicBuffer &buffer, index_t offset, index_t length, const Header &header)
                {
                    steady_clock::time_point end = steady_clock::now();
                    steady_clock::time_point start;

                    buffer.getBytes(offset, (std::uint8_t *)&start, sizeof(steady_clock::time_point));
                    std::int64_t nanoRtt = duration<std::int64_t, std::nano>(end - start).count();

                    hdr_record_value(histogram, nanoRtt);
                });

            std::cout << "Pinging "
                      << toStringWithCommas(settings.numberOfMessages) << " messages of length "
                      << toStringWithCommas(settings.messageLength) << " bytes" << std::endl;

            steady_clock::time_point startRun = steady_clock::now();
            sendPingAndReceivePong(fragmentAssembler.handler(), *pingPublication, *pongSubscription, settings);
            steady_clock::time_point endRun = steady_clock::now();

            hdr_percentiles_print(histogram, stdout, 5, 1000.0, CLASSIC);
            fflush(stdout);

            double runDuration = duration<double>(endRun - startRun).count();
            std::cout << "Throughput of "
                      << toStringWithCommas((double)settings.numberOfMessages / runDuration)
                      << " RTTs/sec" << std::endl;
        }
        while (running && continuationBarrier("Execute again?"));
    }
    catch (const CommandOptionException &e)
    {
        std::cerr << "ERROR: " << e.what() << std::endl << std::endl;
        cp.displayOptionsHelp(std::cerr);
        return -1;
    }
    catch (const SourcedException &e)
    {
        std::cerr << "FAILED: " << e.what() << " : " << e.where() << std::endl;
        return -1;
    }
    catch (const std::exception &e)
    {
        std::cerr << "FAILED: " << e.what() << " : " << std::endl;
        return -1;
    }

    return 0;
}
