
package com.fengjian.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 线程数据存储
 *
 * @author fengjian
 * @create 2020年04月29日
 */
public class SessionThreadLocalUtil {

    private static final ThreadLocal<Map<String, String>> sessionIdLocal = new ThreadLocal<Map<String, String>>();

    public static void setSessionMap(Map<String, String> map) {
        sessionIdLocal.set(map);
    }

    public static Map<String, String> getSessionMap() {
        Map<String, String> map = sessionIdLocal.get();
        if (null == map) {
            map = new HashMap<String, String>();
            setSessionMap(map);
        }
        return map;
    }

    public static void setSessionValue(String key, String value) {
        Map<String, String> map = getSessionMap();
        map.put(key, value);

    }

    public static String getSessionValue(String key) {
        Map<String, String> map = getSessionMap();
        return map.get(key);

    }

    public static void putAll(Map<String, String> map) {
        Map<String, String> mapTemp = getSessionMap();
        mapTemp.putAll(map);
    }
}
