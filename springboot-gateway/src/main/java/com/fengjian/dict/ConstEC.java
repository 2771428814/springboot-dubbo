/**
 * FileName: ConstEC
 * Author:   fengjian
 * Date:     2019-01-21 10:01
 * Description: 错误码定义类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.dict;

/**
 * 〈一句话功能简述〉<br>
 * 〈错误码定义类〉
 *
 * @author fengjian
 * @create 2019-01-21
 * @since 1.0.0
 */
public class ConstEC {
    /**
     * 成功
     **/
    public static final String SUCCESS = "00000000";
    /**错误返回**/
    /*********************************************************************
     * 系统级错误返回
     * 返回码区间为：0000xxxx
     */
    /*v v v v v v v v v v v v000000xx:通用系统错误 begin v v v v v v v v v v v v*/
    /**系统异常*/
    public static final String SYSTEM_ERROR = "00000001";
    /**未知错误*/
    public static final String UNKNOWN = "00000002";
    /**调用其他系统异常*/
    public static final String REVOKE = "00000003";
    /**文件不存在*/
    public static final String FILE_NOT_EXIT = "00000004";
    /**连接超时*/
    public static final String CONNECTION_TIMEOUT = "00000005";
    /**签名失败*/
    public static final String SIGN_ERR = "00000006";
    /* ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ 000000xx:通用系统错误 end ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ */

    /*v v v v v v v v v v v v000001xx:请求参数错误v v v v v v v v v v v v*/
    /**通用数据校验未通过*/
    public static final String REQPARAM_ERR = "00000100";
    /**数据必输校验不通过*/
    public static final String REQPARAM_MUST_ERR = "00000101";
    /**数据正则校验不通过*/
    public static final String REQPARAM_REGEX_ERR = "00000102";
    /**数据加解密校验不通过*/
    public static final String REQPARAM_CRYPTO_ERR = "00000103";
    /**数据校验不通过*/
    public static final String REQPARAM_VALID_ERR = "00000104";
    /* ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ 000001xx:请求参数错误 end ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ */

    /*v v v v v v v v v v v v000002xx:数据库错误 beginv v v v v v v v v v v v*/
    /** 数据库错误*/
    public static final String DB_ERROR = "00000200";
    /* ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ 000002xx:数据库错误 end ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ ^ */

    /*********************************************************************
     * 业务错误错误返回
     * 返回码区间为：0001xxxx~0009xxxx
     */

    /**==============================
     * TEST2业务错误错误返回
     * 返回码区间为：0005xxxx
     * ==============================
     */

    /**==============================
     * TEST2业务错误错误返回
     * 返回码区间为：0005xxxx
     * ==============================
     */

}
