
package com.zbjdl.common.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

/**
 * IP处理工具类
 * 
 */
public class IpUtils {

	public static String getIpAddr(HttpServletRequest request) {
		String ipAddress = request.getHeader("x-forwarded-for");
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.length() == 0
				|| "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = request.getRemoteAddr();

			if (ipAddress.equals("127.0.0.1")) {
				// 根据网卡取本机配置的IP
				InetAddress inet = null;
				try {
					inet = InetAddress.getLocalHost();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				ipAddress = inet.getHostAddress();
			}

		}

		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		// "***.***.***.***".length() = 15
		if (ipAddress != null && ipAddress.length() > 15) {
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
			}
		}
		return ipAddress;
	}

	/**
	 * 获取本地ip地址，有可能会有多个地址, 若有多个网卡则会搜集多个网卡的ip地址
	 */
	public static Set<InetAddress> resolveLocalAddresses() {
		Set<InetAddress> addrs = new HashSet<InetAddress>();
		Enumeration<NetworkInterface> ns = null;
		try {
			ns = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			// ignored...
		}
		while (ns != null && ns.hasMoreElements()) {
			NetworkInterface n = ns.nextElement();
			Enumeration<InetAddress> is = n.getInetAddresses();
			while (is.hasMoreElements()) {
				InetAddress i = is.nextElement();
				if (!i.isLoopbackAddress() && !i.isLinkLocalAddress() && !i.isMulticastAddress()
						&& !isSpecialIp(i.getHostAddress())) {
					addrs.add(i);
				}
			}
		}
		return addrs;
	}

	public static Set<String> resolveLocalIps() {
		Set<InetAddress> addrs = resolveLocalAddresses();
		Set<String> ret = new HashSet<String>();
		for (InetAddress addr : addrs)
			ret.add(addr.getHostAddress());
		return ret;
	}

	private static boolean isSpecialIp(String ip) {
		if (ip.contains(":"))
			return true;
		if (ip.startsWith("127."))
			return true;
//		if (ip.startsWith("172.10."))
//			return true;
//		if (ip.startsWith("192.168."))
//			return true;
		if (ip.equals("255.255.255.255"))
			return true;
		return false;
	}
}
