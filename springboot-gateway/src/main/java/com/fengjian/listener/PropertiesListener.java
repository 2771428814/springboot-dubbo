/**
 * FileName: PropertiesListener
 * Author:   fengjian
 * Date:     2019-12-02 15:14
 * Description: 配置文件监听器，用来加载自定义配置文件
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.fengjian.listener;

import com.fengjian.utils.PropertiesUtil;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 〈一句话功能简述〉<br> 
 * 〈配置文件监听器，用来加载自定义配置文件〉
 *
 * @author fengjian
 * @create 2019-12-02
 * @since 1.0.0
 */
public class PropertiesListener implements ApplicationListener<ApplicationStartedEvent> {

    private String propertyFileName;

    public PropertiesListener(String propertyFileName) {
        this.propertyFileName = propertyFileName;
    }

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        PropertiesUtil.loadAllProperties(propertyFileName);
    }
}
