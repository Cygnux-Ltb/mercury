package io.mercury.common.sys;

import java.io.File;

public enum SysDirectory {

	IO_TMPDIR(SysProperties.JAVA_IO_TMPDIR),

	USER_HOME(SysProperties.USER_HOME),

	USER_DIR(SysProperties.USER_DIR),

	;

	private final String absolutePath;
	private final File file;

	SysDirectory(String absolutePath) {
		this.absolutePath = absolutePath;
		this.file = new File(absolutePath);
	}

	public String absolutePath() {
		return absolutePath;
	}

	public File file() {
		return file;
	}

}
