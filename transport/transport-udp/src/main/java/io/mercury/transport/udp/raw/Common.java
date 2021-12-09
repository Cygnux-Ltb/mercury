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
package io.mercury.transport.udp.raw;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.StandardSocketOptions;
import java.nio.channels.DatagramChannel;
import java.nio.channels.Selector;

import org.agrona.nio.NioSelectedKeySet;

/**
 * Common configuration and functions used across raw samples.
 */
public class Common {
	public static final int NUM_MESSAGES = 10_000;

	public static final int PONG_PORT = 20123;
	public static final int PING_PORT = 20124;

	static final Field SELECTED_KEYS_FIELD;
	static final Field PUBLIC_SELECTED_KEYS_FIELD;

	static {
		Field selectKeysField = null;
		Field publicSelectKeysField = null;

		try {
			final Class<?> clazz = Class.forName("sun.nio.ch.SelectorImpl", false, ClassLoader.getSystemClassLoader());

			if (clazz.isAssignableFrom(Selector.open().getClass())) {
				selectKeysField = clazz.getDeclaredField("selectedKeys");
				selectKeysField.setAccessible(true);

				publicSelectKeysField = clazz.getDeclaredField("publicSelectedKeys");
				publicSelectKeysField.setAccessible(true);
			}
		} catch (final Exception ignore) {
		}

		SELECTED_KEYS_FIELD = selectKeysField;
		PUBLIC_SELECTED_KEYS_FIELD = publicSelectKeysField;
	}

	public static void init(final DatagramChannel channel) throws IOException {
		channel.configureBlocking(false);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
	}

	public static void init(final DatagramChannel channel, final InetSocketAddress sendAddress) throws IOException {
		channel.configureBlocking(false);
		channel.setOption(StandardSocketOptions.SO_REUSEADDR, true);
		channel.connect(sendAddress);
	}

	public static NioSelectedKeySet keySet(final Selector selector) {
		NioSelectedKeySet tmpSet = null;

		if (null != PUBLIC_SELECTED_KEYS_FIELD) {
			try {
				tmpSet = new NioSelectedKeySet();

				SELECTED_KEYS_FIELD.set(selector, tmpSet);
				PUBLIC_SELECTED_KEYS_FIELD.set(selector, tmpSet);
			} catch (final Exception ignore) {
				tmpSet = null;
			}
		}

		return tmpSet;
	}
}
