package io.mercury.common.file;

import static io.mercury.common.sys.SysProperties.JAVA_IO_TMPDIR;
import static io.mercury.common.sys.SysProperties.USER_HOME;

import java.io.File;

import javax.annotation.Nonnull;

public final class Files {

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static final File mkdirInTmp(@Nonnull File path) {
		if (path == null)
			return new File(JAVA_IO_TMPDIR);
		return mkdirInTmp(path.getPath());
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static final File mkdirInTmp(@Nonnull String path) {
		if (path == null)
			return new File(JAVA_IO_TMPDIR);
		File file = new File(JAVA_IO_TMPDIR, path);
		file.mkdirs();
		return file;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static final File mkdirInHome(@Nonnull File path) {
		if (path == null)
			return new File(USER_HOME);
		return mkdirInHome(path.getPath());
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static final File mkdirInHome(@Nonnull String path) {
		if (path == null)
			return new File(USER_HOME);
		File file = new File(USER_HOME, path);
		file.mkdirs();
		return file;
	}

	public static void main(String[] args) {

		System.out.println(Files.mkdirInHome("aaaa"));

	}

}
