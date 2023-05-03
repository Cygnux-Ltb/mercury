package io.mercury.common.file.filter;

import static io.mercury.common.lang.Asserter.requiredLength;

import java.io.File;
import java.io.FileFilter;

import javax.annotation.Nonnull;

/**
 * GroupFileFilter
 * 
 * @author yellow013
 */
public final class FileFilterGroup implements FileFilter {

	private final FileFilter[] filters;

	public FileFilterGroup(@Nonnull FileFilter... filters) {
		requiredLength(filters, 1, "filters");
		this.filters = filters;
	}

	@Override
	public boolean accept(@Nonnull File file) {
		// loop through every filter
		for (FileFilter filter : filters) {
			if (!filter.accept(file))
				return false;
		}
		// if we get here then everything matched!
		return true;
	}

}
