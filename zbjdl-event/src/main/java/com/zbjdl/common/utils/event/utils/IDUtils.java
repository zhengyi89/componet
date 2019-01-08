//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.zbjdl.common.utils.event.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;

public class IDUtils {
    private static final String DATE_PATTERN = "yyyyMMdd";
    private static final Map<String, AtomicLong> cache = Collections.synchronizedMap(new HashMap());
    private static long MAX = 268435455L;
    private static long DAY = 86400000L;

    public IDUtils() {
    }

    private static int computeChecksum(String string) {
        int checksum = 0;

        for(int i = 0; i < string.length(); ++i) {
            checksum ^= string.charAt(i) - 48;
        }

        return checksum % 10;
    }

    private static AtomicLong getAtomicLong(String code) {
        if (cache.containsKey(code)) {
            return (AtomicLong)cache.get(code);
        } else {
            AtomicLong al = new AtomicLong(System.currentTimeMillis() % DAY);
            Map var2 = cache;
            synchronized(cache) {
                if (cache.containsKey(code)) {
                    return (AtomicLong)cache.get(code);
                } else {
                    cache.put(code, al);
                    return al;
                }
            }
        }
    }

    public static String permanentCookieUserId() {
        return generateId("310", (String)null);
    }

    public static String msgId() {
        try {
            return generateId("999", (String)null);
        } catch (Throwable var1) {
            return UUID.randomUUID().toString();
        }
    }

    public static String toID(String code) {
        return generateId(code, (String)null);
    }

    public static String toIDByManu(String code, String serverCode) {
        return generateId(code, serverCode);
    }

    private static String generateId(String bizId, String serverCode) {
        StringBuffer buffer = new StringBuffer(32);
        buffer.append(StringUtils.leftPad(bizId, 3, "0"));
        buffer.append(DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd"));
        String randomValue = serverCode;
        if (StringUtils.isBlank(serverCode)) {
            randomValue = "0000";
        }

        buffer.append(StringUtils.leftPad(randomValue, 4, "0"));
        long sequence = getAtomicLong(bizId).incrementAndGet();
        sequence %= MAX;
        buffer.append(StringUtils.leftPad(Long.toHexString(sequence), 7, "0"));
        int checksum = computeChecksum(buffer.toString());
        buffer.append(checksum);
        return buffer.toString();
    }
}
