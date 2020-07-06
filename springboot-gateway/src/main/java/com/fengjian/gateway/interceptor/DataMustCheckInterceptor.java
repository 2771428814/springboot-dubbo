/**
 * FileName: DataMustCheckInterceptor
 * Author:   fengjian
 * Date:     2019-12-02 10:43
 * Description: 数据必输校验 拦截器
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.interceptor;

import com.fengjian.dict.ConstEC;
import com.fengjian.filter.wappered.WrapperedRequest;
import com.fengjian.gateway.utils.DataMustCheckUtil;
import com.fengjian.gateway.utils.ResultUtil;
import com.fengjian.utils.json.JSONObject;
import com.fengjian.utils.PropertiesUtil;
import com.fengjian.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈数据必输校验 拦截器〉
 *
 * @author fengjian
 * @create 2019-12-02
 * @since 1.0.0
 */
@Component
public class DataMustCheckInterceptor implements HandlerInterceptor {
    private final static Logger logger = LoggerFactory.getLogger(DataMustCheckInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 只针对post请求，进行数据必输校验
        try {
            if ("POST".equalsIgnoreCase(request.getMethod()) && request instanceof WrapperedRequest) {
                String dataMustCheckKey = "";
                String lookupPath = new UrlPathHelper().getLookupPathForRequest(request);
                RequestMappingHandlerMapping requestMappingHandlerMapping = SpringContextUtil.getBean(RequestMappingHandlerMapping.class);
                Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
                java.util.List<String> matchingPatternsList;
                for (RequestMappingInfo info : map.keySet()) {
                    matchingPatternsList = info.getPatternsCondition().getMatchingPatterns(lookupPath);
                    if (matchingPatternsList.size() > 0) {
                        dataMustCheckKey = info.getName();
                        if (dataMustCheckKey == null) {
                            HandlerMethod handlerMethod = map.get(info);
                            logger.info("{} 注解RequestMapping中的name未设置，不进行数据必输校验。", handlerMethod.getMethod());
                            return true;
                        }
                        break;
                    }
                }
                String dataMustCheckValue = PropertiesUtil.getProperty("data.must." + dataMustCheckKey);
                logger.info("必输规则{}={}", "data.must." + dataMustCheckKey, dataMustCheckValue);

                String body = ((WrapperedRequest) request).getBody();
                String check = dataMustCheck(body, dataMustCheckValue);
                if (check.startsWith("Success")){
                    logger.info("必输校验通过");
                }else {
                    logger.info("必输校验失败。{}",check);

                    byte[]  responseBytes = ResultUtil.fail(ConstEC.REQPARAM_MUST_ERR,check).getBytes(StandardCharsets.UTF_8);
                    response.setContentLength(responseBytes.length);
                    ServletOutputStream outputStream = response.getOutputStream();
                    outputStream.write(responseBytes);
                    outputStream.flush();
                    outputStream.close();

                    return  false;
                }
            }
        } catch (Exception e) {
            logger.error("拦截异常", e);
            return false;
        }

        return true;
    }


    private String dataMustCheck(String jsonTypeString, String dataMustCheckValue) {
        if (dataMustCheckValue == null || dataMustCheckValue.length() <= 0) {
            return "Success：dataMustCheckValue is empty";
        }
        //传入参数校验，如果存在空值，直接抛出异常
        JSONObject requestJson;
        if (jsonTypeString == null || jsonTypeString.length() <= 0) {
            return "Fail：data is empty";
        } else {
            try {
                requestJson = new JSONObject(jsonTypeString);
            } catch (Exception e) {
                return "Fail：data is not json";
            }
        }


        String[] chkKeys = dataMustCheckValue.split(",");
        for (String key : chkKeys) {
            if (!DataMustCheckUtil.validJsonObject(requestJson.toString(), key)) {
                return "Fail："+key+" in data is not find";
            }
        }
        return "Success：dataMustCheck is success";

    }



}
