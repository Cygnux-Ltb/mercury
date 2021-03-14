package io.mercury.common.character;

public interface Separator {

	/**
	 * 行分割符
	 */
	String LINE_SEPARATOR = System.getProperty("line.separator");

	/**
	 * 文件分割符
	 */
	String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * 路径分割符
	 */
	String PATH_SEPARATOR = System.getProperty("path.separator");

	public static void main(String[] args) {

		System.out.println(LINE_SEPARATOR);
		System.out.println(LINE_SEPARATOR.length());

		System.out.println(FILE_SEPARATOR);
		System.out.println(FILE_SEPARATOR.length());

		System.out.println(PATH_SEPARATOR);
		System.out.println(PATH_SEPARATOR.length());

	}

}
