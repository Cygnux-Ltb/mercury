/* Generated SBE (Simple Binary Encoding) message codec. */
package sbe.common;

/**
 * Meta attribute enum for selecting a particular meta attribute value.
 */
 @SuppressWarnings("all")
public enum MetaAttribute
{
    /**
     * The epoch or start of time. Default is 'UNIX' which is midnight 1st January 1970 UTC.
     */
    EPOCH,

    /**
     * Time unit applied to the epoch. Can be second, millisecond, microsecond, or nanosecond.
     */
    TIME_UNIT,

    /**
     * The type relationship to a FIX tag value encoded type. For reference only.
     */
    SEMANTIC_TYPE,

    /**
     * Field presence indication. Can be optional, required, or constant.
     */
    PRESENCE
}
