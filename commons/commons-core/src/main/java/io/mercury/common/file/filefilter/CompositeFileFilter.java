package io.mercury.common.file.filefilter;

import java.io.File;
import java.io.FileFilter;

public final class CompositeFileFilter implements FileFilter {

	private final FileFilter[] filters;

	public CompositeFileFilter(FileFilter... filters) {
		this.filters = filters;
	}

	@Override
	public boolean accept(File file) {
		// loop thru every filter
		for (FileFilter filter : filters) {
			if (!filter.accept(file)) {
				return false;
			}
		}
		// if we get here then everything matched!
		return true;
	}

}
