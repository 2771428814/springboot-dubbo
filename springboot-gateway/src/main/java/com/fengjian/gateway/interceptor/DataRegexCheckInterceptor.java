/**
 * FileName: DataRegexCheckInterceptor
 * Author:   fengjian
 * Date:     2019-12-04 10:39
 * Description: 数据正则校验拦截
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.interceptor;

import com.fengjian.dict.ConstEC;
import com.fengjian.filter.wappered.WrapperedRequest;
import com.fengjian.gateway.utils.DataRegexCheckUtil;
import com.fengjian.gateway.utils.ResultUtil;
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
 * 〈数据正则校验拦截〉
 *
 * @author fengjian
 * @create 2019-12-04
 * @since 1.0.0
 */
@Component
public class DataRegexCheckInterceptor implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(DataRegexCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //只针对post请求进行 数据正则校验
        try {
            if ("POST".equalsIgnoreCase(request.getMethod()) && request instanceof WrapperedRequest) {
                String body = ((WrapperedRequest) request).getBody();
                String validJsonObject = DataRegexCheckUtil.validJsonObject(body);
                if (validJsonObject.startsWith("Success")){
                    logger.info("数据正则校验通过");
                }else{
                    logger.info("数据正则校验失败。{}",validJsonObject);
                    byte[]  responseBytes = ResultUtil.fail(ConstEC.REQPARAM_REGEX_ERR,validJsonObject).getBytes(StandardCharsets.UTF_8);
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
}
