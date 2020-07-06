package com.fengjian.user.filter;
/**
 * FileName: DubboMessage
 * Author:   fengjian
 * Date:     2019-01-04 14:55
 * Description: DUBBO 消息
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
import java.util.Arrays;

/**
 * 〈一句话功能简述〉<br>
 * 〈DUBBO 消息〉
 *
 * @author fengjian
 * @create 2019-01-04
 * @since 1.0.0
 */
public class DubboMessage {

	private String interfaceName;// 接口名
	private String methodName;// 方法名
	private Object[] args;// 参数
	// 省略getter setter

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	@Override
	public String toString() {
		return "DubboMessage [interfaceName=" + interfaceName + ", methodName=" + methodName + ", args="
				+ Arrays.toString(args) + "]";
	}

}