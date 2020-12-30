package io.mercury.common.codec;

/*
 * #%L
 * ch-commons-util
 * %%
 * Copyright (C) 2012 Cloudhopper by Twitter
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.nio.ByteBuffer;
import java.text.ParseException;

/**
 * Code originally copied from the OpenDS Java project.
 *
 * http://www.opends.org/
 *
 * This class removes the dependency on the Jakarta commons-codec library. Since
 * this utility package is used in every Cloudhopper Java project, providing our
 * own implementation (that's also much faster) allows the removal of a
 * transitive dependency for every Cloudhopper Java project :-)
 *
 * This class provides methods for performing base64 encoding and decoding.
 * Base64 is a mechanism for encoding binary data in ASCII form by converting
 * sets of three bytes with eight significant bits each to sets of four bytes
 * with six significant bits each.
 *
 * @author joelauer (twitter: @jjlauer or
 *         <a href="http://twitter.com/jjlauer" target=
 *         window>http://twitter.com/jjlauer</a>)
 */
public class Base64Codec {

	/**
	 * The set of characters that may be used in base64-encoded values.
	 */
	private static final char[] BASE64_ALPHABET = ("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
			+ "0123456789+/").toCharArray();

	/**
	 * Prevent instance creation.
	 */
	private Base64Codec() {
		// No implementation required.
	}

	/**
	 * Encodes the provided raw data using base64.
	 * 
	 * @param rawData The raw data to encode. It must not be <CODE>null</CODE>.
	 * @return The base64-encoded representation of the provided raw data.
	 */
	public static String encode(byte[] rawData) {
		StringBuilder buffer = new StringBuilder(4 * rawData.length / 3);

		int pos = 0;
		int iterations = rawData.length / 3;
		for (int i = 0; i < iterations; i++) {
			int value = ((rawData[pos++] & 0xFF) << 16) | ((rawData[pos++] & 0xFF) << 8) | (rawData[pos++] & 0xFF);

			buffer.append(BASE64_ALPHABET[(value >>> 18) & 0x3F]);
			buffer.append(BASE64_ALPHABET[(value >>> 12) & 0x3F]);
			buffer.append(BASE64_ALPHABET[(value >>> 6) & 0x3F]);
			buffer.append(BASE64_ALPHABET[value & 0x3F]);
		}

		switch (rawData.length % 3) {
		case 1:
			buffer.append(BASE64_ALPHABET[(rawData[pos] >>> 2) & 0x3F]);
			buffer.append(BASE64_ALPHABET[(rawData[pos] << 4) & 0x3F]);
			buffer.append("==");
			break;
		case 2:
			int value = ((rawData[pos++] & 0xFF) << 8) | (rawData[pos] & 0xFF);
			buffer.append(BASE64_ALPHABET[(value >>> 10) & 0x3F]);
			buffer.append(BASE64_ALPHABET[(value >>> 4) & 0x3F]);
			buffer.append(BASE64_ALPHABET[(value << 2) & 0x3F]);
			buffer.append("=");
			break;
		}

		return buffer.toString();
	}

