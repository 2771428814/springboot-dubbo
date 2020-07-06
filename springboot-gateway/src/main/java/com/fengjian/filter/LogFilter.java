package com.fengjian.filter;


import com.fengjian.filter.wappered.WrapperedRequest;
import com.fengjian.filter.wappered.WrapperedResponse;
import com.fengjian.utils.JsonUtil;
import com.fengjian.utils.SessionThreadLocalUtil;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@ServletComponentScan
@WebFilter(urlPatterns = {"/"}, filterName = "securityRequestFilter")
public class LogFilter implements Filter {
    public static final Logger logger = LoggerFactory.getLogger(LogFilter.class);
    public static final Logger mpapLogger = LoggerFactory.getLogger("mpsp");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Thread.currentThread().setName("GW" + new SimpleDateFormat("HHmmss").format(new Date()) + UUID.randomUUID().toString().substring(0, 7));
        long startTime = System.currentTimeMillis();
        WrapperedRequest wrapperedRequest = new WrapperedRequest((HttpServletRequest) request);
        WrapperedResponse wrapperedResponse = new WrapperedResponse((HttpServletResponse) response);
        logger.info("【  IP  】: {}", getCustIp((HttpServletRequest)request));
        logger.info("【  UA  】: {}", ((HttpServletRequest) request).getHeader(HttpHeaders.USER_AGENT));
        logger.info("【  URL 】: {}", ((HttpServletRequest) request).getRequestURL().toString());
        logger.info("【METHOD】: {}", ((HttpServletRequest) request).getMethod());

        String params;
        String method = ((HttpServletRequest) request).getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            Map<String, String[]> paramsArr = request.getParameterMap();
            if (CollectionUtils.isEmpty(paramsArr)) {
                logger.warn("Request for get method, param is empty, verification failed.");
            }
            params = new Gson().toJson(paramsArr);
        } else if ("POST".equalsIgnoreCase(method)) {
            // 此处读取了request中的inputStream，因为只能被读取一次，后面spring框架无法读取了，所以需要添加wrapper和filter解决流只能读取一次的问题

            String body = wrapperedRequest.getBody();

            if (body == null || body.length() <= 0) {
                logger.warn("Request for post method, body is empty,  verification failed.");
                throw new ServletException("Request for post method, body is empty,  verification failed.");
            }
            if ( JsonUtil.jsonTypeCheck(body) ==0) {
                logger.warn("Request for post method, body is not json,  verification failed.");
                throw new ServletException("Request for post method, body is not json,  verification failed.");
            }
            params = body;

        } else {
            logger.warn("Not supporting non-get or non-post requests,  verification failed.");
            throw new ServletException("Not supporting non-get or non-post requests,  verification failed.");
        }
        logger.info("【PARAMS】: {}", params);
        //将语言信息放入线程数据中，国际化时使用
        SessionThreadLocalUtil.setSessionValue("Accept-Language",String.valueOf( ((HttpServletRequest) request).getHeader("Accept-Language")));

        chain.doFilter(wrapperedRequest, wrapperedResponse);

        byte[] responseBytes = wrapperedResponse.getResponseData();
        String responseContent = new String(responseBytes, StandardCharsets.UTF_8);
        logger.info("【RETURN】: {}", responseContent);
        logger.info("【 TIME 】: {} ms",System.currentTimeMillis()-startTime);
        try {
            Map map = new Gson().fromJson(responseContent, Map.class);
            mpapLogger.info("URI:{},CODE:{},TIME:{}",((HttpServletRequest) request).getRequestURI(),map.get("code"),System.currentTimeMillis()-startTime);
        }catch (Exception e){
            logger.error("简要日志打印异常,流程不影响",e);
        }

        if ( JsonUtil.jsonTypeCheck(responseContent) >0) {
            response.setContentType("application/json;charset=UTF-8");
        }

        response.setContentLength(responseBytes.length);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(responseBytes);
        outputStream.flush();
        outputStream.close();

    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

    @Override
    public void destroy() {

    }
    public  String getCustIp(HttpServletRequest request) {
        String ipAddress = null;
        ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("X-Real-IP");
            if (null == ipAddress || "".equals(ipAddress.trim()))
                ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ipAddress = inet.getHostAddress();
            }

        }
        String ips = ipAddress;
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) { // "***.***.***.***".length()
            // = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        if ("unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = "";
        }
        logger.info(String.format("Proxy-Client-IP:[%s],WL-Proxy-Client-IP:[%s],X-Real-IP:[%s],remote:[%s]",
                request.getHeader("Proxy-Client-IP"), request.getHeader("WL-Proxy-Client-IP"),
                request.getHeader("X-Real-IP"), request.getRemoteAddr()));

        return ipAddress;
    }
}
