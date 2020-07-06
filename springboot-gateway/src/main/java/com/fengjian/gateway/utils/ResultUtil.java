/*
 * FileName: ResultUtil
 * Author:   fengjian
 * Date:     2020-04-29 08:57
 * Description: 响应工具类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.utils;

import com.fengjian.gateway.vo.ResultVo;
import com.fengjian.utils.SessionThreadLocalUtil;
import com.fengjian.utils.SpringContextUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.tools.internal.xjc.Language;
import com.sun.tools.internal.xjc.Messages;
import org.springframework.context.MessageSource;

import java.util.Locale;

/**
 * 〈响应工具类〉
 *
 * @author fengjian
 * @create 2020-04-29
 * @since 1.0.0
 */
public class ResultUtil {
    private static Gson gson = new GsonBuilder()
//          .excludeFieldsWithoutExposeAnnotation() //不对没有用@Expose注解的属性进行操作
            .enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
//            .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
            .setDateFormat("yyyy-MM-dd HH:mm:ss.SSS") //时间转化为特定格式
            .setPrettyPrinting() //对结果进行格式化，增加换行
            .disableHtmlEscaping() //防止特殊字符出现乱码
//	         .registerTypeAdapter(Paper.class,new UserAdapter()) //为某特定对象设置固定的序列或反序列方式，自定义Adapter需实现JsonSerializer或者JsonDeserializer接口
            .create();

    /**
     * 返回成功数据
     *
     * @param data 要返回的数据
     * @return 统一格式的结果
     */
    public static String success(Object data) {
        ResultVo result = new ResultVo();
        result.setCode("00000000");
        result.setMessage("success");
        result.setData(data);
        return gson.toJson(result);
    }

    /**
     * 返回成功数据
     *
     * @return 统一格式的结果
     */
    public static String success() {
        return success(null);
    }

    /**
     * 返回失败数据
     *
     * @return 统一格式的结果
     */
    public static String fail(String code, String message) {
        ResultVo result = new ResultVo();
        result.setCode(code);
        result.setMessage(message);
        return gson.toJson(result);
    }

    /**
     * 返回失败数据
     * 返回消息根据返回码进行国际化
     *
     * @return 统一格式的结果
     */
    public static String fail(String code) {
        //获取用户请求头的Accept-Language
        String acceptLanguage = SessionThreadLocalUtil.getSessionValue("Accept-Language");
        //目前只支持中文和英文，若请求头部的语言不包含中文则全部默认为英文
        Locale locale;
        if (acceptLanguage != null && acceptLanguage.toLowerCase().contains("zh")) {
            locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            locale = Locale.ENGLISH;
        }
        MessageSource messageSource = SpringContextUtil.getBean(MessageSource.class);
        String message = messageSource.getMessage(code,null, "system error",locale);
        return fail(code, message);
    }
}
