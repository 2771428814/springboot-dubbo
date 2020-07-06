/*
 * FileName: UserLoginService
 * Author:   fengjian
 * Date:     2019-01-07 15:13
 * Description: 用户登录服务
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.business.dubboservice;

import com.fengjian.user.business.dto.*;

/**
 * 〈一句话功能简述〉<br> 
 * 〈用户登录服务〉
 *
 * @author fengjian
 * @create 2019-01-07
 * @since 1.0.0
 */
public interface UserLoginService {

    /**
     * 用户登录
     * @param userDto
     * @return
     */
    public ReturnMessage userLogin(UserDto userDto);

   }
