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
package io.mercury.transport.udp.archive;

import io.aeron.security.Authenticator;
import io.aeron.security.SessionProxy;
import org.agrona.collections.ArrayUtil;
import org.agrona.collections.Long2ObjectHashMap;

import java.nio.charset.StandardCharsets;

/**
 * A sample {@link Authenticator} to demonstrate usage based on some hardcoded
 * credentials.
 */
public class SampleAuthenticator implements Authenticator {
    private static final String CREDENTIALS_STRING_NO_CHALLENGE = "admin:admin";
    private static final String CREDENTIALS_STRING_REQUIRING_CHALLENGE = "admin:adminC";
    private static final String CHALLENGE_CREDENTIALS_STRING = "admin:CSadmin";
    private static final String CHALLENGE_STRING = "challenge!";

    enum SessionState {
        CHALLENGE, AUTHENTICATED, REJECT
    }

    private final Long2ObjectHashMap<SessionState> sessionIdToStateMap = new Long2ObjectHashMap<>();

    /**
     * Client is attempting to connect.
     *
     * @param sessionId          to identify the client session connecting.
     * @param encodedCredentials from the Connect Request. Will not be null, but may
     *                           be 0 length.
     * @param nowMs              current epoch time in milliseconds.
     */
    public void onConnectRequest(final long sessionId, final byte[] encodedCredentials, final long nowMs) {
        final String credentialsString = new String(encodedCredentials, StandardCharsets.US_ASCII);

        if (credentialsString.equals(CREDENTIALS_STRING_NO_CHALLENGE)) {
            sessionIdToStateMap.put(sessionId, SessionState.AUTHENTICATED);
        } else if (credentialsString.equals(CREDENTIALS_STRING_REQUIRING_CHALLENGE)) {
            sessionIdToStateMap.put(sessionId, SessionState.CHALLENGE);
        } else {
            sessionIdToStateMap.put(sessionId, SessionState.REJECT);
        }
    }

    /**
     * Client has returned a response to a challenge.
     *
     * @param sessionId          to identify the client session connecting.
     * @param encodedCredentials from the Challenge Response. Will not be null, but
     *                           may be 0 length.
     * @param nowMs              current epoch time in milliseconds.
     */
    public void onChallengeResponse(final long sessionId, final byte[] encodedCredentials, final long nowMs) {
        final String credentialsString = new String(encodedCredentials, StandardCharsets.US_ASCII);
        final SessionState sessionState = sessionIdToStateMap.get(sessionId);

        if (SessionState.CHALLENGE == sessionState && credentialsString.equals(CHALLENGE_CREDENTIALS_STRING)) {
            sessionIdToStateMap.put(sessionId, SessionState.AUTHENTICATED);
        } else if (!credentialsString.equals(CHALLENGE_CREDENTIALS_STRING)) {
            sessionIdToStateMap.put(sessionId, SessionState.REJECT);
        }
    }

    /**
     * Client session is now connected so a response can be sent.
     *
     * @param sessionProxy to use to inform client of status.
     * @param nowMs        current epoch time in milliseconds.
     */
    public void onConnectedSession(final SessionProxy sessionProxy, final long nowMs) {
        final SessionState sessionState = sessionIdToStateMap.get(sessionProxy.sessionId());

        if (null != sessionState) {
            switch (sessionState) {
                case CHALLENGE:
                    sessionProxy.challenge((CHALLENGE_STRING.getBytes()));
                    break;

                case AUTHENTICATED:
                    sessionProxy.authenticate(ArrayUtil.EMPTY_BYTE_ARRAY);
                    break;

                case REJECT:
                    sessionProxy.reject();
                    break;
            }
        }
    }

    /**
     * The client has been challenged and is awaiting a response.
     *
     * @param sessionProxy to use to inform client of status.
     * @param nowMs        current epoch time in milliseconds.
     */
    public void onChallengedSession(final SessionProxy sessionProxy, final long nowMs) {
        final SessionState sessionState = sessionIdToStateMap.get(sessionProxy.sessionId());

        if (null != sessionState) {
            switch (sessionState) {
                case AUTHENTICATED:
                    sessionProxy.authenticate(ArrayUtil.EMPTY_BYTE_ARRAY);
                    break;
                case REJECT:
                    sessionProxy.reject();
                    break;
                default:
                    System.err.println("error");
                    break;
            }
        }
    }
}
