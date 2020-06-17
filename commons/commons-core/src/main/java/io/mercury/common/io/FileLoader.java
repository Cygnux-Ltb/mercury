package io.mercury.common.io;

import java.io.File;
import java.util.function.Predicate;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.set.MutableSet;

import io.mercury.common.collections.MutableSets;

public final class FileLoader {

	private FileLoader() {
	}

	/**
	 * 收集指定路径下全部文件
	 * 
	 * @param path
	 * @return
	 */
	@Nonnull
	public static final MutableSet<File> recursiveLoad(@Nonnull File path) {
		return recursiveLoad(path, any -> true);
	}

	/**
	 * 根据过滤器收集指定路径下全部文件, 如果过滤器为null, 默认收集全部文件
	 * 
	 * @param path
	 * @param fileFilter
	 * @return
	 */
	@Nonnull
	public static final MutableSet<File> recursiveLoad(@Nonnull File path, Predicate<File> fileFilter) {
		if (fileFilter == null)
			fileFilter = any -> true;
		MutableSet<File> files = MutableSets.newUnifiedSet();
		recursiveLoad0(files, path, fileFilter);
		return files;
	}

	private static final void recursiveLoad0(MutableSet<File> files, File path, Predicate<File> fileFilter) {
		if (path == null || fileFilter == null)
			return;
		File[] listFiles = path.listFiles();
		if (listFiles != null && listFiles.length != 0) {
			for (File file : listFiles)
				if (file.isDirectory()) {
					// 如果文件是一个目录, 递归执行
					recursiveLoad0(files, file, fileFilter);
				} else if (fileFilter.test(file)) {
					// 如果文件符合过滤器断言, 则加入Set
					files.add(file);
				} else
					// 否则忽略此文件
					continue;
		} else
			return;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 20; i++)
			if (i == 0)
				System.out.println("00" + 0);
			else if (i % 2 == 0)
				System.out.println(i);
			else
				System.out.println("PPPP");
	}

}
