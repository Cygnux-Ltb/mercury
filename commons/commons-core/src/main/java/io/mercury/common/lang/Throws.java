package io.mercury.common.lang;

public final class Throws {

    /**
     * @throws RuntimeException exception
     */
    public static void runtimeException() throws RuntimeException {
        throw new RuntimeException();
    }

    /**
     * @param msg String
     * @throws RuntimeException exception
     */
    public static void runtimeException(String msg) throws RuntimeException {
        throw new RuntimeException(msg);
    }

    /**
     * @param t Throwable
     * @throws RuntimeException exception
     */
    public static void runtimeException(Throwable t) throws RuntimeException {
        throw new RuntimeException(t);
    }

    /**
     * @param msg String
     * @param t   Throwable
     * @throws RuntimeException exception
     */
    public static void runtimeException(String msg, Throwable t) throws RuntimeException {
        throw new RuntimeException(msg, t);
    }

    /**
     * @throws NullPointerException exception
     */
    public static void nullPointer() throws NullPointerException {
        throw new NullPointerException();
    }

    /**
     * @param objName String
     * @throws NullPointerException exception
     */
    public static void nullPointer(String objName) throws NullPointerException {
        throw new NullPointerException("object name -> [" + objName + "]");
    }

    /**
     * @throws IllegalArgumentException exception
     */
    public static void illegalArgument() throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    /**
     * @param argumentName String
     * @throws IllegalArgumentException exception
     */
    public static void illegalArgument(String argumentName) throws IllegalArgumentException {
        throw new IllegalArgumentException("illegal argument -> " + argumentName);
    }

    /**
     * @param t Throwable
     * @throws IllegalArgumentException exception
     */
    public static void illegalArgument(Throwable t) throws IllegalArgumentException {
        throw new IllegalArgumentException(t);
    }

    /**
     * @param msg String
     * @param t   Throwable
     * @throws IllegalArgumentException exception
     */
    public static void illegalArgument(String msg, Throwable t) throws IllegalArgumentException {
        throw new IllegalArgumentException("illegal argument -> " + msg, t);
    }

    /**
     * @throws IllegalStateException exception
     */
    public static void illegalState() throws IllegalStateException {
        throw new IllegalStateException();
    }

    /**
     * @param state String
     * @throws IllegalStateException exception
     */
    public static void illegalState(String state) throws IllegalStateException {
        throw new IllegalStateException("state -> " + state);
    }

    /**
     * @param t Throwable
     * @throws IllegalStateException exception
     */
    public static void illegalState(Throwable t) throws IllegalStateException {
        throw new IllegalStateException(t);
    }

    /**
     * @param state String
     * @param t     Throwable
     * @throws IllegalStateException exception
     */
    public static void illegalState(String state, Throwable t) throws IllegalStateException {
        throw new IllegalStateException("state -> " + state, t);
    }

    /**
     * @param msg String
     * @throws UnsupportedOperationException uoe
     */
    public static void unsupportedOperation(String msg) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(msg);
    }

}
