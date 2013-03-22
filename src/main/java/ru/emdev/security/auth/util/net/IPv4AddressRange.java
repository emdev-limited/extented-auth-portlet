package ru.emdev.security.auth.util.net;


/**
 * @author Alexey Melnikov
 */
public class IPv4AddressRange {

	protected IPv4Address from;
	protected IPv4Address to;

	IPv4AddressRange(IPv4Address from, IPv4Address to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * IPMask factory method.
	 * 
	 * @param mask
	 *            IP/Mask String in format "nnn.nnn.nnn.nnn/mask". If the
	 *            "/mask" is omitted, "/32" (just the single address) is
	 *            assumed.
	 * @return a new IPv4Network
	 */
	public static IPv4AddressRange fromFirstAndLast(String from, String to) {
		if (IPUtil.isIPv4Range(from) || IPUtil.isIPv4Range(to))
			throw new IllegalArgumentException("IPv4 address range cannot be specified with CIDR notation");

		IPv4Address fromAddr = IPv4Address.fromString(from);
		IPv4Address toAddr = IPv4Address.fromString(to);
		return fromFirstAndLast(fromAddr, toAddr);
	}

	/**
	 * @param fromString
	 * @param fromString2
	 * @return
	 */
	public static IPv4AddressRange fromFirstAndLast(IPv4Address from, IPv4Address to) {
		if (from == null || to == null)
			throw new IllegalArgumentException("All addresses should be not null");
		return new IPv4AddressRange(from, to);
	}

	/**
	 * Test given IPv4 address against this IPv4Networkk object.
	 * 
	 * @param testAddr
	 *            address to check.
	 * @return true if address is in the IPv4Network range, false if not.
	 */
	public boolean contains(IPv4Address test) {
		int from = this.from.getAddrInt();
		int to = this.to.getAddrInt();
		return test != null && (test.getAddrInt() >= from && test.getAddrInt() <= to);
	}

	/**
	 * Convenience method that converts String host to IPv4 address.
	 * 
	 * @param addr
	 *            IP address to match in nnn.nnn.nnn.nnn format or hostname.
	 * @return true if address is in the IP Mask range, false if not.
	 */
	public boolean contains(String addr) {
		IPv4Address address = IPv4Address.fromString(addr);
		return address != null && contains(address);
	}
}