/**
 * 
 */
package ru.emdev.security.auth.tests;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Assert;
import org.junit.Test;

import ru.emdev.security.auth.util.net.IPAddressRange;
import ru.emdev.security.auth.util.net.IPUtil;

/**
 * @author Alexey Melnikov
 */
public class IPUtilTest {

	/**
	 * Test method for
	 * {@link ru.emdev.security.auth.util.net.IPUtil#isIPv4(java.lang.String)}.
	 */
	@Test
	public void testIsIPv4() {

		Assert.assertTrue("8.8.8.8",			IPUtil.isIPv4("8.8.8.8"));
		Assert.assertTrue("10.10.10.10",		IPUtil.isIPv4("10.10.10.10"));
		Assert.assertTrue("192.168.1.1",		IPUtil.isIPv4("192.168.1.1"));
		Assert.assertTrue("192.168.31.1",		IPUtil.isIPv4("192.168.31.1"));
		Assert.assertTrue("  192.168.31.1  ",	IPUtil.isIPv4("192.168.31.1"));
		Assert.assertTrue("\t\n192.168.31.1\n",	IPUtil.isIPv4("192.168.31.1"));

		Assert.assertFalse("1921.0.2.1", 		IPUtil.isIPv4("1921.0.2.1"));
		Assert.assertFalse("text 192.0.2.1",	IPUtil.isIPv4("some text 192.0.2.1"));
		Assert.assertFalse("10.10.10.10.",		IPUtil.isIPv4("10.10.10.10."));
		Assert.assertFalse("192.168.1.1/",		IPUtil.isIPv4("192.168.1.1/"));
		Assert.assertFalse("192.0.2.32/27",		IPUtil.isIPv4("192.0.2.32/27"));
		Assert.assertFalse("10.0.0.0/8",		IPUtil.isIPv4("10.0.0.0/8"));
		Assert.assertFalse("  10.0.0.0/8  ",	IPUtil.isIPv4("  10.0.0.0/8  "));
		Assert.assertFalse("\t10.0.0.0/8\n",	IPUtil.isIPv4("\t10.0.0.0/8\n"));
		Assert.assertFalse("10.0a.0.0/8", 		IPUtil.isIPv4("10.0a.0.0/8"));
		Assert.assertFalse("192.168.0.0.1/8", 	IPUtil.isIPv4("192.168.0.0.1/8"));
		Assert.assertFalse("google.com",		IPUtil.isIPv4("google.com"));
		Assert.assertFalse("192.168.1.1/002",	IPUtil.isIPv4("192.168.1.1/002"));
		Assert.assertFalse("",					IPUtil.isIPv4(""));
		Assert.assertFalse("  ",				IPUtil.isIPv4("  "));
		Assert.assertFalse("null",				IPUtil.isIPv4(null));
		Assert.assertFalse("255.255.255.256",	IPUtil.isIPv4("255.255.255.256"));
		Assert.assertFalse("255.255.256.255",	IPUtil.isIPv4("255.255.256.255"));
		Assert.assertFalse("255.256.255.255",	IPUtil.isIPv4("255.256.255.255"));
		Assert.assertFalse("256.255.255.255",	IPUtil.isIPv4("256.255.255.255"));
	}

