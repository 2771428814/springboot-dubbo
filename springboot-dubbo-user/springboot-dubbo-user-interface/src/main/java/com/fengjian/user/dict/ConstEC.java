/**
 * FileName: ConstEC
 * Author:   fengjian
 * Date:     2019-01-21 10:01
 * Description: 错误码定义类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.dict;

/**
 * 〈一句话功能简述〉<br> 
 * 〈错误码定义类〉
 *
 * @author fengjian
 * @create 2019-01-21
 * @since 1.0.0
 */
public class ConstEC {
    /**成功**/
    public static final String SUCCESS = "00000000";
    /**错误返回**/
    /*********************************************************************
     * 系统级错误返回
     * 返回码区间为：0001xxxx
     */
    public static final String SYSTEM_ERROR = "00010000";
    public static final String UNKNOWN = "00010001"; //未知错误
    public static final String REVOKE = "00010002";
    public static final String FILE_NOT_EXIT="00010003";//文件不存在
    public static final String CONNECTION_TIMEOUT="00010004";//连接超时
    public static final String SIGN_ERR="00010005";//验签失败

    /*********************************************************************
     * 数据库错误返回
     * 返回码区间为：0002xxxx
     */
    public static final String DB_ERROR = "00020000";   //数据库错误
    /**数据不存在*/
    public static final String DB_DATA_NOT_EXIST = "00020001";

    /*********************************************************************
     * 参数校验错误错误返回
     * 返回码区间为：0003xxxx
     */
    public static final String REQPARAM_ERR="00030000"; //数据校验未通过
    public static final String REQPARAM_MUST_ERR="00030001"; //数据[%s]必输校验不通过
    public static final String REQPARAM_REGEX_ERR="00030002"; //数据[%s]正则[%s]校验不通过
    public static final String REQPARAM_CRYPTO_ERR="00030003"; //数据[%s]加解密校验不通过
    public static final String REQPARAM_VALID_ERR="00030004"; //数据[%s]校验不通过

    /*********************************************************************
     * 业务错误错误返回
     * 返回码区间为：0004xxxx~0009xxxx
     */
    /**==============================
     * Gateway业务错误错误返回
     * 返回码区间为：0004xxxx
     * ==============================
     */
    public static final String GATEWAY_ERR = "00040000";//网关 业务异常
    /**用户配置信息错误*/
    public static final String USER_CONFIG_ERR="00040001";
    /**
     * 支付通道方式错误
     */
    public static final String PAY_CHANNEL_TYPE_ERR="00040002";
    /**
     * 支付订单状态错误
     */
    public static final String PAY_ORDER_STATUS_ERR="00040003";


    /**==============================
     * TEST2业务错误错误返回
     * 返回码区间为：0005xxxx
     * ==============================
     */


}
