package io.mercury.common.collections.keeper;

import io.mercury.common.file.PermissionDeniedException;

import java.io.File;

import static io.mercury.common.file.FileUtil.mkdir;

public abstract class FilesKeeper<K, V> extends AbstractKeeper<K, V> {

    protected void createFile(File file) throws PermissionDeniedException {
        if (!file.exists())
            // 创建文件目录
            if (!mkdir(file.getParentFile()))
                throw new PermissionDeniedException(file.getParentFile());
    }

}
