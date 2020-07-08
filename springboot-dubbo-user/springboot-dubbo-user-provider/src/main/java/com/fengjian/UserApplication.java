package com.fengjian;

import com.fengjian.user.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动主类
 * @author fengjian
 */

@SpringBootApplication
public class UserApplication
{
    public static void main( String[] args )
    {
        Logger logger = LoggerFactory.getLogger(UserApplication.class);
        ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(UserApplication.class)
                .run(args);
        SpringContextUtil.resetApplicationContext(applicationContext);
        logger.info("------===启动成功===------");
    }
}
