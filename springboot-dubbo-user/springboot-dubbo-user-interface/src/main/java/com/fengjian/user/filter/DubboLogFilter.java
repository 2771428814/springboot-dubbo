/**
 * FileName: DubboLogFilter
 * Author:   fengjian
 * Date:     2018-12-24 14:55
 * Description: DUBBO 日志
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.filter;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.alibaba.dubbo.rpc.*;
import com.fengjian.user.dict.ConstEC;
import com.fengjian.user.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import  com.fengjian.user.business.dto.*;


/**
 * 〈一句话功能简述〉<br>
 * 〈DUBBO 日志〉
 *
 * @author fengjian
 * @create 2019-01-04
 * @since 1.0.0
 */
public class DubboLogFilter implements Filter {
    private final static Logger logger = LoggerFactory.getLogger(DubboLogFilter.class);
    private final static Logger mpspLogger = LoggerFactory.getLogger("mpsp");

    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        //  日志穿透
        // >>>>>>>>>>>>>>>>>>>>>
        //优先获取dubbo上下文中的rpid，再获取 TraceIdUtils，最后获取线程名称
        String rpid = RpcContext.getContext().getAttachment("rpid");
        if (StringUtils.isEmpty(rpid)) {
            rpid = Thread.currentThread().getName();
        }
        RpcContext.getContext().setAttachment("rpid", rpid);
        Thread.currentThread().setName(rpid);

        // <<<<<<<<<<<<<<<<<<<<<<
        //  打印IP
        // >>>>>>>>>>>>>>>>>>>>>
        boolean isConsumerSide = RpcContext.getContext().isConsumerSide();
        if (isConsumerSide) {
            logger.info(String.format("客户端[%s]>>>>>>>>服务端IP[%s]", RpcContext.getContext().getLocalHost(), RpcContext.getContext().getRemoteHost()));
        } else {
            logger.info(String.format("客户端[%s]>>>>>>>>服务端IP[%s]", RpcContext.getContext().getRemoteHost(), RpcContext.getContext().getLocalHost()));

        }
        // <<<<<<<<<<<<<<<<<<<<<<

        long startTime = System.currentTimeMillis();
        // 打印请求和响应
        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>
        DubboMessage filterRsp = new DubboMessage();
        DubboMessage filterReq = new DubboMessage();
        try {
            filterReq.setInterfaceName(invocation.getInvoker().getInterface().getName());
            filterReq.setMethodName(invocation.getMethodName());
            filterReq.setArgs(invocation.getArguments());

            logger.info("dubbo请求数据:" + filterReq.toString());

            Result result = invoker.invoke(invocation);
            if (result.hasException()) {
                logger.error("dubbo执行异常", result.getException());

                if(result.getException() instanceof BusinessException){
                    BusinessException businessException = (BusinessException)result.getException();
                    ReturnMessage returnMessage = new ReturnMessage();
                    returnMessage.setRetCode(businessException.getCode());
                    returnMessage.setRetMessage(businessException.getMessage());
                    ((RpcResult) result).setException(null);
                    ((RpcResult)result).setValue(returnMessage);
                }else{
                    ReturnMessage returnMessage = new ReturnMessage();
                    returnMessage.setRetCode(ConstEC.REVOKE);
                    returnMessage.setRetMessage(result.getException().getMessage());
                    ((RpcResult) result).setException(null);
                    ((RpcResult)result).setValue(returnMessage);
                }

            } else {
                logger.debug("dubbo执行成功!");


                filterRsp.setMethodName(invocation.getMethodName());
                filterRsp.setInterfaceName(invocation.getInvoker().getInterface().getName());
                filterRsp.setArgs(new Object[]{result.getValue()});
                logger.info("dubbo返回数据" + filterRsp.toString());

            }
            //打印简要日志
            if (isConsumerSide) {
                mpspLogger.info(String.format("客户端[%s]>>>服务端IP[%s] 请求：[%s] ,响应：[%s] ，耗时:[%s ms]", RpcContext.getContext().getLocalHost(), RpcContext.getContext().getRemoteHost(), filterReq.toString(), filterRsp.toString(), System.currentTimeMillis()-startTime));
            } else {
                mpspLogger.info(String.format("客户端[%s]>>>服务端IP[%s] 请求：[%s] ,响应：[%s] ，耗时:[%s ms]", RpcContext.getContext().getRemoteHost(), RpcContext.getContext().getLocalHost(), filterReq.toString(), filterRsp.toString(), System.currentTimeMillis()-startTime));
            }
            return result;

        } catch (RuntimeException e) {
            logger.error("dubbo未知异常" + RpcContext.getContext().getRemoteHost() + ". service: "
                    + invoker.getInterface().getName() + ", method: " + invocation.getMethodName() + ", exception: "
                    + e.getClass().getName() + ": " + e.getMessage(), e);

            //打印简要日志
            if (RpcContext.getContext().isConsumerSide()) {
                mpspLogger.info(String.format("客户端[%s]>>>服务端IP[%s] 请求：[%s] ,响应：[%s] ，耗时:[%s ms]", RpcContext.getContext().getLocalHost(), RpcContext.getContext().getRemoteHost(), filterReq.toString(), e.getMessage(), System.currentTimeMillis()-startTime));
            } else {
                mpspLogger.info(String.format("客户端[%s]>>>服务端IP[%s] 请求：[%s] ,响应：[%s] ，耗时:[%s ms]", RpcContext.getContext().getRemoteHost(), RpcContext.getContext().getLocalHost(), filterReq.toString(), e.getMessage(), System.currentTimeMillis()-startTime));
            }
            throw e;
        }
        // <<<<<<<<<<<<<<<<<<<<<
    }

}
