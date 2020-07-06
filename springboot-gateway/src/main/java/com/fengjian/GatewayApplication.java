/**
 * FileName: GatewayApplication
 * Author:   fengjian
 * Date:     2019-11-28 10:18
 * Description: 网关启动
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian;

import com.fengjian.listener.PropertiesListener;
import com.fengjian.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 〈一句话功能简述〉<br>
 * 〈网关启动〉
 *
 * @author fengjian
 * @create 2019-11-28
 * @since 1.0.0
 */
@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(GatewayApplication.class);
        SpringApplication springApplication = new SpringApplication(GatewayApplication.class);
        springApplication.addListeners(
                new PropertiesListener("data-must-check.properties"),
                new PropertiesListener("data-regex-check.properties")
                );
        ConfigurableApplicationContext applicationContext = springApplication.run(args);
        SpringContextUtil.resetApplicationContext(applicationContext);
        logger.info("------===启动成功===------");
    }
}
