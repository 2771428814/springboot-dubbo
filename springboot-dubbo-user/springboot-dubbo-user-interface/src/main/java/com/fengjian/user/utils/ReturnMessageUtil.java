/*
 * FileName: ResultUtil
 * Author:   fengjian
 * Date:     2020-04-29 08:57
 * Description: 响应工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.utils;


import com.fengjian.user.business.dto.ReturnMessage;
import com.fengjian.user.dict.ConstEC;

/**
 * 〈响应工具类〉
 *
 * @author fengjian
 * @create 2020-04-29
 * @since 1.0.0
 */
public class ReturnMessageUtil {


    /**
     * 返回成功数据
     *
     * @param data 要返回的数据
     * @return 统一格式的结果
     */
    public static ReturnMessage success(Object data) {
        ReturnMessage result = new ReturnMessage(ConstEC.SUCCESS);
        result.setRetData(data);
        return result;
    }

    /**
     * 返回成功数据
     *
     * @return 统一格式的结果
     */
    public static ReturnMessage success() {
        return success(null);
    }

    /**
     * 返回失败数据
     *
     * @return 统一格式的结果
     */
    public static ReturnMessage fail(String code, String message) {
        return new ReturnMessage(code,message);
    }


}