	/**
	 * Test method for
	 * {@link ru.emdev.security.auth.util.net.IPUtil#isIPv4(java.lang.String)}.
	 */
	@Test
	public void testIsIPv4Range() {

		Assert.assertTrue("192.0.2.32/27",		IPUtil.isIPv4Range("192.0.2.32/27"));
		Assert.assertTrue("10.0.0.0/8",			IPUtil.isIPv4Range("10.0.0.0/8"));
		Assert.assertTrue("  10.0.0.0/8  ",		IPUtil.isIPv4Range("  10.0.0.0/8  "));
		Assert.assertTrue("\t\n10.0.0.0/8\n",	IPUtil.isIPv4Range("\t10.0.0.0/8\n"));

		Assert.assertFalse("10.0a.0.0/8", 		IPUtil.isIPv4Range("10.0a.0.0/8"));
		Assert.assertFalse("192.168.0.0.1/8", 	IPUtil.isIPv4Range("192.168.0.0.1/8"));
		Assert.assertFalse("google.com",		IPUtil.isIPv4Range("google.com"));
		Assert.assertFalse("192.168.1.1/002",	IPUtil.isIPv4Range("192.168.1.1/002"));
		Assert.assertFalse("1921.0.2.1", 		IPUtil.isIPv4Range("1921.0.2.1"));
		Assert.assertFalse("8.8.8.8",			IPUtil.isIPv4Range("8.8.8.8"));
		Assert.assertFalse("10.10.10.10",		IPUtil.isIPv4Range("10.10.10.10"));
		Assert.assertFalse("192.168.1.1",		IPUtil.isIPv4Range("192.168.1.1"));
		Assert.assertFalse("192.168.31.1",		IPUtil.isIPv4Range("192.168.31.1"));
		Assert.assertFalse("text 192.0.2.1",	IPUtil.isIPv4Range("some text 192.0.2.1"));
		Assert.assertFalse("10.10.10.10.",		IPUtil.isIPv4Range("10.10.10.10."));
		Assert.assertFalse("192.168.1.1/",		IPUtil.isIPv4Range("192.168.1.1/"));
		Assert.assertFalse("",					IPUtil.isIPv4Range(""));
		Assert.assertFalse("  ",				IPUtil.isIPv4Range("  "));
		Assert.assertFalse("null",				IPUtil.isIPv4Range(null));
		Assert.assertFalse("255.255.255.256/32",	IPUtil.isIPv4("255.255.255.256/32"));
		Assert.assertFalse("255.255.256.255/32",	IPUtil.isIPv4("255.255.256.255/32"));
		Assert.assertFalse("255.256.255.255/32",	IPUtil.isIPv4("255.256.255.255/32"));
		Assert.assertFalse("256.255.255.255/32",	IPUtil.isIPv4("256.255.255.255/32"));
		Assert.assertFalse("255.255.255.255/33",	IPUtil.isIPv4("255.255.255.255/33"));
	}
	
	/**
	 * Test method for
	 * {@link ru.emdev.security.auth.util.net.IPUtil#ipToLong(java.net.InetAddress)}
	 * .
	 */
	@Test
	public void testIpToLong() {
		try {
			long ip0_0_0_1 = 1l;
			Assert.assertEquals(ip0_0_0_1, IPUtil.ipToLong(InetAddress.getByName("0.0.0.1")));
			long ip192_168_0_1 = 3232235521l;
			Assert.assertEquals(ip192_168_0_1, IPUtil.ipToLong(InetAddress.getByName("192.168.0.1")));
			long ip255_255_255_255 = 4294967295l;
			Assert.assertEquals(ip255_255_255_255, IPUtil.ipToLong(InetAddress.getByName("255.255.255.255")));
		} catch (UnknownHostException e) {
			Assert.fail("Unexpected exception: " + e);
		}
	}

