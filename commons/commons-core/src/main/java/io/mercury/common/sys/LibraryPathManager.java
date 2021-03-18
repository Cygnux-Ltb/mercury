package io.mercury.common.sys;

import java.io.File;
import java.lang.reflect.Field;

import io.mercury.common.character.Separator;
import io.mercury.common.util.PropertiesUtil;

public class LibraryPathManager {

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
		Field userPathsField = ClassLoader.class.getDeclaredField("usr_paths");
		userPathsField.setAccessible(true);
		String[] paths = (String[]) userPathsField.get(null);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < paths.length; i++) {
			if (libraryPath.equals(paths[i])) {
				continue;
			}
			sb.append(paths[i]).append(Separator.PATH_SEPARATOR);
		}
		sb.append(libraryPath);
		System.setProperty("java.library.path", sb.toString());
		final Field sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
		sysPathsField.setAccessible(true);
		sysPathsField.set(null, null);
	}

	public static void main(String[] args) {
		System.out.println("---old---");
		PropertiesUtil.showProperties(System.getProperties());
		try {
			addLibraryDir("~/java_lib");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("---nwe---");
		PropertiesUtil.showProperties(System.getProperties());
	}

}
