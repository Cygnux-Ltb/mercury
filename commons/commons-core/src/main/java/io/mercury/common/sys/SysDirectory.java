package io.mercury.common.sys;

import java.io.File;

public enum SysDirectory {

	USER_HOME,

	IO_TMPDIR,

	LIBRARY_PATH,

	;

	private String absolutePath;
	private File file;

	private SysDirectory() {

	}

	public String absolutePath() {
		return absolutePath;
	}

	public File file() {
		return file;
	}

}
