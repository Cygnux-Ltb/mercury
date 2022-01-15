package io.mercury.common.collections.window;

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

/**
 * Thrown if an offer cannot be accepted within a specified amount of time.
 * 
 * @author joelauer (twitter: @jjlauer or
 *         <a href="http://twitter.com/jjlauer" target=
 *         window>http://twitter.com/jjlauer</a>)
 */
public class OfferTimeoutException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5509263104569242653L;

	public OfferTimeoutException(String msg) {
		super(msg);
	}

	public OfferTimeoutException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
