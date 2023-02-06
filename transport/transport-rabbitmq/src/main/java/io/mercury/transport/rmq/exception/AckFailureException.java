package io.mercury.transport.rmq.exception;

import java.io.Serial;

public class AckFailureException extends Exception {

    @Serial
    private static final long serialVersionUID = -197190157920481972L;

    /**
     * @param exchange       String
     * @param routingKey     String
     * @param confirmRetry   int
     * @param confirmTimeout long
     */
    public AckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout) {
        super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
                + "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]");
    }

    /**
     * @param exchange       String
     * @param routingKey     String
     * @param confirmRetry   int
     * @param confirmTimeout long
     * @param cause          Throwable
     */
    public AckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
                               Throwable cause) {
        super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
                + "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "]", cause);
    }

    /**
     * @param exchange       String
     * @param routingKey     String
     * @param confirmRetry   int
     * @param confirmTimeout long
     * @param msg            byte[]
     */
    public AckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout, byte[] msg) {
        super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
                + "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "], msg==["
                + new String(msg) + "]");
    }

    /**
     * @param exchange       String
     * @param routingKey     String
     * @param confirmRetry   int
     * @param confirmTimeout long
     * @param msg            byte[]
     * @param cause          Throwable
     */
    public AckFailureException(String exchange, String routingKey, int confirmRetry, long confirmTimeout,
                               byte[] msg, Throwable cause) {
        super("Call confirmPublish failure -> exchange==[" + exchange + "], routingKey==[" + routingKey
                + "], confirmRetry==[" + confirmRetry + "], confirmTimeout==[" + confirmTimeout + "], msg==["
                + new String(msg) + "]", cause);
    }

}
