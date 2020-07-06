/**
 * FileName: ConstMapDict
 * Author:   fengjian
 * Date:     2019-01-21 10:02
 * Description: 参数映射定义类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.dict;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈参数映射定义类〉
 *
 * @author fengjian
 * @create 2019-01-21
 * @since 1.0.0
 */
public class ConstMapDict {
    private static Map<String, String> reqConMap = new HashMap<String, String>();
    public static Map<String, String> noSignCheckMap = new HashMap<String, String>();

    static {
        reqConMap.put("11", "GGGGGGG");
        reqConMap.put("22", "HHHHHHH");

        noSignCheckMap.put("/oauth/authorize", DataDict.COMMON_YES);
        noSignCheckMap.put("/payments/payment/notify/Y", DataDict.COMMON_YES);



    }

    //翻转key value的例子
    public static Map<String, String> getBackValueMap() {
        Map<String, String> backMap = new HashMap<String, String>();
        for (String s : reqConMap.keySet()) {
            backMap.put(reqConMap.get(s), s);
        }
        return backMap;
    }
}
