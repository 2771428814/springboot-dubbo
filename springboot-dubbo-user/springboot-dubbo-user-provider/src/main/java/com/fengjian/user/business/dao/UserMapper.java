/*
 * FileName: UserMapper
 * Author:   fengjian
 * Date:     2020-07-06 15:50
 * Description: 用户
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.business.dao;

import com.fengjian.user.business.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 〈用户〉
 *
 * @author fengjian
 * @create 2020-07-06
 * @since 1.0.0
 */
@Mapper
public interface  UserMapper {
    /**
     * 根据用户编号查询用户信息
     * @param userNo
     * @return
     */
    User queryByuserNo(String userNo);
}
