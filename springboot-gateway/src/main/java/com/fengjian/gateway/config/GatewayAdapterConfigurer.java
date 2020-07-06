/**
 * FileName: GateWayConfigurer
 * Author:   fengjian
 * Date:     2019-11-28 11:37
 * Description: 网关接入配置
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.config;

import com.fengjian.gateway.interceptor.DataDecryptAndEncryptInterceptor;
import com.fengjian.gateway.interceptor.DataMustCheckInterceptor;
import com.fengjian.gateway.interceptor.DataRegexCheckInterceptor;
import com.fengjian.gateway.interceptor.SignInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * 〈一句话功能简述〉<br> 
 * 〈网关接入配置〉
 *
 * @author fengjian
 * @create 2019-11-28
 * @since 1.0.0
 */
@Configuration
public class GatewayAdapterConfigurer implements WebMvcConfigurer {

    @Resource
    SignInterceptor signInterceptor;
    @Resource
    DataMustCheckInterceptor dataMustCheckInterceptor;
    @Resource
    DataDecryptAndEncryptInterceptor dataDecryptAndEncryptInterceptor;
    @Resource
    DataRegexCheckInterceptor dataRegexCheckInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 可添加多个
        /*验签*/
        registry.addInterceptor(signInterceptor).order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/demo/post2");
        /*必输*/
        registry.addInterceptor(dataMustCheckInterceptor).order(2)
                .addPathPatterns("/**");
        /*解密*/
        registry.addInterceptor(dataDecryptAndEncryptInterceptor).order(3)
                .addPathPatterns("/**");
        /*正则*/
        registry.addInterceptor(dataRegexCheckInterceptor).order(4)
                .addPathPatterns("/**");

    }
}
