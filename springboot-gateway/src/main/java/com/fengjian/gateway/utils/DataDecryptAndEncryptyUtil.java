package com.fengjian.gateway.utils;



import com.fengjian.utils.JsonUtil;
import com.fengjian.utils.json.JSONArray;
import com.fengjian.utils.json.JSONException;
import com.fengjian.utils.json.JSONObject;
import com.fengjian.utils.AesEncryptUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据加密解密处理
 * @author fengjian
 *
 */
public class DataDecryptAndEncryptyUtil {
	private static final Logger logger = LoggerFactory.getLogger(DataDecryptAndEncryptyUtil.class);
	


	

	
	/**
	 * son型字符串 数据加解密
	 * @param jsonString 待处理字符串
	 * @param decryptAndEncryptKeys  关键字
	 * @param cryptoType  1 解密  2 加密
	 * @return  Success:+解密后json串
	 * 			Fail:+ 原因
	 */
	public static String decryptAndEncryptyJsonObject(String jsonString,String decryptAndEncryptKeys,int cryptoType) {
		
		try {
			//判断json的类型1：是JsonObject 2 ：是JsonArray  
			int i = JsonUtil.jsonTypeCheck(jsonString);
			if(i == 0){
				//判断json的类型都不是的情况直接报错
				
				return "Fail: data is not json type"; 
			}else if( i == 1){  
				//JsonObject的处理
				JSONObject jsonObject = new  JSONObject(jsonString);
				
				String[]   names = JSONObject.getNames(jsonObject);
				if (names==null) {
					logger.info("【数据加解密处理】待验证数据【{}】为空，验证通过", jsonString);
					return String.format("Success:%s",jsonString); 
				}
				for (String name : names) {
					String value = jsonObject.get(name).toString();

					if (JsonUtil.jsonTypeCheck(value) == 0) {
						if (("," + decryptAndEncryptKeys + ",").contains("," + name + ",")) {
							// 当前名称在解密字段中，解密数据
							logger.info("【数据加解密处理】【{}】数据需要加解密", name);
							try {
								if (cryptoType == 1) {
									value = AesEncryptUtil.decryptHex(value, null);
								} else {
									value = AesEncryptUtil.encryptHex(value, null);
								}
								// 判断AES加密是否成功
								if (value ==null ){
									logger.info("【数据加解密处理】【{}】数据加解密失败", name);
									return String.format("Fail: %s decrypt or encrypt error ", name);
								}
							} catch (Exception e) {
								logger.info("【数据加解密处理】【{}】数据需要加解密异常", name);

								return String.format("Fail: %s decrypt or encrypt error ", name);
							}

						}
						jsonObject.put(name, value);

					} else {
						//递归
						String jsonValue = decryptAndEncryptyJsonObject(value, decryptAndEncryptKeys, cryptoType);
						if (jsonValue.startsWith("Success:")) {
							String replaceFirst = jsonValue.replaceFirst("Success:", "");
							if (JsonUtil.jsonTypeCheck(replaceFirst)==1){
								jsonObject.put(name, new JSONObject(replaceFirst));
							}else {
								jsonObject.put(name, new JSONArray(replaceFirst));
							}

						} else {
							return jsonValue;
						}
					}

				}
				
				return String.format("Success:%s",jsonObject);
				
			}else if( i == 2){  
				//JsonArray  的处理
				JSONArray jsonArrays = new JSONArray(jsonString);
				for(int k =0;k<jsonArrays.length();k++){
					String  arrayValue = jsonArrays.get(k).toString();
					String  jsonValue= decryptAndEncryptyJsonObject(arrayValue,decryptAndEncryptKeys,cryptoType);
					if (jsonValue.startsWith("Success:")){
						String replaceFirst = jsonValue.replaceFirst("Success:", "");
						if (JsonUtil.jsonTypeCheck(replaceFirst)==1){
							jsonArrays.put(k, new JSONObject(replaceFirst));
						}else {
							jsonArrays.put(k, new JSONArray(replaceFirst));
						}

					}else {
						return  jsonValue;
					}
				}
				return String.format("Success:%s",jsonArrays);
			}else {
				//判断json的类型都不是的情况直接报错
				return String.format("Fail: data is not json type ");
			}
		} catch (JSONException e) {
			//判断json的类型都不是的情况直接报错
			return String.format("Fail: data is not json type ");
		}catch (Exception e) {
			return String.format("Fail: data  encrypt or decrypt error ");
		}
		
	}
	
	public static void main(String[] args) {
		String  aa = "{"
				+ "\"aaa\":\"1\","
				+ "\"phone\":\"5AAFC7E43899A79A9D1476A488264E5F\","
				+ "\"jsonobject\":{"
				+ "              \"aaa\":\"5AAFC7E43899A79A9D1476A488264E5F\","
				+ "              \"jsarray\":["
				+ "						{\"aaa\":\"5AAFC7E43899A79A9D1476A488264E5F\"},"
				+ "						{\"aaarw\":\"aaarw\"} "
				+ "				   ]"
				+ "         },"
				+ "\"jsonarray\":[{"
				+ "              \"aaa\":\"5AAFC7E43899A79A9D1476A488264E5F\","
				+ "              \"phone1\":{"
				+ "              	\"aaa\":\"5AAFC7E43899A79A9D1476A488264E5F\","
				+ "              	\"phone0\":\"aaarw\" "
				+ "         	} "
				+ "         },"
				+ "			{"
				+ "              \"aaa2\":\"333a\","
				+ "              \"phone2\":\"aaarw\" "
				+ "         }]"
				+ "}";
		System.out.println(aa);
		System.out.print(decryptAndEncryptyJsonObject(aa,"aaa",1));
		//System.out.print(cryptoJsonObject(aa).get(NameDict.JSONOBJECT));
	}
	
}
