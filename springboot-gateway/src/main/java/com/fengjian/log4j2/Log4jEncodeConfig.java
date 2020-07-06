/**
 * FileName: Log4jEncodeConfig
 * Author:   fengjian
 * Date:     2019-01-08 14:47
 * Description: 日志脱敏配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.log4j2;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 〈一句话功能简述〉<br> 
 * 〈日志脱敏配置〉
 *
 * @author fengjian
 * @create 2019-01-08
 * @since 1.0.0
 */

@Component("GatewayLog4jEncodeConfig")
@Getter
@Setter
public class Log4jEncodeConfig {
    @Value("${log4jConfig.encodeCharKeys}")
    private String encodeCharKeys ;
}
