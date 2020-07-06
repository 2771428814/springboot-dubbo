#项目说明

## 一：采用过滤器加拦截器的方式，实现日志打印功能，签名及验签功能，数据必输校验，数据加解密，数据正则校验。GatewayAdapterConfigurer为配置拦截器执行顺序等。
- 仅支持post请求报文体为json格式的数据
- 日志打印采用过滤器模式，并在过滤器中完成拦截器所需的请求及相应的重写
- 签名及验签功能采用MD5模式，仅对post请求进行验签，所有响应进行签名。MD5使用的原串为requestBody+key，其中key在拦截器中写死。
- 必输校验功能通过controller配置的requestMapping的name及data-must-check.properties完成。配置文件的key模版data.must.xxx。前半部分'data.must.'为固定值，后半部分xxx为requestMapping的name值。配置文件的value值若被中括号标记则为非必填，负责为必填。
- 数据加解密功能采用AES加解密，对配置的实际需要加解密的key进行处理，根name在配置值中时进行加密解密处理。配置项在application.yml中的data.decryptAndEncryptKeys。
- 数据正则校验根据根name值及配置文件中的正则进行校验。data-regex-check.properties说明，key模式为data.regex.xxx，前半部分'data.regex.'为固定值，后半部分xxx为根的name值。
## 二：自定义配置文件采用监听方式读取到内存，通过工具类获取
## 三：日志脱敏处理，脱敏关键字段在application.yml中的log4jConfig.encodeCharKeys。

