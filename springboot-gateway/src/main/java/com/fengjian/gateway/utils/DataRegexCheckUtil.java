package com.fengjian.gateway.utils;

import com.fengjian.utils.JsonUtil;
import com.fengjian.utils.json.JSONArray;
import com.fengjian.utils.json.JSONException;
import com.fengjian.utils.json.JSONObject;
import com.fengjian.utils.PropertiesUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.PatternSyntaxException;

/**
 * 商户请求参数正则校验
 * @author fengjian
 *
 */
public class DataRegexCheckUtil {
	private static final Logger logger = LoggerFactory.getLogger(DataRegexCheckUtil.class);
	/**
	 * 正则校验json型字符串
	 * @param jsonString
	 * @return
	 */
	public static String validJsonObject(String jsonString) {

		try {
			//判断json的类型1：是JsonObject 2 ：是JsonArray  
			int i = JsonUtil.jsonTypeCheck(jsonString);
			if(i == 0){
				//判断json的类型都不是的情况直接报错
				return "Fail: data is not json type";
			}else if( i == 1){  
				//JsonObject的处理
				JSONObject	jsonObject = new JSONObject(jsonString);
				String[]   names = JSONObject.getNames(jsonObject);
				if (names==null) {
					logger.info(String.format("【接口正则校验】待验证数据【%s】为空，验证通过", jsonString));
					return String.format("Success:%s",jsonString);
				}
				for (String name : names) {
					String value = jsonObject.get(name).toString();
					if (JsonUtil.jsonTypeCheck(value)==0) {
						String regexRule =  PropertiesUtil.getProperty(String.format("data.regex.%s", name));
						if (regexRule==null || regexRule.length()<=0) {
							logger.info(String.format("【接口正则校验】【%s】对应的正则【未配置】匹配验证【%s】的结果【true】", name,value));
							continue;
						}else {
							boolean regexResult = value.matches(regexRule);
							logger.info(String.format("【接口正则校验】【%s】对应的正则【%s】匹配验证【%s】的结果【%s】", name,regexRule,value,regexResult));
							if (regexResult) {
								continue;
							}else {
								return String.format("Fail:%s not match %s",value,regexRule);
							}
							
						}
					}else {
						//递归
						String  regexResult = validJsonObject(value);
						if (!regexResult.startsWith("Success:")) {
							return regexResult;
						}
					}
				}
				return String.format("Success:%s"," all JsonObject regex success ");
				
			}else if( i == 2){  
				//JsonArray  的处理
				
				
				JSONArray arrays = new JSONArray(jsonString);
				for(int k =0;k<arrays.length();k++){ 
					String  arrayValue = arrays.get(k).toString();
					String regexResult = validJsonObject(arrayValue);
					if (!regexResult.startsWith("Success:")) {
						return regexResult;
					}
				}
				return String.format("Success:%s"," all JsonArray regex success ");
			}else {
				//判断json的类型都不是的情况直接报错
				return "Fail: data is not json type";
			}
		} catch (JSONException e) {
			//判断json的类型都不是的情况直接报错
			return "Fail: data is not json type";
		}catch (PatternSyntaxException e) {
			//判断json的类型都不是的情况直接报错
			return "Fail: data regex  error";
		}
		
	}
	
	public static void main(String[] args) {
		String  aa = "{"
				+ "\"aaa\":\"123\","
				+ "\"phone\":\"aaa\","
				+ "\"jsonobject\":{"
				+ "              \"aaa\":\"44444444skdfj\","
				+ "              \"phone0\":\"aaarw\" "
				+ "         },"
				+ "\"jsonarray\":[{"
				+ "              \"aaa1\":\"333\","
				+ "              \"phone1\":\"aaarw\" "
				+ "         },"
				+ "			{"
				+ "              \"aaa2\":\"333a\","
				+ "              \"phone2\":\"aaarw\" "
				+ "         }]"
				+ "}";
		System.out.print(validJsonObject(aa));
	}
}
