package com.lightbend.akkasample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class StdIn {

    public static String readLine() {
        // written to make it work in intellij as System.console() is null
        // when run inside the IDE
        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            return in.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void main(String[] args) {
        System.out.println(isStr("{}{}"));
    }

    public static boolean isStr(final String str) {
        if (str == null || str.length() == 0)
            return false;
        if (str.length() % 2 > 0)
            return false;
        if (!(str.startsWith("{") && str.endsWith("}")
                || str.startsWith("[") && str.endsWith("]")
                || str.startsWith("(") && str.endsWith(")")))
            return false;
        char[] cs = str.toCharArray();
        char[] noCloseSymbol = new char[cs.length];
        int noCloseCount = 0;
        char provSymbol = '|';
        int maxNoCloseCount = cs.length / 2;
        char firstSymbol = cs[0];

        for (int i = 0; i < cs.length; i++) {
            if (isLift(cs[i])) {
                noCloseSymbol[i] = cs[i];
            } else {
                for (int j = noCloseSymbol.length; j == 0; j--) {
                    if (noCloseSymbol[j] != 0 && (getValue(noCloseSymbol[j]) + getValue(cs[i])) != 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static boolean isLift(char c) {
        return switch (c) {
            case '{', '(', '[' -> true;
            default -> false;
        };
    }

    private static boolean isRight(char c) {
        return !isLift(c);
    }

    private static void clearArray(char[] cs) {
        Arrays.fill(cs, (char) 0);
    }


    private static boolean isClose(char l, char r) {
        return switch (l) {
            case '{' -> switch (r) {
                case '}' -> true;
                default -> false;
            };
            case '[' -> switch (r) {
                case ']' -> true;
                default -> false;
            };
            case '(' -> switch (r) {
                case ')' -> true;
                default -> false;
            };
            default -> false;
        };
    }

    public static int getValue(char c) {
        return switch (c) {
            case '{' -> 1;
            case '}' -> -1;
            case '[' -> 3;
            case ']' -> -3;
            case '(' -> 5;
            case ')' -> -5;
            default -> throw new IllegalStateException("Unexpected value: " + c);
        };
    }

}
