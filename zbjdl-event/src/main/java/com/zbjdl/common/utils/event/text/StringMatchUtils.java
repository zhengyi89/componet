package com.zbjdl.common.utils.event.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

public class StringMatchUtils {
    private static final Logger logger = LoggerFactory.getLogger(StringMatchUtils.class);
    private static AntPathMatcher antmatcher = new AntPathMatcher();

    public StringMatchUtils() {
    }

    public static boolean stringMatch(String source, String[] pattern) {
        if (pattern.length == 1) {
            return stringMatch(source, pattern[0]);
        } else {
            boolean b = false;
            String[] var3 = pattern;
            int var4 = pattern.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String s = var3[var5];
                b = stringMatch(source, s);
                if (b) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean stringMatch(String source, String pattern) {
        try {
            Pattern sp = Pattern.compile(pattern);
            Matcher matcher = sp.matcher(StringUtils.trim(source));
            if (matcher.matches()) {
                return true;
            }
        } catch (Exception var4) {
            if (logger.isDebugEnabled()) {
                logger.debug("source : " + source + " | pattern : " + pattern, var4);
            }
        }

        return false;
    }

    public static boolean stringAntMatch(String source, String pattern) {
        return antmatcher.match(pattern, source);
    }

    public static void main(String[] args) {
        System.out.println(stringMatch("13910129320", "^(13[0-9]|15[0-9]|14[0-9]|18[7|8|9|6|5])\\d{4,8}$"));
    }
}
