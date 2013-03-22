package ru.emdev.security.auth.util.net;

import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPUtil {

	private final static Pattern ip4pattern = Pattern.compile("^(\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})$");
	private final static Pattern ip4RangePattern = Pattern.compile("^(\\d{1,3}).(\\d{1,3}).(\\d{1,3}).(\\d{1,3})/(\\d{1,2})$");

	public static boolean isIPv4(String address) {
		if (address == null)
			return false;
		Matcher matcher = ip4pattern.matcher(address.trim());
		return isValidIPv4Values(matcher);
	}

	public static boolean isIPv4Range(String addressRange) {
		if (addressRange == null)
			return false;
		Matcher matcher = ip4RangePattern.matcher(addressRange.trim());
		boolean matches = isValidIPv4Values(matcher);
		matches = matches && Integer.valueOf(matcher.group(5)) <= 32;
		return matches;
	}

	public static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

    public boolean rangeContains(String from, String to, String address) {
    	IPAddressRange range = IPAddressRange.fromFirstAndLast(from, to);
    	return range != null && range.contains(address);
    }

    public boolean rangeContains(String mask, String address) {
    	IPAddressRange range = IPAddressRange.fromString(mask);
    	return range != null && range.contains(address);
    }

	private static boolean isValidIPv4Values(Matcher matcher) {
		boolean matches = matcher.matches();
		for (int i = 1; matches && i < 5; i++) {
			matches = Integer.valueOf(matcher.group(i)) <= 255;
		}
		return matches;
	}
}
