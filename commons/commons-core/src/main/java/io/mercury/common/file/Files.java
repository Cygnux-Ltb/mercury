package io.mercury.common.file;

import java.io.File;

import io.mercury.common.sys.SysProperties;

public final class Files {

	public static String mkdirInTempDir(File path) {
		if (path == null)
			return SysProperties.JAVA_IO_TMPDIR;
		File file = new File(SysProperties.JAVA_IO_TMPDIR, path.getPath());
		file.mkdirs();
		return file.getPath();
	}

	public static String mkdirInUserHome(File path) {
		if (path == null)
			return SysProperties.USER_HOME;
		File file = new File(SysProperties.USER_HOME_FILE, path.getPath());
		file.mkdirs();
		return file.getPath();
	}

	public static void main(String[] args) {

		System.out.println(Files.mkdirInUserHome(new File("aaaa")));

	}

}
