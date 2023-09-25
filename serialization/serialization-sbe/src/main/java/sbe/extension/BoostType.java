/* Generated SBE (Simple Binary Encoding) message codec. */
package sbe.extension;

@SuppressWarnings("all")
public enum BoostType {
    TURBO((byte) 84),

    SUPERCHARGER((byte) 83),

    NITROUS((byte) 78),

    KERS((byte) 75),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((byte) 0);

    private final byte value;

    BoostType(final byte value) {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public byte value() {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static BoostType get(final byte value) {
        switch (value) {
            case 84:
                return TURBO;
            case 83:
                return SUPERCHARGER;
            case 78:
                return NITROUS;
            case 75:
                return KERS;
            case 0:
                return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
