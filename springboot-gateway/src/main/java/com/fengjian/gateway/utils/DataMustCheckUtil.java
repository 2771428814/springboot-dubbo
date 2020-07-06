package com.fengjian.gateway.utils;


import com.fengjian.utils.JsonUtil;
import com.fengjian.utils.json.JSONArray;
import com.fengjian.utils.json.JSONException;
import com.fengjian.utils.json.JSONObject;


/**
 * 请求参数必输性校验
 *
 * @author fengjian
 */
public class DataMustCheckUtil {


    /*
     * 判断是否是非必输字段，带有[]的是非必输字段
     */
    private static boolean isOptional(String key) {
        return key.startsWith("[") && key.endsWith("]");
    }





    /**
     * 校验必输字段是否存在值[内部使用]
     *
     * @param jsonObject jsonArrayORObject型字符串
     * @param key        必输字段名称
     * @return fengjian
     */
    public static boolean validJsonObject(String jsonObject, String key) {
        //非必传字段直接成功
        if (isOptional(key)){
            return  true;
        }
        String lastKey = null;//剩余json的key值 例如key=aaa.bbb那么lastKey开始为aaa.bbb循环第一次之后为bbb
        //判断json的类型1：是JsonObject 2 ：是JsonArray  
        int i = JsonUtil.jsonTypeCheck(jsonObject);
        if (i == 0) {
            //判断json的类型都不是的情况直接报错
            return false;
        } else if (i == 1) {
            //JsonObject的处理
            lastKey = key;
            for (String keyTemp : key.split("\\.")) {
                String replaceString = null;
                if (key.startsWith(keyTemp + ".")) {
                    replaceString = keyTemp + ".";
                } else {
                    replaceString = keyTemp;
                }
                lastKey = lastKey.replaceFirst(replaceString, "");
                if (isEmpty(lastKey)) {
                    //循环结束说明判断最底层keyTemp是否存在值确定必输性
                    try {
                        String value = new JSONObject(jsonObject).get(keyTemp).toString();
                        if (isEmpty(value)) {
                            return false;
                        } else {
                            return true;
                        }
                    } catch (Exception e) {
                        return false;
                    }
                } else {
                    //递归调用本身方法，继续验证
                    try {
                        String value = new JSONObject(jsonObject).get(keyTemp).toString();
                        if (isEmpty(value)) {
                            return false;
                        } else {
                            return validJsonObject(value, lastKey);
                        }
                    } catch (JSONException e) {
                        return false;
                    }
                }
            }
        } else if (i == 2) {
            //JsonArray  的处理
            JSONArray arrays;
            try {
                arrays = new JSONArray(jsonObject);
            } catch (JSONException e1) {
                return false;
            }
            for (int k = 0; k < arrays.length(); k++) {
                lastKey = key;
                for (String keyTemp : key.split("\\.")) {
                    String replaceString = null;
                    if (key.startsWith(keyTemp + ".")) {
                        replaceString = keyTemp + ".";
                    } else {
                        replaceString = keyTemp;
                    }
                    lastKey = lastKey.replaceFirst(replaceString, "");
                    if (isEmpty(lastKey)) {
                        //循环结束说明判断最底层keyTemp是否存在值确定必输性
                        try {
                            String value = new JSONObject(arrays.get(k).toString()).get(keyTemp).toString();
                            if (isEmpty(value)) {
                                return false;
                            } else {
                                continue;
                            }
                        } catch (Exception e) {
                            return false;
                        }
                    } else {
                        //递归调用本身方法，继续验证
                        try {
                            String value = ((JSONObject) arrays.get(k)).get(keyTemp).toString();
                            if (isEmpty(value)) {
                                return false;
                            } else {
                                return validJsonObject(value, lastKey);
                            }
                        } catch (JSONException e) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static boolean isEmpty(String string) {
        return (string == null || string.length() <= 0);
    }
}
