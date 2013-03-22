/**
 * 
 */
package ru.emdev.security.auth.util.net;

import com.googlecode.ipv6.IPv6Address;
import com.googlecode.ipv6.IPv6AddressRange;
import com.googlecode.ipv6.IPv6Network;

/**
 * @author Alexey Melnikov
 */
public class IPAddressRange {

	private IPv4AddressRange ipv4AddressRange = null;
	private IPv6AddressRange ipv6AddressRange = null;

	IPAddressRange(String mask) {
		if (IPUtil.isIPv4(mask) || IPUtil.isIPv4Range(mask)) {
			ipv4AddressRange = IPv4Network.fromString(mask);
		} else {
			String range = mask;
			if (mask.indexOf('/') == -1) {
				range += "/128";
			}
			ipv6AddressRange = IPv6Network.fromString(range);
		}
	}

	IPAddressRange(String from, String to) {
		if (IPUtil.isIPv4Range(from) || IPUtil.isIPv4Range(to))
			throw new IllegalArgumentException(
					"IPv4 addresses range cannot be specified with CIDR notation");

		if (IPUtil.isIPv4(from) && IPUtil.isIPv4(to)) {
			ipv4AddressRange = IPv4AddressRange.fromFirstAndLast(from, to);
		} else {
			ipv6AddressRange = IPv6AddressRange.fromFirstAndLast(IPv6Address.fromString(from),
					IPv6Address.fromString(to));
		}
	}

	public static IPAddressRange fromString(String mask) {
		return new IPAddressRange(mask);
	}

	public static IPAddressRange fromFirstAndLast(String from, String to) {
		return new IPAddressRange(from, to);
	}

	public boolean contains(String address) {
		if (ipv4AddressRange != null)
			return ipv4AddressRange.contains(address);
		if (ipv6AddressRange != null)
			return ipv6AddressRange.contains(IPv6Address.fromString(address));
		return false;
	}
}