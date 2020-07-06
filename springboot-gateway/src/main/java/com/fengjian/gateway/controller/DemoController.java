/**
 * FileName: DemoController
 * Author:   fengjian
 * Date:     2019-11-28 10:50
 * Description: Demo
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.fengjian.dict.ConstEC;
import com.fengjian.gateway.utils.ResultUtil;
import com.fengjian.user.business.dto.ReturnMessage;
import com.fengjian.user.business.dto.UserDto;
import com.fengjian.user.business.dubboservice.UserLoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈Demo〉
 *
 * @author fengjian
 * @create 2019-11-28
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/demo")
public class DemoController {
    Logger logger = LoggerFactory.getLogger(DemoController.class);

    @Reference
    UserLoginService userLoginService;

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public String get(@RequestParam(value = "name") String name) {
        logger.info("请求：name={}", name);
        logger.info("retCode={},retMsg={}", "00000", "请求成功");
        UserDto userDto = new UserDto();
        userDto.setUserNo("1234");
        userDto.setPassword("password");
        ReturnMessage returnMessage = userLoginService.userLogin(userDto);
        if (com.fengjian.user.dict.ConstEC.SUCCESS.equalsIgnoreCase(returnMessage.getRetCode())) {
            return ResultUtil.success(returnMessage.getRetData());
        } else {
            return ResultUtil.fail(ConstEC.SYSTEM_ERROR);
        }
    }

    @RequestMapping(value = "/post", method = RequestMethod.POST)
    public String post(@RequestBody String body) {
        logger.info("请求：body={}", body);
        logger.info("retCode={},retMsg={}", "00000", "请求成功");
        Map data = new HashMap(1);
        data.put("retCode", "retCode");
        return ResultUtil.success(data);
    }

    @RequestMapping(value = "/post2", method = RequestMethod.POST)
    public String post2(@RequestBody String body) {
        logger.info("请求：body={}", body);
        logger.info("retCode={},retMsg={}", "00000", "请求成功");
        return ResultUtil.fail(ConstEC.FILE_NOT_EXIT);
    }

    @RequestMapping(value = "/{name}/post3", name = "demoPost3", method = RequestMethod.POST)
    public String post3(@PathVariable String name, @RequestBody String body) {
        logger.info("请求：name={}", name);
        logger.info("请求：body={}", body);
        logger.info("retCode={},retMsg={}", "00000", "请求成功");
        Map data = new HashMap(2);
        data.put("coke", "好吃不如饺子");
        data.put("bbb", "可口可乐");
        return ResultUtil.success(data);
    }
}
