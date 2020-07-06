/**
 * FileName: BusinessException
 * Author:   fengjian
 * Date:     2019-01-21 09:47
 * Description: 业务异常类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.exception;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 〈一句话功能简述〉<br> 
 * 〈业务异常类〉
 *
 * @author fengjian
 * @create 2019-01-21
 * @since 1.0.0
 */
@Getter
@Setter
public class BusinessException extends  RuntimeException{
    final private Logger logger = LoggerFactory.getLogger(BusinessException.class);

    private String code;
    private String message;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        logger.error(String.format("业务异常：%s - %s",code,message));
    }
}
