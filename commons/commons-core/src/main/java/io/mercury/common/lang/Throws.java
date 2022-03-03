package io.mercury.common.lang;

public final class Throws {

    /**
     * @throws RuntimeException
     */
    public static void runtimeException() throws RuntimeException {
        throw new RuntimeException();
    }

    /**
     * @param msg
     * @throws RuntimeException
     */
    public static void runtimeException(String msg) throws RuntimeException {
        throw new RuntimeException(msg);
    }

    /**
     * @param t
     * @throws RuntimeException
     */
    public static void runtimeException(Throwable t) throws RuntimeException {
        throw new RuntimeException(t);
    }

    /**
     * @param msg
     * @param t
     * @throws RuntimeException
     */
    public static void runtimeException(String msg, Throwable t) throws RuntimeException {
        throw new RuntimeException(msg, t);
    }

    /**
     * @throws NullPointerException
     */
    public static void nullPointer() throws NullPointerException {
        throw new NullPointerException();
    }

    /**
     * @param obj
     * @throws NullPointerException
     */
    public static void nullPointer(String obj) throws NullPointerException {
        throw new NullPointerException("object name -> " + obj);
    }

    /**
     * @throws IllegalArgumentException
     */
    public static void illegalArgument() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    /**
     * @param msg
     * @throws IllegalArgumentException
     */
    public static void illegalArgument(String msg) throws IllegalArgumentException {
        throw new IllegalArgumentException("illegal argument -> " + msg);
    }

    /**
     * @param t
     * @throws IllegalArgumentException
     */
    public static void illegalArgument(Throwable t) throws IllegalArgumentException {
        throw new IllegalArgumentException(t);
    }

    /**
     * @param msg
     * @param t
     * @throws IllegalArgumentException
     */
    public static void illegalArgument(String msg, Throwable t) throws IllegalArgumentException {
        throw new IllegalArgumentException("illegal argument -> " + msg, t);
    }

    /**
     * @throws IllegalStateException
     */
    public static void illegalState() throws IllegalStateException {
        throw new IllegalStateException();
    }

    /**
     * @param state
     * @throws IllegalStateException
     */
    public static void illegalState(String state) throws IllegalStateException {
        throw new IllegalStateException("state -> " + state);
    }

    /**
     * @param t
     * @throws IllegalStateException
     */
    public static void illegalState(Throwable t) throws IllegalStateException {
        throw new IllegalStateException(t);
    }

    /**
     * @param state
     * @param t
     * @throws IllegalStateException
     */
    public static void illegalState(String state, Throwable t) throws IllegalStateException {
        throw new IllegalStateException("state -> " + state, t);
    }

}
