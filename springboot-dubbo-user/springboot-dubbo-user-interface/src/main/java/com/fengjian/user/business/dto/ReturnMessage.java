/*
 * FileName: ReturnMessage
 * Author:   fengjian
 * Date:     2019-01-07 15:29
 * Description: 返回消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.user.business.dto;


import com.fengjian.user.dict.ConstEC;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 〈一句话功能简述〉<br> 
 * 〈返回消息〉
 *
 * @author fengjian
 * @create 2019-01-07
 * @since 1.0.0
 */
@Getter
@Setter
public class ReturnMessage  implements Serializable {
    /**
     *  响应码
     */
    String retCode;
    /**
     * 响应信息描述
     */
    String retMessage;
    /**
     * 响应数据
     */
    Object retData;

    public  ReturnMessage(){

    }
    public  ReturnMessage(String retCode){
        this.retCode = retCode;
        if (ConstEC.SUCCESS.equalsIgnoreCase(retCode)){
            this.retMessage = "Success";
        }else {
            this.retMessage = "Fail";
        }
    }

    public  ReturnMessage(String retCode,String retMessage){
        this.retCode = retCode;
        this.retMessage = retMessage;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ReturnMessage{");
        sb.append("retCode=").append(retCode == null ? "null" : retCode.toString());
        sb.append(", retMessage=").append(retMessage == null ? "null" : retMessage.toString());
        sb.append(", retData=").append(retData == null ? "null" : retData.toString());
        sb.append('}');
        return sb.toString();
    }
}
