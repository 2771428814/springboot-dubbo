/**
 * FileName: PropertiesUtil
 * Author:   fengjian
 * Date:     2019-12-02 15:15
 * Description: 自定义配置文件工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.utils;

import org.springframework.beans.BeansException;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 〈PropertiesListener  在项目启动时将配置文件通过loadAllProperties加载进静态变量propertiesMap中 〉<br>
 * 〈自定义配置文件工具类〉
 *
 * @author fengjian
 * @create 2019-12-02
 * @since 1.0.0
 */
public class PropertiesUtil {
    private static Map<String, String> propertiesMap = new HashMap<>();

    private static void processProperties(Properties props) throws BeansException {
        //propertiesMap = new HashMap<String, String>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            try {
                // PropertiesLoaderUtils的默认编码是ISO-8859-1,在这里转码一下
                propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadAllProperties(String propertyFileName) {
        try {
            Properties properties = PropertiesLoaderUtils.loadAllProperties(propertyFileName);
            processProperties(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String name) {
        return propertiesMap.get(name);
    }

    public static Map<String, String> getAllProperty() {
        return propertiesMap;
    }
}
