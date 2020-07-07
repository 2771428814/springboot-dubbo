package com.fengjian;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.fengjian.user.utils.SpringContextUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * 启动 - 主类
 *
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
