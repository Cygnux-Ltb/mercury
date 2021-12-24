package io.mercury.common.net;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public final class NetworkProperties {

	public final static String getLocalMacAddress() {
		try {
			var networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				var networkInterface = networkInterfaces.nextElement();
				var mac = networkInterface.getHardwareAddress();
				
				if (mac == null)
					continue;
				else {
					var builder = new StringBuilder();
					for (int i = 0; i < mac.length; i++) {
						if (i != 0)
							builder.append('-');
						// (mac[i] & 0xFF) 将byte转换为正整数
						String hex = Integer.toHexString(mac[i] & 0xFF);
						if (hex.length() == 1)
							builder.append('0');
						builder.append(hex);
					}
					return builder.toString();
				}
			}
		} catch (SocketException e) {
		}
		return "";
	}

	public static void main(String[] args) throws UnknownHostException {
		System.out.println(getLocalMacAddress());
	}

}
