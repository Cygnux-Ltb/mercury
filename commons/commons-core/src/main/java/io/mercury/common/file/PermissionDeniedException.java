package io.mercury.common.file;

import java.io.File;

public class PermissionDeniedException extends RuntimeException {

    public PermissionDeniedException(File file) {
        this(file.getAbsolutePath());
    }

    public PermissionDeniedException(String filepath) {
        super("The current user does not have permission to access -> " + filepath);
    }

}
