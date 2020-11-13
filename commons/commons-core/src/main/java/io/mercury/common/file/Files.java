package io.mercury.common.file;

import static io.mercury.common.sys.SysProperties.JAVA_IO_TMPDIR;
import static io.mercury.common.sys.SysProperties.USER_HOME;

import java.io.File;

import javax.annotation.Nonnull;

public enum Files {

	;

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static File mkdirsInTmp(@Nonnull File path) {
		if (path == null)
			return new File(JAVA_IO_TMPDIR);
		return mkdirsInTmp(path.getPath());
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static File mkdirsInTmp(@Nonnull String path) {
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
	public static File mkdirsInHome(@Nonnull File path) {
		if (path == null)
			return new File(USER_HOME);
		return mkdirsInHome(path.getPath());
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public static File mkdirsInHome(@Nonnull String path) {
		if (path == null)
			return new File(USER_HOME);
		File file = new File(USER_HOME, path);
		file.mkdirs();
		return file;
	}

	public static void main(String[] args) {

		System.out.println(Files.mkdirsInHome("aaaa"));

	}

}
