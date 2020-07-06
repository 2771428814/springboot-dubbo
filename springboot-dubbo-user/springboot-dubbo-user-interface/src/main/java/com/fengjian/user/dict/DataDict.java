/**
 * FileName: DataDict
 * Author:   fengjian
 * Date:     2019-01-21 10:04
 * Description: 数据字典类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.dict;

/**
 * 〈一句话功能简述〉<br>
 * 〈数据字典类〉
 *
 * @author fengjian
 * @create 2019-01-21
 * @since 1.0.0
 */
public class DataDict {
    /**
     * 0: 通用不支持、否等情况
     **/
    public static final String COMMON_NO = "0";
    /**
     * 1：通用支持、是等情况
     **/
    public static final String COMMON_YES = "1";
    /**
     * N: 通用不支持、否等情况
     **/
    public static final String COMMON_N = "N";
    /**
     * Y：通用支持、是等情况
     **/
    public static final String COMMON_Y = "Y";

    /**
     * 需要加解密字段
     **/
    public static final String NEED_CRYPTO_NAME = "need_crypto_name";

    /**
     * EN:语言环境 英文
     */
    public static final String LOCALE_EN = "EN";
    /**
     * ZH:语言环境 中文
     */
    public static final String LOCALE_ZH = "ZH";

    /**
     * 启用标识 0 -启用
     */
    public static final String ENB_FLG_0 = "0";
    /**
     * 启用标识 1 -不启用
     */
    public static final String ENB_FLG_1 = "1";
    /**
     * B2B-B2B支付；
     */
    public static final String PAY_CHANNEL_B2B = "B2B";
    /**
     * B2C-B2C支付
     */
    public static final String PAY_CHANNEL_B2C = "B2C";

    /**
     * 支付类型
     * AA -直连网银
     */
    public static final String PAY_TYPE_AA = "AA";
    /**
     * 支付类型
     * AA -直连网银
     */
    public static final String PAY_TYPE_AB = "AB";
    /**
     * 支付类型
     * BA -API银行快捷借记卡
     */
    public static final String PAY_TYPE_BA = "BA";
    /**
     * 支付类型
     * BB -API银行快捷贷记卡；
     */
    public static final String PAY_TYPE_BB = "BB";




    /**
     * U：通用初始化等情况
     **/
    public static final String COMMON_STATUS_U = "U";
    /**
     * W：通用进行中等情况
     **/
    public static final String COMMON_STATUS_W = "W";
    /**
     * P：通用进行中等情况
     **/
    public static final String COMMON_STATUS_P = "P";
    /**
     * S：通用成功等情况
     **/
    public static final String COMMON_STATUS_S = "S";
    /**
     * F：通用失败情况
     **/
    public static final String COMMON_STATUS_F = "F";
    /**
     * C:被冲正
     **/
    public static final String COMMON_STATUS_C = "C";
    /**
     * T:发送超时
     **/
    public static final String COMMON_STATUS_T = "T";
    /**
     * X:发送失败
     **/
    public static final String COMMON_STATUS_X = "X";
    /**
     * R:交易撤消
     **/
    public static final String COMMON_STATUS_R = "R";
    /**
     * E:其他错误
     **/
    public static final String COMMON_STATUS_E = "E";


}
