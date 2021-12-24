package io.mercury.transport;

public class IpAddressIllegalException extends IllegalArgumentException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3756639631788771826L;

	public IpAddressIllegalException(String ipAddress) {
		super(ipAddress);
	}

}
