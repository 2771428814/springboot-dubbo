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
import com.fengjian.user.business.dto.ReturnMessage;
import com.fengjian.user.business.dto.UserDto;
import com.fengjian.user.business.dubboservice.UserLoginService;
import com.fengjian.user.utils.ReturnMessageUtil;

/**
 * 〈用户登录服务实现〉
 *
 * @author fengjian
 * @create 2020-07-06
 * @since 1.0.0
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {
    @Override
    public ReturnMessage userLogin(UserDto userDto) {
        return ReturnMessageUtil.success();
    }
}
