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

#include <stdio.h>
#include <signal.h>
#include <inttypes.h>
#include <errno.h>

#ifndef _MSC_VER
#include <unistd.h>
#include <getopt.h>
#endif

#include "aeronc.h"
#include "aeron_common.h"
#include "util/aeron_strutil.h"
#include "concurrent/aeron_thread.h"
#include "concurrent/aeron_atomic.h"

volatile bool running = true;

void sigint_handler(int signal)
{
    AERON_PUT_ORDERED(running, false);
}

static const char *aeron_stat_usage()
{
    return
        "    -h               Displays help information.\n"
        "    -p basePath      Base Path to shared memory. Default: /dev/shm/aeron-[user]\n"
        "    -u update period Update period in milliseconds. Default: 1000\n"
        "    -t timeout       Number of milliseconds to wait to see if the driver metadata is available. Default: 1000\n";
}

static void aeron_stat_print_error_and_usage(const char *message)
{
    fprintf(stderr, "%s\n%s", message, aeron_stat_usage());
}

typedef struct aeron_stat_settings_stct
{
    const char *base_path;
    int64_t update_interval_ms;
    int64_t timeout_ms;
}
aeron_stat_settings_t;

static void aeron_stat_print_counter(
    int64_t value,
    int32_t id,
    int32_t type_id,
    const uint8_t *key,
    size_t key_length,
    const char *label,
    size_t label_length,
    void *clientd)
{
    char value_str[AERON_FORMAT_NUMBER_TO_LOCALE_STR_LEN];
    printf(
        "%3" PRId32 " : %20s - %.*s\n",
        id,
        aeron_format_number_to_locale(value, value_str, sizeof(value_str)),
        (int)label_length, label);
}

int main(int argc, char **argv)
{
    char default_directory[AERON_MAX_PATH];
    aeron_default_path(default_directory, AERON_MAX_PATH);
    aeron_stat_settings_t settings =
        {
            .base_path = default_directory,
            .update_interval_ms = 1000,
            .timeout_ms = 1000
        };


    int opt;

    while ((opt = getopt(argc, argv, "p:u:t:h")) != -1)
    {
        switch (opt)
        {
            case 'd':
                settings.base_path = optarg;
                break;

            case 't':
            {
                errno = 0;
                char *endptr;
                settings.timeout_ms = strtoll(optarg, &endptr, 10);
                if (0 != errno || '\0' != endptr[0])
                {
                    aeron_stat_print_error_and_usage("Invalid timeout");
                    return EXIT_FAILURE;
                }
                break;
            }

            case 'u':
            {
                errno = 0;
                char *endptr;
                settings.update_interval_ms = strtoll(optarg, &endptr, 10);
                if (0 != errno || '\0' != endptr[0])
                {
                    aeron_stat_print_error_and_usage("Invalid timeout");
                    return EXIT_FAILURE;
                }
                break;
            }

            case 'h':
                aeron_stat_print_error_and_usage(argv[0]);
                return EXIT_SUCCESS;

            default:
                aeron_stat_print_error_and_usage("Unknown option");
                return EXIT_FAILURE;
        }
    }

    aeron_cnc_t *aeron_cnc = NULL;
    if (aeron_cnc_init(&aeron_cnc, settings.base_path, settings.timeout_ms) < 0)
    {
        aeron_stat_print_error_and_usage(aeron_errmsg());
        return EXIT_FAILURE;
    }

    signal(SIGINT, sigint_handler);

    aeron_cnc_constants_t cnc_constants;
    aeron_cnc_constants(aeron_cnc, &cnc_constants);
    aeron_counters_reader_t *counters_reader = aeron_cnc_counters_reader(aeron_cnc);

    while (running)
    {
        int64_t now_ms = aeron_epoch_clock();
        char currentTime[AERON_MAX_PATH];
        char client_liveness_str[AERON_FORMAT_NUMBER_TO_LOCALE_STR_LEN];
        aeron_format_date(currentTime, sizeof(currentTime) - 1, now_ms);

        printf("\033[H\033[2J");

        printf(
            "%s - Aeron Stat (CnC v%s), pid %" PRId64 ", client liveness %s ns\n",
            currentTime,
            aeron_version_full(),
            cnc_constants.pid,
            aeron_format_number_to_locale(
                cnc_constants.client_liveness_timeout, client_liveness_str, sizeof(client_liveness_str)));
        printf("===========================\n");

        aeron_counters_reader_foreach_counter(counters_reader, aeron_stat_print_counter, NULL);

        aeron_micro_sleep((unsigned int)(settings.timeout_ms * 1000));
    }

    aeron_cnc_close(aeron_cnc);

    return 0;
}
