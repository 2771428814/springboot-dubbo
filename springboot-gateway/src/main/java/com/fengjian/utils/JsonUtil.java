/**
 * FileName: JsonUtil
 * Author:   fengjian
 * Date:     2019-12-03 16:18
 * Description: json工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.utils;

import com.fengjian.utils.json.JSONArray;
import com.fengjian.utils.json.JSONException;
import com.fengjian.utils.json.JSONObject;

/**
 * 〈一句话功能简述〉<br> 
 * 〈json工具类〉
 *
 * @author fengjian
 * @create 2019-12-03
 * @since 1.0.0
 */
public class JsonUtil {
   public static int jsonTypeCheck(String checkString) {
        try {
            JSONArray array = new JSONArray(checkString);
            return 2;
        } catch (JSONException e) {// 抛错 说明JSON字符不是数组或根本就不是JSON
            try {
                JSONObject object = new JSONObject(checkString);
                return 1;
            } catch (JSONException e2) {// 抛错 说明JSON字符根本就不是JSON
                //System.out.println("非法的JSON字符串");
                return 0;
            }
        }
    }
}
