package io.mercury.common.character;

public interface ControlCharacter {

	/**
	 * Null
	 */
	String NUL_STRING = new String(new byte[] { 0 });
	char NUL = NUL_STRING.charAt(0);

	/**
	 * Start of Heading
	 */
	String SOH_STRING = new String(new byte[] { 1 });
	char SOH = SOH_STRING.charAt(0);

	/**
	 * Start of Text
	 */
	String STX_STRING = new String(new byte[] { 2 });
	char STX = STX_STRING.charAt(0);

	/**
	 * End of Text
	 */
	String ETX_STRING = new String(new byte[] { 3 });
	char ETX = ETX_STRING.charAt(0);

	/**
	 * End of Transmission
	 */
	String EOT_STRING = new String(new byte[] { 4 });
	char EOT = EOT_STRING.charAt(0);

	/**
	 * Acknowledgement
	 */
	String ACK_STRING = new String(new byte[] { 6 });
	char ACK = ACK_STRING.charAt(0);

}