	/**
	 * Decodes the provided set of base64-encoded data.
	 * 
	 * @param encodedData The base64-encoded data to decode. It must not be
	 *                    <CODE>null</CODE>.
	 * @return The decoded raw data.
	 * @throws ParseException If a problem occurs while attempting to decode the
	 *                        provided data.
	 */
	public static byte[] decode(String encodedData) throws ParseException {
		// The encoded value must have length that is a multiple of four bytes.
		int length = encodedData.length();

		if ((length % 4) != 0) {
			// Message message = ERR_BASE64_DECODE_INVALID_LENGTH.get(encodedData);
			throw new ParseException("Base64 data was not 4-byte aligned", 0);
		}

		ByteBuffer buffer = ByteBuffer.allocate(length);
		for (int i = 0; i < length; i += 4) {
			boolean append = true;
			int value = 0;

			for (int j = 0; j < 4; j++) {
				switch (encodedData.charAt(i + j)) {
				case 'A':
					value <<= 6;
					break;
				case 'B':
					value = (value << 6) | 0x01;
					break;
				case 'C':
					value = (value << 6) | 0x02;
					break;
				case 'D':
					value = (value << 6) | 0x03;
					break;
				case 'E':
					value = (value << 6) | 0x04;
					break;
				case 'F':
					value = (value << 6) | 0x05;
					break;
				case 'G':
					value = (value << 6) | 0x06;
					break;
				case 'H':
					value = (value << 6) | 0x07;
					break;
				case 'I':
					value = (value << 6) | 0x08;
					break;
				case 'J':
					value = (value << 6) | 0x09;
					break;
				case 'K':
					value = (value << 6) | 0x0A;
					break;
				case 'L':
					value = (value << 6) | 0x0B;
					break;
				case 'M':
					value = (value << 6) | 0x0C;
					break;
				case 'N':
					value = (value << 6) | 0x0D;
					break;
				case 'O':
					value = (value << 6) | 0x0E;
					break;
				case 'P':
					value = (value << 6) | 0x0F;
					break;
				case 'Q':
					value = (value << 6) | 0x10;
					break;
				case 'R':
					value = (value << 6) | 0x11;
					break;
				case 'S':
					value = (value << 6) | 0x12;
					break;
				case 'T':
					value = (value << 6) | 0x13;
					break;
				case 'U':
					value = (value << 6) | 0x14;
					break;
				case 'V':
					value = (value << 6) | 0x15;
					break;
				case 'W':
					value = (value << 6) | 0x16;
					break;
				case 'X':
					value = (value << 6) | 0x17;
					break;
				case 'Y':
					value = (value << 6) | 0x18;
					break;
				case 'Z':
					value = (value << 6) | 0x19;
					break;
				case 'a':
					value = (value << 6) | 0x1A;
					break;
				case 'b':
					value = (value << 6) | 0x1B;
					break;
				case 'c':
					value = (value << 6) | 0x1C;
					break;
				case 'd':
					value = (value << 6) | 0x1D;
					break;
				case 'e':
					value = (value << 6) | 0x1E;
					break;
				case 'f':
					value = (value << 6) | 0x1F;
					break;
				case 'g':
					value = (value << 6) | 0x20;
					break;
				case 'h':
					value = (value << 6) | 0x21;
					break;
				case 'i':
					value = (value << 6) | 0x22;
					break;
				case 'j':
					value = (value << 6) | 0x23;
					break;
				case 'k':
					value = (value << 6) | 0x24;
					break;
				case 'l':
					value = (value << 6) | 0x25;
					break;
				case 'm':
					value = (value << 6) | 0x26;
					break;
				case 'n':
					value = (value << 6) | 0x27;
					break;
				case 'o':
					value = (value << 6) | 0x28;
					break;
				case 'p':
					value = (value << 6) | 0x29;
					break;
				case 'q':
					value = (value << 6) | 0x2A;
					break;
				case 'r':
					value = (value << 6) | 0x2B;
					break;
				case 's':
					value = (value << 6) | 0x2C;
					break;
				case 't':
					value = (value << 6) | 0x2D;
					break;
				case 'u':
					value = (value << 6) | 0x2E;
					break;
				case 'v':
					value = (value << 6) | 0x2F;
					break;
				case 'w':
					value = (value << 6) | 0x30;
					break;
				case 'x':
					value = (value << 6) | 0x31;
					break;
				case 'y':
					value = (value << 6) | 0x32;
					break;
				case 'z':
					value = (value << 6) | 0x33;
					break;
				case '0':
					value = (value << 6) | 0x34;
					break;
				case '1':
					value = (value << 6) | 0x35;
					break;
				case '2':
					value = (value << 6) | 0x36;
					break;
				case '3':
					value = (value << 6) | 0x37;
					break;
				case '4':
					value = (value << 6) | 0x38;
					break;
				case '5':
					value = (value << 6) | 0x39;
					break;
				case '6':
					value = (value << 6) | 0x3A;
					break;
				case '7':
					value = (value << 6) | 0x3B;
					break;
				case '8':
					value = (value << 6) | 0x3C;
					break;
				case '9':
					value = (value << 6) | 0x3D;
					break;
				case '+':
					value = (value << 6) | 0x3E;
					break;
				case '/':
					value = (value << 6) | 0x3F;
					break;
				case '=':
					append = false;
					switch (j) {
					case 2:
						buffer.put((byte) ((value >>> 4) & 0xFF));
						break;
					case 3:
						buffer.put((byte) ((value >>> 10) & 0xFF));
						buffer.put((byte) ((value >>> 2) & 0xFF));
						break;
					}
					break;
				default:
					// Message message = ERR_BASE64_DECODE_INVALID_CHARACTER.get(encodedData,
					// encodedData.charAt(i + j));
					throw new ParseException("Invalid Base64 character '" + encodedData.charAt(i + j) + "'", i + j);
				}

				if (!append) {
					break;
				}
			}

			if (append) {
				buffer.put((byte) ((value >>> 16) & 0xFF));
				buffer.put((byte) ((value >>> 8) & 0xFF));
				buffer.put((byte) (value & 0xFF));
			} else {
				break;
			}
		}

		buffer.flip();
		byte[] returnArray = new byte[buffer.limit()];
		buffer.get(returnArray);
		return returnArray;
	}

}
