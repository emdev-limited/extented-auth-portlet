package ru.emdev.security.auth.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPUtil {
    public static long ipToLong(InetAddress ip) {
        byte[] octets = ip.getAddress();
        long result = 0;
        for (byte octet : octets) {
            result <<= 8;
            result |= octet & 0xff;
        }
        return result;
    }

    public boolean rangeContains(String fromIp, String toIp, String ip) throws UnknownHostException {
        long ipLo = ipToLong(InetAddress.getByName(fromIp));
        long ipHi = ipToLong(InetAddress.getByName(toIp));
        long ipToTest = ipToLong(InetAddress.getByName(ip));

        return ipToTest >= ipLo && ipToTest <= ipHi;
    }
}