	/**
	 * Test method for
	 * {@link ru.emdev.security.auth.util.net.IPUtil#ipToLong(java.net.InetAddress)}
	 * .
	 */
	@Test
	public void testContains() {
		Assert.assertTrue("192.168.1.1-192.168.1.32 contains 192.168.1.15",
				IPAddressRange.fromFirstAndLast("192.168.1.1", "192.168.1.32").contains("192.168.1.15"));

		Assert.assertTrue("192.168.1.1-192.168.1.32 contains 192.168.1.1",
				IPAddressRange.fromFirstAndLast("192.168.1.1", "192.168.1.32").contains("192.168.1.1"));

		Assert.assertTrue("192.168.1.1-192.168.1.32 contains 192.168.1.32",
				IPAddressRange.fromFirstAndLast("192.168.1.1", "192.168.1.32").contains("192.168.1.32"));

		Assert.assertFalse("192.168.1.1-192.168.1.32 contains 192.168.1.0",
				IPAddressRange.fromFirstAndLast("192.168.1.1", "192.168.1.32").contains("192.168.1.0"));

		Assert.assertFalse("192.168.1.1-192.168.1.32 contains 192.168.1.33",
				IPAddressRange.fromFirstAndLast("192.168.1.1", "192.168.1.32").contains("192.168.1.33"));

		Assert.assertTrue("192.168.1.32/27 contains 192.168.1.55", 
				IPAddressRange.fromString("192.168.1.32/27").contains("192.168.1.55"));

		Assert.assertFalse("192.168.1.32/27 contains 192.168.1.15", 
				IPAddressRange.fromString("192.168.1.32/27").contains("192.168.1.15"));

		Assert.assertTrue("192.168.1.32/32 equals 192.168.1.32", 
				IPAddressRange.fromString("192.168.1.32/32").contains("192.168.1.32"));

		Assert.assertFalse("192.168.1.32/32 not equals 192.168.1.33", 
				IPAddressRange.fromString("192.168.1.32/32").contains("192.168.1.33"));

		Assert.assertTrue("192.168.1.32 equals 192.168.1.32", 
				IPAddressRange.fromString("192.168.1.32").contains("192.168.1.32"));

		Assert.assertFalse("192.168.1.32 not equals 192.168.1.33", 
				IPAddressRange.fromString("192.168.1.32").contains("192.168.1.33"));

	
		Assert.assertTrue(
				IPAddressRange.fromFirstAndLast(
						"fe80::226:2dff:fefa:cd1f",
						"fe80::226:2dff:fefa:ffff")
					.contains("fe80::226:2dff:fefa:dcba"));
		Assert.assertTrue(
				IPAddressRange.fromFirstAndLast(
						"fe80::226:2dff:fefa:cd1f",
						"fe80::226:2dff:fefa:ffff")
					.contains("fe80::226:2dff:fefa:cd1f"));
		Assert.assertTrue(
				IPAddressRange.fromFirstAndLast(
						"fe80::226:2dff:fefa:cd1f",
						"fe80::226:2dff:fefa:ffff")
					.contains("fe80::226:2dff:fefa:ffff"));

		Assert.assertTrue(
				IPAddressRange.fromString(
						"fe80::226:2dff:fefa:0/112")
					.contains("fe80::226:2dff:fefa:aaaa"));

		Assert.assertTrue(
				IPAddressRange.fromString(
						"FE80::226:2DFF:fefa:0/112")
					.contains("fe80::226:2dff:FEFA:AAAA"));

		Assert.assertTrue(
				IPAddressRange.fromString("::1")
					.contains("0000:0000:0000:0000:0000:0000:0000:0001"));

		Assert.assertFalse(
				IPAddressRange.fromFirstAndLast(
						"fe80::226:2dff:fefa:cd1f",
						"fe80::226:2dff:fefa:ffff")
					.contains("fe80::226:2dff:fefa:cd1e"));
		Assert.assertFalse(
				IPAddressRange.fromFirstAndLast(
						"fe80::226:2dff:fefa:cd1f",
						"fe80::226:2dff:fefa:fffe")
					.contains("fe80::226:2dff:fefa:ffff"));

		Assert.assertFalse(
				IPAddressRange.fromString(
						"fe80::226:2dff:fefa:0/113")
					.contains("fe80::226:2dff:fefa:ffff"));


		Class<? extends Exception> clazz = null;
		try {
			IPAddressRange.fromString("255.255.256.255").contains("255.255.256.255");
		} catch (Exception e) {
			clazz = e.getClass();
		} finally {
			Assert.assertTrue("Expected IllegalArgumentException", clazz == IllegalArgumentException.class);
		}

		try {
			IPAddressRange.fromString("255.255.256.255/32").contains("255.255.255.255");
		} catch (Exception e) {
			clazz = e.getClass();
		} finally {
			Assert.assertTrue("Expected IllegalArgumentException", clazz == IllegalArgumentException.class);
		}

		try {
			IPAddressRange.fromString("fe80::226:2dff:fefa:cd1g");
		} catch (Exception e) {
			clazz = e.getClass();
		} finally {
			Assert.assertTrue("Expected IllegalArgumentException", clazz == IllegalArgumentException.class);
		}

		try {
			IPAddressRange.fromString("fe80::226:2dff:fefa:0/129");
		} catch (Exception e) {
			clazz = e.getClass();
		} finally {
			Assert.assertTrue("Expected IllegalArgumentException", clazz == IllegalArgumentException.class);
		}

		try {
			IPAddressRange.fromString(":::1");
		} catch (Exception e) {
			clazz = e.getClass();
		} finally {
			Assert.assertTrue("Expected IllegalArgumentException", clazz == IllegalArgumentException.class);
		}
		
		try {
			IPAddressRange.fromFirstAndLast("192.168.1.1/31", "192.168.1.32/31");
		} catch (Exception e) {
			clazz = e.getClass();
		} finally {
			Assert.assertTrue("Expected IllegalArgumentException", clazz == IllegalArgumentException.class);
		}
		
		try {
			IPAddressRange.fromFirstAndLast("fe80::226:2dff:fefa:0/112","fe80::226:2dff:fefb:0/112");
		} catch (Exception e) {
			clazz = e.getClass();
		} finally {
			Assert.assertTrue("Expected IllegalArgumentException", clazz == IllegalArgumentException.class);
		}

	}
}
