package io.mercury.common.character;

public interface ControlCharacter {

	/**
	 * Null
	 */
	String STR_NUL = new String(new byte[] { 0 });
	char NUL = STR_NUL.charAt(0);

	/**
	 * Start of Heading
	 */
	String STR_SOH = new String(new byte[] { 1 });
	char SOH = STR_SOH.charAt(0);

	/**
	 * Start of Text
	 */
	String STR_STX = new String(new byte[] { 2 });
	char STX = STR_STX.charAt(0);

	/**
	 * End of Text
	 */
	String STR_ETX = new String(new byte[] { 3 });
	char ETX = STR_ETX.charAt(0);

	/**
	 * End of Transmission
	 */
	String STR_EOT = new String(new byte[] { 4 });
	char EOT = STR_EOT.charAt(0);

	/**
	 * Acknowledgement
	 */
	String STR_ACK = new String(new byte[] { 6 });
	char ACK = STR_ACK.charAt(0);

}
