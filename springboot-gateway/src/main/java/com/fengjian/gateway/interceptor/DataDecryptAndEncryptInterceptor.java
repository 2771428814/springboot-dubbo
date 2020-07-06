/**
 * FileName: DataDecryptAndEncryptInterceptor
 * Author:   fengjian
 * Date:     2019-12-03 15:11
 * Description: 数据加解密拦截器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.interceptor;

import com.fengjian.dict.ConstEC;
import com.fengjian.filter.wappered.WrapperedRequest;
import com.fengjian.filter.wappered.WrapperedResponse;
import com.fengjian.gateway.utils.DataDecryptAndEncryptyUtil;
import com.fengjian.gateway.utils.ResultUtil;
import com.fengjian.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * 〈一句话功能简述〉<br>
 * 〈数据加解密拦截器〉
 *
 * @author fengjian
 * @create 2019-12-03
 * @since 1.0.0
 */
@Component
public class DataDecryptAndEncryptInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(DataDecryptAndEncryptInterceptor.class);

    @Value("${data.decryptAndEncryptKeys}")
    private String decryptAndEncryptKeys;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 仅对post请求的报文体进行数据加解密操作
        try {
            if ("POST".equalsIgnoreCase(request.getMethod()) && request instanceof WrapperedRequest) {
                logger.info("decryptAndEncryptKeys={}", decryptAndEncryptKeys);
                String body = ((WrapperedRequest) request).getBody();
                String decryptAndEncryptyJsonObject = DataDecryptAndEncryptyUtil.decryptAndEncryptyJsonObject(body, decryptAndEncryptKeys, 1);
                if (decryptAndEncryptyJsonObject.startsWith("Success")) {
                    body = decryptAndEncryptyJsonObject.replaceFirst("Success:", "");
                    logger.info("数据解密完成");
                    ((WrapperedRequest) request).resetBody(body);
                } else {
                    logger.info("数据解密失败。{}", decryptAndEncryptyJsonObject);

                    byte[] responseBytes = ResultUtil.fail(ConstEC.REQPARAM_CRYPTO_ERR,decryptAndEncryptyJsonObject).getBytes(StandardCharsets.UTF_8);
                    response.setContentLength(responseBytes.length);
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(responseBytes);
                    outputStream.flush();
                    outputStream.close();

                    return false;
                }

            }
        } catch (Exception e) {
            logger.error("拦截异常", e);
            return false;
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //对响应数据进行加密
        if (response instanceof WrapperedResponse) {
            String responseData = new String(((WrapperedResponse) response).getResponseData(), StandardCharsets.UTF_8);
            if (JsonUtil.jsonTypeCheck(responseData)>0){
                String decryptAndEncryptyJsonObject = DataDecryptAndEncryptyUtil.decryptAndEncryptyJsonObject(responseData, decryptAndEncryptKeys, 2);
                if (decryptAndEncryptyJsonObject.startsWith("Success")) {
                    logger.info("数据加密完成");
                    responseData = decryptAndEncryptyJsonObject.replaceFirst("Success:", "");
                } else {
                    logger.info("数据解密失败。{}", decryptAndEncryptyJsonObject);
                    responseData = decryptAndEncryptyJsonObject;
                }
                response.reset();
                byte[] responseBytes = responseData.getBytes(StandardCharsets.UTF_8);
                response.setContentLength(responseBytes.length);
                ServletOutputStream outputStream = response.getOutputStream();
                outputStream.write(responseBytes);
                outputStream.flush();
                outputStream.close();
            }else {
                logger.info("响应数据非json格式，无需加密");
            }

        }
    }
}
