package ru.emdev.security.auth.util.net;


/**
 * @author Alexey Melnikov
 */
public class IPv4Network extends IPv4AddressRange {

	private byte maskCtr;

	private int maskInt;

	IPv4Network(IPv4Address address, byte mask) {
		super(address, null);
		this.from = address;
		this.maskCtr = mask;

		this.maskInt = ~((1 << (32 - maskCtr)) - 1);
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
	public static IPv4Network fromString(String mask) {
		int pos = mask.indexOf('/');
		String addr;
		byte maskCtr;
		if (pos == -1) {
			addr = mask;
			maskCtr = 32;
		} else {
			addr = mask.substring(0, pos);
			maskCtr = Byte.parseByte(mask.substring(pos + 1));
		}

		IPv4Address address = IPv4Address.fromString(addr);

		if (address != null)
			return new IPv4Network(address, maskCtr);
		else
			return null;
	}

	/**
	 * Test given IPv4 address against this IPv4Networkk object.
	 * 
	 * @param test
	 *            address to check.
	 * @return true if address is in the IPv4Network range, false if not.
	 */
	@Override
	public boolean contains(IPv4Address test) {
		return ((from.getAddrInt() & maskInt) == (test.getAddrInt() & maskInt));
	}

	/**
	 * Convenience method that converts String host to IPv4 address.
	 * 
	 * @param addr
	 *            IP address to match in nnn.nnn.nnn.nnn format or hostname.
	 * @return true if address is in the IP Mask range, false if not.
	 */
	@Override
	public boolean contains(String addr) {
		IPv4Address address = IPv4Address.fromString(addr);
		return address != null && contains(address);
	}
}
