/*
 * FileName: User
 * Author:   fengjian
 * Date:     2020-07-06 15:58
 * Description: 用户信息表
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.business.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 〈用户信息表〉
 *
 * @author fengjian
 * @create 2020-07-06
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class User {
   private String user_id;
   private String user_name;
}
