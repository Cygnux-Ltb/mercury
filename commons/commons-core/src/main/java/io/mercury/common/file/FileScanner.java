package io.mercury.common.file;

import java.io.File;
import java.io.FileFilter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.eclipse.collections.api.set.MutableSet;

import io.mercury.common.collections.MutableSets;
import io.mercury.common.lang.Assertor;

public final class FileScanner {

	private FileScanner() {
	}

	/**
	 * 收集指定路径下全部文件
	 * 
	 * @param path
	 * @return
	 */
	@Nonnull
	public static final MutableSet<File> depthFirst(@Nonnull File path) {
		Assertor.nonNull(path, "path");
		return depthFirst(path, any -> true);
	}

	/**
	 * 根据过滤器收集指定路径下全部文件, 如果过滤器为null, 默认收集全部文件
	 * 
	 * @param path
	 * @param fileFilter
	 * @return
	 */
	@Nonnull
	public static final MutableSet<File> depthFirst(@Nonnull File path, @Nullable FileFilter filter) {
		Assertor.nonNull(path, "path");
		if (filter == null)
			filter = any -> true;
		MutableSet<File> files = MutableSets.newUnifiedSet();
		depthFirst0(files, path, filter);
		return files;
	}

	private static final void depthFirst0(@Nonnull MutableSet<File> files, @Nonnull File path,
			@Nonnull FileFilter filter) {
		if (path == null || filter == null)
			return;
		var listFiles = path.listFiles();
		if (listFiles != null && listFiles.length != 0) {
			for (File file : listFiles) {
				if (file.isDirectory())
					// 如果文件是一个目录, 递归执行
					depthFirst0(files, file, filter);
				else if (filter.accept(file))
					// 如果文件符合过滤器断言, 则加入Set
					files.add(file);
				else
					// 否则忽略此文件
					continue;
			}
		} else
			return;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++) {
			if (i == 0)
				System.out.println("00" + 0);
			else if (i % 2 == 0)
				System.out.println(i);
			else
				System.out.println("PPPP");
		}
	}

}