#日志例子
##   1.post验签必输正则通过日志例子 
```java
2019-12-04 16:34:04,530 INFO  [GW163404a3afe05]LogFilter:140 -Proxy-Client-IP:[null],WL-Proxy-Client-IP:[null],X-Real-IP:[null],remote:[127.0.0.1]
2019-12-04 16:34:04,531 INFO  [GW163404a3afe05]LogFilter:41 -【  IP  】: 10.10.85.79
2019-12-04 16:34:04,531 INFO  [GW163404a3afe05]LogFilter:42 -【  UA  】: PostmanRuntime/6.3.2
2019-12-04 16:34:04,532 INFO  [GW163404a3afe05]LogFilter:44 -【  URL 】: http://127.0.0.1:9090/gateway/demo/fengjian/post3
2019-12-04 16:34:04,532 INFO  [GW163404a3afe05]LogFilter:45 -【METHOD】: POST
2019-12-04 16:34:04,533 INFO  [GW163404a3afe05]LogFilter:74 -【PARAMS】: {"aa":[{"bb":"ccc","cc":"eee"},{"bb":"vvv","cc":"ddd"}],"bbb":"5AAFC7E43899A79A9D1476A488264E5F"}
2019-12-04 16:34:04,547 INFO  [GW163404a3afe05]SignInterceptor:63 -验签结果：成功
2019-12-04 16:34:04,549 INFO  [GW163404a3afe05]DataMustCheckInterceptor:67 -必输规则data.must.demoPost3=aa.bb,aa.cc
2019-12-04 16:34:04,551 INFO  [GW163404a3afe05]DataMustCheckInterceptor:72 -必输校验通过
2019-12-04 16:34:04,551 INFO  [GW163404a3afe05]DataDecryptAndEncryptInterceptor:49 -decryptAndEncryptKeys=name,idNo,bbb
2019-12-04 16:34:04,554 INFO  [GW163404a3afe05]DataDecryptAndEncryptyUtil:57 -【数据加解密处理】【bbb】数据需要加解密
2019-12-04 16:34:04,559 INFO  [GW163404a3afe05]DataDecryptAndEncryptInterceptor:54 -数据解密完成
2019-12-04 16:34:04,560 INFO  [GW163404a3afe05]DataRegexCheckUtil:46 -【接口正则校验】【bb】对应的正则【未配置】匹配验证【ccc】的结果【true】
2019-12-04 16:34:04,561 INFO  [GW163404a3afe05]DataRegexCheckUtil:46 -【接口正则校验】【cc】对应的正则【未配置】匹配验证【eee】的结果【true】
2019-12-04 16:34:04,562 INFO  [GW163404a3afe05]DataRegexCheckUtil:46 -【接口正则校验】【bb】对应的正则【未配置】匹配验证【vvv】的结果【true】
2019-12-04 16:34:04,562 INFO  [GW163404a3afe05]DataRegexCheckUtil:46 -【接口正则校验】【cc】对应的正则【未配置】匹配验证【ddd】的结果【true】
2019-12-04 16:34:04,564 INFO  [GW163404a3afe05]DataRegexCheckUtil:50 -【接口正则校验】【bbb】对应的正则【(?s).{1,256}】匹配验证【1】的结果【true】
2019-12-04 16:34:04,565 INFO  [GW163404a3afe05]DataRegexCheckInterceptor:45 -数据正则校验通过
2019-12-04 16:34:04,643 INFO  [GW163404a3afe05]DemoController:49 -请求：name=fengjian
2019-12-04 16:34:04,644 INFO  [GW163404a3afe05]DemoController:50 -请求：body={"aa":[{"bb":"ccc","cc":"eee"},{"bb":"vvv","cc":"ddd"}],"bbb":"1"}
2019-12-04 16:34:04,644 INFO  [GW163404a3afe05]DemoController:51 -retCode=0****,retMsg=请***
2019-12-04 16:34:04,663 INFO  [GW163404a3afe05]DataDecryptAndEncryptyUtil:57 -【数据加解密处理】【bbb】数据需要加解密
2019-12-04 16:34:04,664 INFO  [GW163404a3afe05]DataDecryptAndEncryptInterceptor:86 -数据加密完成
2019-12-04 16:34:04,664 INFO  [GW163404a3afe05]SignInterceptor:83 -添加签名成功
2019-12-04 16:34:04,667 INFO  [GW163404a3afe05]LogFilter:81 -【RETURN】: {"aaa":"111","bbb":"C23592C2FB616B78A4A9F31CFBF6C832"}
2019-12-04 16:34:04,667 INFO  [GW163404a3afe05]LogFilter:82 -【 TIME 】: 141 ms
```
## 2.post解密处理失败例子
```java
2019-12-04 16:34:20,866 INFO  [GW16342028cdce7]LogFilter:140 -Proxy-Client-IP:[null],WL-Proxy-Client-IP:[null],X-Real-IP:[null],remote:[127.0.0.1]
2019-12-04 16:34:20,866 INFO  [GW16342028cdce7]LogFilter:41 -【  IP  】: 10.10.85.79
2019-12-04 16:34:20,867 INFO  [GW16342028cdce7]LogFilter:42 -【  UA  】: PostmanRuntime/6.3.2
2019-12-04 16:34:20,867 INFO  [GW16342028cdce7]LogFilter:44 -【  URL 】: http://127.0.0.1:9090/gateway/demo/post2
2019-12-04 16:34:20,868 INFO  [GW16342028cdce7]LogFilter:45 -【METHOD】: POST
2019-12-04 16:34:20,868 INFO  [GW16342028cdce7]LogFilter:74 -【PARAMS】: {"aa":"aaa","bbb":"bbb"}
2019-12-04 16:34:20,870 INFO  [GW16342028cdce7]DataMustCheckInterceptor:60 -public java.lang.String com.fengjian.gateway.controller.DemoController.post2(java.lang.String) 注解RequestMapping中的name未设置，不进行数据必输校验。
2019-12-04 16:34:20,870 INFO  [GW16342028cdce7]DataDecryptAndEncryptInterceptor:49 -decryptAndEncryptKeys=name,idNo,bbb
2019-12-04 16:34:20,871 INFO  [GW16342028cdce7]DataDecryptAndEncryptyUtil:57 -【数据加解密处理】【bbb】数据需要加解密
2019-12-04 16:34:20,876 INFO  [GW16342028cdce7]DataDecryptAndEncryptyUtil:66 -【数据加解密处理】【bbb】数据加解密失败
2019-12-04 16:34:20,877 INFO  [GW16342028cdce7]DataDecryptAndEncryptInterceptor:57 -数据解密失败。Fail: bbb decrypt or encrypt error
2019-12-04 16:34:20,878 INFO  [GW16342028cdce7]LogFilter:81 -【RETURN】: Fail: bbb decrypt or encrypt error
2019-12-04 16:34:20,878 INFO  [GW16342028cdce7]LogFilter:82 -【 TIME 】: 13 ms

```
## 3.post验签失败例子
```java
2019-12-04 16:34:31,663 INFO  [GW163431298128d]LogFilter:140 -Proxy-Client-IP:[null],WL-Proxy-Client-IP:[null],X-Real-IP:[null],remote:[127.0.0.1]
2019-12-04 16:34:31,663 INFO  [GW163431298128d]LogFilter:41 -【  IP  】: 10.10.85.79
2019-12-04 16:34:31,663 INFO  [GW163431298128d]LogFilter:42 -【  UA  】: PostmanRuntime/6.3.2
2019-12-04 16:34:31,664 INFO  [GW163431298128d]LogFilter:44 -【  URL 】: http://127.0.0.1:9090/gateway/demo/post
2019-12-04 16:34:31,664 INFO  [GW163431298128d]LogFilter:45 -【METHOD】: POST
2019-12-04 16:34:31,665 INFO  [GW163431298128d]LogFilter:74 -【PARAMS】: {"aa":"aaa","bbb":"bbb"}
2019-12-04 16:34:31,666 INFO  [GW163431298128d]SignInterceptor:49 -验签结果：失败,请求signature：null，计算signature：2F1E445FD47CB934000D1F24204BD3A7
2019-12-04 16:34:31,667 INFO  [GW163431298128d]LogFilter:81 -【RETURN】: Fail: signature verification failed
2019-12-04 16:34:31,667 INFO  [GW163431298128d]LogFilter:82 -【 TIME 】: 5 ms
```
## 4.get处理请求例子
```java

2019-12-04 16:34:45,583 INFO  [GW163445f94f8f1]LogFilter:140 -Proxy-Client-IP:[null],WL-Proxy-Client-IP:[null],X-Real-IP:[null],remote:[127.0.0.1]
2019-12-04 16:34:45,583 INFO  [GW163445f94f8f1]LogFilter:41 -【  IP  】: 10.10.85.79
2019-12-04 16:34:45,583 INFO  [GW163445f94f8f1]LogFilter:42 -【  UA  】: PostmanRuntime/6.3.2
2019-12-04 16:34:45,584 INFO  [GW163445f94f8f1]LogFilter:44 -【  URL 】: http://127.0.0.1:9090/gateway/demo/get
2019-12-04 16:34:45,584 INFO  [GW163445f94f8f1]LogFilter:45 -【METHOD】: GET
2019-12-04 16:34:45,596 INFO  [GW163445f94f8f1]LogFilter:74 -【PARAMS】: {"name":["6"]}
2019-12-04 16:34:45,603 INFO  [GW163445f94f8f1]DemoController:31 -请求：name=6
2019-12-04 16:34:45,603 INFO  [GW163445f94f8f1]DemoController:32 -retCode=0****,retMsg=请***
2019-12-04 16:34:45,605 INFO  [GW163445f94f8f1]DataDecryptAndEncryptInterceptor:86 -数据加密完成
2019-12-04 16:34:45,605 INFO  [GW163445f94f8f1]SignInterceptor:83 -添加签名成功
2019-12-04 16:34:45,606 INFO  [GW163445f94f8f1]LogFilter:81 -【RETURN】: {"aaa":"111"}
2019-12-04 16:34:45,606 INFO  [GW163445f94f8f1]LogFilter:82 -【 TIME 】: 24 ms
```
