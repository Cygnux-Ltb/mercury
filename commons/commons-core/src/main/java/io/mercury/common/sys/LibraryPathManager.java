package io.mercury.common.sys;

import java.io.File;
import java.lang.reflect.Field;

import io.mercury.common.character.Separator;

public final class LibraryPathManager {

	/**
	 * 获取LibraryPath, System.getProperty("java.library.path")
	 * 
	 * @return
	 */
	public static String getLibraryPath() {
		return SysProperties.JAVA_LIBRARY_PATH;
	}

	/**
	 * 添加java.library.path
	 * 
	 * @param libraryPath
	 * @throws Exception
	 */
	public static void addLibraryDir(File libraryFile) throws Exception {
		addLibraryDir(libraryFile.getAbsolutePath());
	}

	/**
	 * 
	 * @param libraryPath
	 * @throws Exception
	 */
	public static void addLibraryDir(String libraryPath) throws Exception {
		Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		usrPathsField.setAccessible(true);
		String[] paths = (String[]) usrPathsField.get(null);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paths.length; i++) {
			if (libraryPath.equals(paths[i]))
				continue;
			sb.append(paths[i]).append(Separator.PATH_SEPARATOR);
		}
		sb.append(libraryPath);
		System.setProperty("java.library.path", sb.toString());
		final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
		sysPathsField.setAccessible(true);
		sysPathsField.set(null, null);
	}

}
