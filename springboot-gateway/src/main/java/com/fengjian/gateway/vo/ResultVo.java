/*
 * FileName: ResultVo
 * Author:   fengjian
 * Date:     2020-04-29 08:53
 * Description: 通用响应对象
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.gateway.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * 〈通用响应对象〉
 *
 * @author fengjian
 * @create 2020-04-29
 * @since 1.0.0
 */
@Getter
@Setter
@ToString
public class ResultVo  implements Serializable {
    private  String code;
    private  String message;
    private  String requestId;
    private  Object data;
   public ResultVo(){
        this.requestId=Thread.currentThread().getName();
    }
}
