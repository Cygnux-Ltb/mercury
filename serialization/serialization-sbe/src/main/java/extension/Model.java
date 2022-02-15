/* Generated SBE (Simple Binary Encoding) message codec. */
package extension;

public enum Model
{
    A((byte)65),

    B((byte)66),

    C((byte)67),

    /**
     * To be used to represent not present or null.
     */
    NULL_VAL((byte)0);

    private final byte value;

    Model(final byte value)
    {
        this.value = value;
    }

    /**
     * The raw encoded value in the Java type representation.
     *
     * @return the raw value encoded.
     */
    public byte value()
    {
        return value;
    }

    /**
     * Lookup the enum value representing the value.
     *
     * @param value encoded to be looked up.
     * @return the enum value representing the value.
     */
    public static Model get(final byte value)
    {
        switch (value)
        {
            case 65: return A;
            case 66: return B;
            case 67: return C;
            case 0: return NULL_VAL;
        }

        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
