/**
 * FileName: Log4jEncodeLayout
 * Author:   fengjian
 * Date:     2019-01-04 14:41
 * Description: 敏感信息的编码或加密
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.log4j2;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.fengjian.user.utils.SpringContextUtil;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.AbstractStringLayout;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 〈一句话功能简述〉<br>
 * 〈敏感信息的编码或加密〉
 *
 * @author fengjian
 * @create 2019-01-04
 * @since 1.0.0
 */

@Plugin(name = "Log4jEncodeLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class Log4jEncodeLayout extends AbstractStringLayout {

    private PatternLayout patternLayout;

    protected Log4jEncodeLayout(Charset charset, String pattern) {
        super(charset);
        patternLayout = PatternLayout.newBuilder().withPattern(pattern).build();
        //init();
    }

    private static final Logger log = LoggerFactory.getLogger(Log4jEncodeLayout.class);
    /**
     * 匹配关键字配置
     */
    private String encodeCharKeys ;
    /**
     * 要匹配的正则表达式（身份证/银行卡）
     */
    private Pattern patternChar;
    /**
     * 要匹配的正则表达式（姓名）
     */
    private Pattern namepatternChar;
    /**
     * 要匹配的正则表达式（不显示值）
     */
    private Pattern unshowpatternChar;

    /**
     * 要加密匹配关键字的数目
     */
    private int encodeCharKeysNumner;
    /**
     * 要不显示匹配关键字的数目
     */
    private int unshowCharKeysNumber;

    private String[] charKeys;
    private String[] unshowcharKeys;

    private void init() {
        //从配置文件中读取
        try {
            encodeCharKeys = SpringContextUtil.getBean(Log4jEncodeConfig.class).getEncodeCharKeys();
        } catch (Exception e) {

        }

        StringBuffer charSbf = new StringBuffer();
        StringBuffer unshowSbf = new StringBuffer();
        String unshowCharKeys = "";
        //没有配置log4j.char.keys，不需要替换*
        if (StringUtils.isEmpty(encodeCharKeys)) {
            return;
        }

        //2、区分脱敏字段配置规则
        //把带[]的脱敏字段放入到unshowSbf中，其他脱敏字段放入到charSbf中

        // 读取配置中要加密的key
        String[] charKeysStrs = encodeCharKeys.split(",");
        for (int i = 0; i < charKeysStrs.length; i++) {
            if (isOptional(charKeysStrs[i])) {
                //带[]的脱敏字段
                unshowSbf.append(charKeysStrs[i].substring(1, charKeysStrs[i].length() - 1) + ",");
            } else {
                charSbf.append(charKeysStrs[i] + ",");
            }
        }
        encodeCharKeys = charSbf.toString();
        unshowCharKeys = unshowSbf.toString();
        //去掉拼转好的字符串最后一个逗号“，”
        if (StringUtils.isNotEmpty(encodeCharKeys) && encodeCharKeys.length() > 0) {
            encodeCharKeys = encodeCharKeys.substring(0, encodeCharKeys.length() - 1);
        }
        if (StringUtils.isNotEmpty(unshowCharKeys) && unshowCharKeys.length() > 0) {
            unshowCharKeys = unshowCharKeys.substring(0, unshowCharKeys.length() - 1);
        }

        //3、组织需要脱敏字段的正则表达式
        if (encodeCharKeys.length() > 0) {
            // 读取配置中要加密的key
            charKeys = encodeCharKeys.split(",");
            StringBuffer sbRegChar = new StringBuffer();
            sbRegChar.append("(");
            for (int i = 0; i < charKeys.length; i++) {
                sbRegChar.append("(");
                sbRegChar.append(charKeys[i]);
                sbRegChar.append(")");
                if (i < charKeys.length - 1) {
                    sbRegChar.append("|");
                }
            }
            StringBuffer namesbRegChar = new StringBuffer(sbRegChar);
            namesbRegChar.append(")(=|:|：|>)([\u4E00-\u9FA5\u00B7]{1,16})(;|,|\\||\\}|]|$|&|<)");
            //由于(\\s*\\w+={0,2})  key:value  中value中结尾包括==（base64导致，所以修改该写法）
            sbRegChar.append(")(=|:|：|>)(\\s*\\w+={0,2})(;|,|\\||\\}|]|$|&|<)");
            // 编译正则表达式
            namepatternChar = Pattern.compile(namesbRegChar.toString());
            // 编译正则表达式
            patternChar = Pattern.compile(sbRegChar.toString());
            encodeCharKeysNumner = charKeys.length;
        }


        if (unshowCharKeys.length() > 0) {
            // 读取配置中要加密的key
            unshowcharKeys = unshowCharKeys.split(",");
            StringBuffer unshowRegChar = new StringBuffer();
            unshowRegChar.append("(");
            for (int i = 0; i < unshowcharKeys.length; i++) {
                unshowRegChar.append("(");
                unshowRegChar.append(unshowcharKeys[i]);
                unshowRegChar.append(")");
                if (i < unshowcharKeys.length - 1) {
                    unshowRegChar.append("|");
                }
            }
            unshowRegChar.append(")(=|:|：|>)(.*?)(;|,|\\||\\}|]|$|&|<)");
            // 编译正则表达式
            unshowpatternChar = Pattern.compile(unshowRegChar.toString());
            unshowCharKeysNumber = unshowcharKeys.length;
        }
    }

    public static void main(String[] args) {
        Log4jEncodeLayout log = new Log4jEncodeLayout(null, "");
    }

    /**
     * 对银行卡以及身份证号脱敏处理
     * 1.判断配置文件中是否已经配置需要脱敏字段
     * 2.判断敏感信息内容是否需要处理
     * 2.1 处理银行卡和身份证敏感信息
     * 2.2 处理用户姓名敏感信息
     * 2.3 处理需要不展示字段（例如：cvv=123展示为cvv=）
     *
     * @param @param  msg
     * @param @return
     * @return String
     * @throws
     */
    private String dimChar(String msg) {
        try {
            if (encodeCharKeys==null) {
                //没有配置过滤信息。重新读取配置
                init();
            }


            //1.判断配置文件中是否已经配置需要脱敏字段
            if (encodeCharKeysNumner <= 0 && unshowCharKeysNumber <= 0) {
                return msg;
            }

            boolean nameFlag = false;
            Matcher match = null;
            StringBuffer sbMsg = new StringBuffer();
            if (patternChar != null) {
                match = patternChar.matcher(msg);
                // 处理要打印的日志信息
                //2.判断敏感信息内容是否需要处理
                while (match.find()) {
                    //2.1 处理银行卡和身份证敏感信息
                    nameFlag = true;
                    // group(1)匹配key，group(keys_length+2)匹配(=|:|：)，group(keys_length+3)匹配value，group(keys_length+4)匹配(;|,|\\||]|\\)|$)
                    match.appendReplacement(
                            sbMsg,
                            match.group(1)
                                    + match.group(encodeCharKeysNumner + 2)
                                    + replaceChar(match.group(encodeCharKeysNumner + 3))
                                    + match.group(encodeCharKeysNumner + 4));
                }
                match.appendTail(sbMsg);// 增加最后一个匹配项后面的值
            }

            if (namepatternChar != null) {
                //2.2 处理用户姓名敏感信息
                if (nameFlag) {
                    match = namepatternChar.matcher(sbMsg);
                } else {
                    match = namepatternChar.matcher(msg);
                }
                sbMsg = new StringBuffer();
                while (match.find()) {
                    nameFlag = true;
                    // group(1)匹配key，group(keys_length+2)匹配(=|:|：)，group(keys_length+3)匹配value，group(keys_length+4)匹配(;|,|\\||]|\\)|$)
                    match.appendReplacement(
                            sbMsg,
                            match.group(1)
                                    + match.group(encodeCharKeysNumner + 2)
                                    + replaceNameChar(match.group(encodeCharKeysNumner + 3))
                                    + match.group(encodeCharKeysNumner + 4));
                }
                match.appendTail(sbMsg);// 增加最后一个匹配项后面的值
            }

            if (unshowpatternChar != null) {
                if (nameFlag) {
                    match = unshowpatternChar.matcher(sbMsg);
                } else {
                    match = unshowpatternChar.matcher(msg);
                }
                sbMsg = new StringBuffer();
                //2.3 处理需要不展示字段（例如：cvv=123展示为cvv=）
                while (match.find()) {
                    // group(1)匹配key，group(keys_length+2)匹配(=|:|：)，group(keys_length+3)匹配value，group(keys_length+4)匹配(;|,|\\||]|\\)|$)
                    match.appendReplacement(
                            sbMsg,
                            match.group(1)
                                    + match.group(unshowCharKeysNumber + 2)
                                    + replaceCvvChar(match.group(unshowCharKeysNumber + 3))
                                    + match.group(unshowCharKeysNumber + 4));
                }
                match.appendTail(sbMsg);// 增加最后一个匹配项后面的值
            }
            return sbMsg.toString();
        } catch (Exception e) {
            //如果跑出异常为了不影响流程，直接返回原信息
            log.error(e.getMessage(), e);
        }
        return msg;
    }

    private boolean isOptional(String key) {
        return key.startsWith("[") && key.endsWith("]");
    }


    /**
     * 日志关键字用* 替换
     * 规则：  value <= 6     替换为******
     * 6 > value <= 12  后四位保留，其他为*
     * value > 12      前6位保留  中间*  后4位保留
     *
     * @param value
     * @return
     */
    private static String replaceChar(String value) {

        if (StringUtils.isEmpty(value)) {
            return "";
        }
        if (value.length() <= 6) {
            // value <= 6   //前1位保留  其他为*
            String replaceHeadStr = value.substring(0, 1);
            String dimStr = lStr("", '*', value.length() - 1);
            return replaceHeadStr + dimStr;

        } else if (value.length() > 6 && value.length() <= 12) {
            //后四位保留，其他为*
            int valLen = value.length();
            String replaceHeadStr = value.substring(value.length() - 4);
            return replaceHeadStr = lStr(replaceHeadStr, '*', valLen);
        } else if (value.length() > 12) {
            //前6位保留  中间*  后4位保留
            String replaceHeadStr = value.substring(0, 6);
            String replaceTailStr = value.substring(value.length() - 4);
            String dimStr = lStr("", '*', value.length() - 10);
            return replaceHeadStr + dimStr + replaceTailStr;
        } else {
            return value;
        }
    }

    /**
     * 日志姓名关键字替换
     * 保留第一个汉字，后边用*号表示（例如：张三--》张*）
     *
     * @param @param  value
     * @param @return
     * @return String
     * @throws
     */
    private static String replaceNameChar(String value) {

        if (StringUtils.isEmpty(value)) {
            return "";
        }
        String replaceHeadStr = value.substring(0, 1);
        return replaceHeadStr + lStr("", '*', value.length() - 1);
    }

    /**
     * 日志cvv关键字替换
     * 直接输出空（例如：1608--》）
     *
     * @param @param  value
     * @param @return
     * @return String
     * @throws
     */
    private static String replaceCvvChar(String value) {
        return "";
    }

    /*
     * 左补长char
     */
    public static String lStr(String s, char ch, int width) {

        if (s.length() < width) {
            // 需要前面补'0'
            while (s.length() < width) {
                s = ch + s;
            }
        } else {
            // 需要将高位丢弃
            s = s.substring(s.length() - width, s.length());
        }
        return s;
    }

    @Override
    public String toSerializable(LogEvent event) {
        String message = patternLayout.toSerializable(event);
        return dimChar(message);
    }

    @PluginFactory
    public static Layout createLayout(
            @PluginAttribute(value = "pattern") final String pattern,
            // LOG4J2-783 use platform default by default, so do not specify defaultString for charset
            @PluginAttribute(value = "charset") final Charset charset) {
        return new Log4jEncodeLayout(charset, pattern);
    }

}
