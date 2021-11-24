package io.mercury.common.file.filefilter;

import java.io.File;
import java.io.FileFilter;

import javax.annotation.Nonnull;

public class FileNameEndsWithFilter implements FileFilter {

	private final String endsWithStr;
	private final boolean ignoreCase;

	public FileNameEndsWithFilter(@Nonnull String endsWithStr) {
		this(endsWithStr, true);
	}

	public FileNameEndsWithFilter(@Nonnull String endsWithStr, boolean ignoreCase) throws IllegalArgumentException {
		this.endsWithStr = endsWithStr;
		this.ignoreCase = ignoreCase;
	}

	@Override
	public boolean accept(@Nonnull File file) {
		if (ignoreCase) {
			return file.getName().toLowerCase().endsWith(endsWithStr.toLowerCase());
		} else {
			return file.getName().endsWith(endsWithStr);
		}
	}

}
