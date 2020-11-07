package io.mercury.common.file;

import java.io.File;

import io.mercury.common.sys.SysProperties;

public enum Files {

	;

	public static File mkdirsInTmp(File path) {
		if (path == null)
			return new File(SysProperties.JAVA_IO_TMPDIR);
		File file = new File(SysProperties.JAVA_IO_TMPDIR, path.getPath());
		file.mkdirs();
		return file;
	}

	public static File mkdirsInHome(File path) {
		if (path == null)
			return new File(SysProperties.USER_HOME);
		File file = new File(SysProperties.USER_HOME_FILE, path.getPath());
		file.mkdirs();
		return file;
	}

	public static void main(String[] args) {

		System.out.println(Files.mkdirsInHome(new File("aaaa")));

	}

}
