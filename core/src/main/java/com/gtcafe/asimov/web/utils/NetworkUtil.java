package com.gtcafe.asimov.web.utils;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public class NetworkUtil {
    
    public static String getHostName() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "Unknown Host";
        }
    }

    // @TODO: move out
    public static String getClientIp(HttpServletRequest request) {
        String ip = Optional.ofNullable(request.getHeader("X-Forwarded-For"))
                .map(forwarded -> forwarded.split(",")[0].trim()) // 取第一個 IP
                .orElseGet(() -> Optional.ofNullable(request.getHeader("X-Real-IP"))
                        .orElse(request.getRemoteAddr()));

        // 檢查是否為 IPv6 並轉換為 IPv4
        if (ip.contains(":")) {
            try {
                InetAddress inetAddress = InetAddress.getByName(ip);
                if (inetAddress instanceof Inet6Address) {
                    ip = convertIPv6ToIPv4(inetAddress);
                }
            } catch (UnknownHostException e) {
                return "Unknown IP";
            }
        }
        return ip;
    }

    // @TODO: move out
    public static String convertIPv6ToIPv4(InetAddress inetAddress) {
        if (inetAddress.isLoopbackAddress()) {
            return "127.0.0.1"; // 本機 IPv6 轉換
        }
        byte[] ipv4Bytes = inetAddress.getAddress();
        if (ipv4Bytes.length == 16) {
            return String.format("%d.%d.%d.%d", ipv4Bytes[12] & 0xFF, ipv4Bytes[13] & 0xFF, ipv4Bytes[14] & 0xFF,
                    ipv4Bytes[15] & 0xFF);
        }
        return inetAddress.getHostAddress(); // 若非 IPv6，直接回傳
    }
}
