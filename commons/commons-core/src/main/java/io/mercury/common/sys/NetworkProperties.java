package io.mercury.common.sys;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public final class NetworkProperties {

	public static final InetAddress LocalInetAddress = getlocalInetAddress();

	public static final String LocalHostAddress = LocalInetAddress.getHostAddress();

	public static final String LocalMacAddress = getMacAddress(LocalInetAddress);

	public static final NetworkInterface LocalNetworkInterface = getNetworkInterface(LocalInetAddress);

	private static InetAddress getlocalInetAddress() {
		try {
			return InetAddress.getLocalHost();
			// TODO
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public static NetworkInterface getNetworkInterface(InetAddress inetAddress) {
		try {
			return NetworkInterface.getByInetAddress(inetAddress);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getMacAddress(InetAddress inetAddress) {
		@SuppressWarnings("unused")
		NetworkInterface networkInterface = getNetworkInterface(inetAddress);
		try {
			// 获得网络接口对象（即网卡），并得到mac地址，mac地址存在于一个byte数组中。
			byte[] mac = LocalNetworkInterface.getHardwareAddress();
			// 下面代码是把mac地址拼装成String
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < mac.length; i++) {
				if (i != 0)
					sb.append("-");
				// mac[i] & 0xFF 是为了把byte转化为正整数
				String s = Integer.toHexString(mac[i] & 0xFF);
				sb.append(s.length() == 1 ? 0 + s : s);
			}
			// 把字符串所有小写字母改为大写成为正规的mac地址并返回
			return sb.toString().toUpperCase();
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		try {
			Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
			while (networkInterfaces.hasMoreElements()) {
				NetworkInterface networkInterface = networkInterfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					System.out.println(inetAddress instanceof Inet4Address);
					System.out.println(inetAddress instanceof Inet6Address);

					System.out.println(inetAddress.getHostAddress());
				}
			}

			System.out.println(LocalInetAddress.getHostAddress());
			System.out.println(LocalInetAddress instanceof Inet4Address);
			System.out.println(LocalInetAddress instanceof Inet6Address);

		} catch (SocketException e) {
			e.printStackTrace();
		}

		// System.out.println(NetworkPropertys.LocalHostAddress);
		// System.out.println(NetworkPropertys.LocalMacAddress);
	}

}
