/*
 * FileName: UserLoginServiceImpl
 * Author:   fengjian
 * Date:     2020-07-06 11:22
 * Description: 用户登录服务实现
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.business.dubbosrervice.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fengjian.user.business.dao.UserMapper;
import com.fengjian.user.business.dto.ReturnMessage;
import com.fengjian.user.business.dto.UserDto;
import com.fengjian.user.business.dubboservice.UserLoginService;
import com.fengjian.user.business.pojo.User;
import com.fengjian.user.utils.ReturnMessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;

/**
 * 〈用户登录服务实现〉
 *
 * @author fengjian
 * @create 2020-07-06
 * @since 1.0.0
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
   private Logger logger = LoggerFactory.getLogger(UserLoginServiceImpl.class);

    @Resource
    private UserMapper userMapper;
    @Override
    public ReturnMessage userLogin(UserDto userDto) {
        logger.info("数据库查询结果：{}",userDto.toString());
        User user = userMapper.queryByuserNo(userDto.getUserNo());
        logger.info("数据库查询结果：{}",user==null?"":user.toString());
        return ReturnMessageUtil.success();
    }
}
