/*
 * FileName: UserDto
 * Author:   fengjian
 * Date:     2020-07-06 10:49
 * Description: 用户信息类
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.business.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 〈用户信息类〉
 *
 * @author fengjian
 * @create 2020-07-06
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class UserDto  implements Serializable {
    /**
     * 用户编号
     */
    private String userNo;
    /**
     * 用户密码
     */
    private String password;
}
