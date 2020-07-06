/**
 * FileName: SignInterceptor
 * Author:   fengjian
 * Date:     2019-11-29 16:06
 * Description: 签名拦截器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.interceptor;


import com.fengjian.dict.ConstEC;
import com.fengjian.filter.wappered.WrapperedRequest;
import com.fengjian.filter.wappered.WrapperedResponse;
import com.fengjian.gateway.utils.ResultUtil;
import com.fengjian.utils.MD5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 〈一句话功能简述〉<br>
 * 〈签名拦截器〉
 *
 * @author fengjian
 * @create 2019-11-29
 * @since 1.0.0
 */
@Component
public class SignInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(SignInterceptor.class);

    private final static String SIGN_KEY = "fengjian-sign-key";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只对post请求进行验签
        try {
            if ("POST".equalsIgnoreCase(request.getMethod()) && request instanceof WrapperedRequest) {
                String body = ((WrapperedRequest) request).getBody();
                String signature = request.getHeader("signature");
                String mySign = MD5Util.MD5(body + SIGN_KEY);
                if (signature == null || !signature.equalsIgnoreCase(mySign)) {
                    logger.info("验签结果：{},请求signature：{}，计算signature：{}", "失败", signature, mySign);


                    byte[]  responseBytes = ResultUtil.fail(ConstEC.SIGN_ERR,"Fail: signature verification failed").getBytes(StandardCharsets.UTF_8);
                    response.setContentLength(responseBytes.length);
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(responseBytes);
                    outputStream.flush();
                    outputStream.close();


                    return false;
                }

                logger.info("验签结果：{}", "成功");
            }
        } catch (Exception e) {
            logger.error("拦截异常", e);
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //对响应数据进行签名
        if (response instanceof WrapperedResponse) {
            String responseData = new String(((WrapperedResponse) response).getResponseData(), "utf-8");
            String signature = MD5Util.MD5(responseData+ SIGN_KEY);

            response.setHeader("signature", signature);


            logger.info("添加签名成功");
        }

    }
}
