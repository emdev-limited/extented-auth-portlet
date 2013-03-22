/**
 * 
 */
package ru.emdev.security.auth.util.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Alexey Melnikov
 */
public class IPv4Address {

	private InetAddress inetAddress;
	private int addrInt;

	IPv4Address(Inet4Address inetAddress) {
		this.inetAddress = inetAddress;
		this.addrInt = addrToInt(inetAddress);
	}

	public static IPv4Address fromString(String address) {
		if (!IPUtil.isIPv4(address))
			throw new IllegalArgumentException("'"+address+"' is not a valid IPv4 string");
		InetAddress inetAddress = null;
		try {
			inetAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e) { /* skip, not reachable */ }
		
		if (inetAddress != null && inetAddress instanceof Inet4Address)
			return new IPv4Address((Inet4Address)inetAddress);
		else
			return null;
	}

	/**
	 * Converts IPv4 address to integer representation.
	 */
	private static int addrToInt(Inet4Address inetAddress) {
		byte[] ba = inetAddress.getAddress();
		return (ba[0] << 24) | ((ba[1] & 0xFF) << 16) | ((ba[2] & 0xFF) << 8) | (ba[3] & 0xFF);
	}

	public int getAddrInt() {
		return this.addrInt;
	}

	@Override
	public String toString() {
		return "IPv4Address(" + inetAddress.getHostAddress() + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final IPv4Address that = (IPv4Address) obj;
		return (this.addrInt == that.addrInt);
	}

	@Override
	public int hashCode() {
		return this.addrInt;
	}
}