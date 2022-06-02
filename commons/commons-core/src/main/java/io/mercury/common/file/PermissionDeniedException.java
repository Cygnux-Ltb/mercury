package io.mercury.common.file;

import java.io.File;

public class PermissionDeniedException extends RuntimeException {

	private static final long serialVersionUID = 4465231636091844730L;

	public PermissionDeniedException(File file) {
		this(file.getAbsolutePath());
	}

	public PermissionDeniedException(String filepath) {
		super("The current user does not have permission to access -> " + filepath);
	}

}
