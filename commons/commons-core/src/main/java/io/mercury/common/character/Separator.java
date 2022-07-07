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

    /**
     * char = '-';
     */
    String CHAR_LINE = "-";

    /**
     * char = ' ';
     */
    String CHAR_BLANK = " ";

}
