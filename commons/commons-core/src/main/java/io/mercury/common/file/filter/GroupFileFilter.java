package io.mercury.common.file.filter;

import java.io.File;
import java.io.FileFilter;

import javax.annotation.Nonnull;

import io.mercury.common.util.Assertor;

public final class GroupFileFilter implements FileFilter {

	private final FileFilter[] filters;

	public GroupFileFilter(@Nonnull FileFilter... filters) {
		Assertor.requiredLength(filters, 1, "filters");
		this.filters = filters;
	}

	@Override
	public boolean accept(@Nonnull File file) {
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
