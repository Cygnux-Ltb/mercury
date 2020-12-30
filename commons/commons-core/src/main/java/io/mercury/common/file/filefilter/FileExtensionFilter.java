package io.mercury.common.file.filefilter;

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

// java imports
import java.io.File;
import java.io.FileFilter;

import io.mercury.common.file.FileUtil;

/**
 * Accepts a file based on its file extension against a list of one or more
 * acceptable file extensions.
 * 
 * @author joelauer (twitter: @jjlauer or
 *         <a href="http://twitter.com/jjlauer" target=
 *         window>http://twitter.com/jjlauer</a>)
 */
public class FileExtensionFilter implements FileFilter {

	private final boolean ignoreCase;
	private String[] extensions;

	/**
	 * Creates a new instance of a <code>FileExtensionMatcher</code> that will
	 * perform a case insensitive match to this file extension.
	 * 
	 * @param extension The file extension to match against a File
	 * @throws IllegalArgumentException Thrown if the file extension is not
	 *                                  formatted correctly such as containing a
	 *                                  period.
	 */
	public FileExtensionFilter(String... extensions) throws IllegalArgumentException {
		this(true, extensions);
	}

	/**
	 * Creates a new instance of a <code>FileExtensionMatcher</code> that will
	 * perform a match to this array of file extensions.
	 * 
	 * @param extension     The array of file extensions to match against a File
	 * @param caseSensitive If true the extension must match the case of the
	 *                      provided extension, otherwise a case insensitive match
	 *                      will occur.
	 * @throws IllegalArgumentException Thrown if the file extension is not
	 *                                  formatted correctly such as containing a
	 *                                  period.
	 */
	public FileExtensionFilter(boolean ignoreCase, String... extensions) throws IllegalArgumentException {
		// check each extension
		for (String ext : extensions) {
			if (!FileUtil.isValidFileExtension(ext)) {
				throw new IllegalArgumentException("Invalid file extension '" + ext + "' cannot be matched");
			}
		}
		this.ignoreCase = ignoreCase;
		this.extensions = extensions;
	}

	/**
	 * Accepts a File by its file extension.
	 * 
	 * @param file The file to match
	 * @return True if the File matches this the array of acceptable file
	 *         extensions.
	 */
	@Override
	public boolean accept(File file) {
		// extract this file's extension
		String fileExt = FileUtil.parseFileExtension(file.getName());

		// a file extension might not have existed
		if (fileExt == null) {
			// if no file extension extracted, this definitely is not a match
			return false;
		}

		// does it match our list of acceptable file extensions?
		for (String extension : extensions) {
			if (ignoreCase) {
				if (fileExt.equalsIgnoreCase(extension))
					return true;
			} else {
				if (fileExt.equals(extension))
					return true;
			}
		}

		// if we got here, then no match was found
		return false;
	}

}
