package com.zbjdl.common.utils.event.text;

import com.zbjdl.common.utils.event.utils.URLUtils;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

public class StringUtilsEx extends StringUtils {
    private static char charInter = '\n';
    private static char charLt = '<';
    private static char charGt = '>';
    private static char charQuot = '"';
    private static char charAmp = '&';

    public StringUtilsEx() {
    }

    public static Map<String, String> uriQueryToMap(String query) {
        return uriQueryToMap(query, false, (String)null);
    }

    public static Map<String, String> uriQueryToMapWithDecode(String query) {
        return uriQueryToMap(query, "UTF-8");
    }

    public static Map<String, String> uriQueryToMap(String query, String charset) {
        return uriQueryToMap(query, true, charset);
    }

    public static Map<String, String> uriQueryToMap(String query, boolean encode, String charset) {
        return uriQueryToMap(query, encode, charset, false);
    }

    public static Map<String, String> uriQueryToMap(String query, boolean encode, String charset, boolean keepOrder) {
        Map<String, String> parameterMap = null;
        if (keepOrder) {
            parameterMap = new LinkedHashMap();
        } else {
            parameterMap = new HashMap();
        }

        if (null == query) {
            return (Map)parameterMap;
        } else {
            String paramString = query;
            int p = query.indexOf("?");
            if (p > -1) {
                paramString = query.substring(p + 1);
            }

            if (StringUtils.isNotBlank(paramString)) {
                String[] params = StringUtils.split(paramString, "&");
                String key = null;
                String value = null;
                String[] var10 = params;
                int var11 = params.length;

                for(int var12 = 0; var12 < var11; ++var12) {
                    String param = var10[var12];
                    if (StringUtils.isNotBlank(param)) {
                        p = param.indexOf("=");
                        if (p > 0) {
                            key = param.substring(0, p);
                            value = param.substring(p + 1);
                            if (encode) {
                                ((Map)parameterMap).put(key, URLUtils.decodeUrl(value, charset));
                            } else {
                                ((Map)parameterMap).put(key, value);
                            }
                        }
                    }
                }
            }

            return (Map)parameterMap;
        }
    }

    public static String[] splitOnce(String query, String separator) {
        int p = query.indexOf(separator);
        if (p > 0) {
            String key = query.substring(0, p);
            String value = query.substring(p + 1);
            return new String[]{key, value};
        } else {
            return new String[]{query};
        }
    }

    public static String mapToUriQuery(Map<String, String> map) {
        return mapToUriQuery(map, false, (String)null);
    }

    public static String mapToUriQueryWithEncode(Map<String, String> map) {
        return mapToUriQuery(map, "UTF-8");
    }

    public static String mapToUriQuery(Map<String, String> map, String charset) {
        return mapToUriQuery(map, true, charset);
    }

    public static String mapToUriQuery(Map<String, String> map, boolean encode, String charset) {
        if (null == map) {
            return "";
        } else {
            StringBuffer buff = new StringBuffer();
            Iterator<String> keys = map.keySet().iterator();
            String key = null;
            String value = null;

            while(keys.hasNext()) {
                key = (String)keys.next();
                value = (String)map.get(key);
                if (buff.length() > 0) {
                    buff.append("&");
                }

                if (encode) {
                    buff.append(key).append("=").append(URLUtils.encodeUrl(value, charset));
                } else {
                    buff.append(key).append("=").append(value);
                }
            }

            return buff.toString();
        }
    }

    public static String[] trim(String[] values) {
        String[] tv = new String[values.length];
        int i = 0;
        String[] var3 = values;
        int var4 = values.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String vv = var3[var5];
            tv[i++] = StringUtils.trimToEmpty(vv);
        }

        return tv;
    }

    public static boolean equals(Object me, String target) {
        return null != me ? equals(me.toString(), target) : false;
    }

    public static boolean equalsOr(String me, String[] targets) {
        String[] var2 = targets;
        int var3 = targets.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String target = var2[var4];
            if (StringUtils.equals(me, target)) {
                return true;
            }
        }

        return false;
    }

    public static boolean equalsOr(String me, List<String> targets) {
        Iterator var2 = targets.iterator();

        String target;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            target = (String)var2.next();
        } while(!StringUtils.equals(me, target));

        return true;
    }

    public static boolean equalsOr(String me, String targets) {
        return isNotEmpty(targets) && equalsOr(me, split(targets, ","));
    }

    public static boolean equalsAnd(String me, String[] targets) {
        String[] var2 = targets;
        int var3 = targets.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            String target = var2[var4];
            if (!StringUtils.equals(me, target)) {
                return false;
            }
        }

        return true;
    }

    public static String trim(String target, String defaultValue) {
        return StringUtils.isBlank(target) ? trim(defaultValue) : trim(target);
    }

    public static String toHtml(String strSource) {
        if (StringUtils.isBlank(strSource)) {
            return "";
        } else {
            StringBuffer strBufReturn = new StringBuffer();

            for(int i = 0; i < strSource.length(); ++i) {
                if (strSource.charAt(i) == charInter) {
                    strBufReturn.append("<BR>");
                } else if (strSource.charAt(i) == charLt) {
                    strBufReturn.append("<");
                } else if (strSource.charAt(i) == charGt) {
                    strBufReturn.append(">");
                } else if (strSource.charAt(i) == charQuot) {
                    strBufReturn.append("\"");
                } else if (strSource.charAt(i) == charAmp) {
                    strBufReturn.append("&");
                } else {
                    strBufReturn.append(strSource.charAt(i));
                }
            }

            return strBufReturn.toString();
        }
    }

    public static String stringToHTMLString(String string) {
        if (StringUtils.isBlank(string)) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer(string.length());
            boolean lastWasBlankChar = false;
            int len = string.length();
            boolean inHtml = false;

            for(int i = 0; i < len; ++i) {
                char c = string.charAt(i);
                if (c == ' ') {
                    if (lastWasBlankChar && !inHtml) {
                        lastWasBlankChar = false;
                        sb.append("&nbsp;");
                    } else {
                        lastWasBlankChar = true;
                        sb.append(' ');
                    }
                } else {
                    lastWasBlankChar = false;
                    if (c == charLt) {
                        inHtml = true;
                        sb.append(c);
                    } else if (c == charGt) {
                        inHtml = false;
                        sb.append(c);
                    } else if (c == '&' && i + 4 < len && string.charAt(i + 1) != 'n' && string.charAt(i + 2) != 'b' && string.charAt(i + 3) != 's' && string.charAt(i + 4) != 'p') {
                        sb.append("&amp;");
                    } else if (c == '\n') {
                        sb.append("<br>");
                    } else {
                        int ci = '\uffff' & c;
                        if (ci < 160) {
                            sb.append(c);
                        } else {
                            sb.append("&#");
                            sb.append((new Integer(ci)).toString());
                            sb.append(';');
                        }
                    }
                }
            }

            return sb.toString();
        }
    }

    public static String join(String... args) {
        return StringUtils.join(args);
    }
}
