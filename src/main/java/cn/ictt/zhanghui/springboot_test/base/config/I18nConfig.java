package cn.ictt.zhanghui.springboot_test.base.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author huizhang43
 * @description: i18n配置类
 */
//@Configuration
public class I18nConfig {
    
    @Value("${spring.messages.basename}")
    private String i18nPath;
    
    @Value("${spring.messages.encoding}")
    private String encoding;
    
    @Value("${spring.messages.cache-second}")
    private int cacheTime;
    
    @Bean(name = "messageSource")
    public ReloadableResourceBundleMessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageBundle = new ReloadableResourceBundleMessageSource();
        messageBundle.setBasename(i18nPath);
        messageBundle.setDefaultEncoding(encoding);
        messageBundle.setCacheSeconds(cacheTime);
        return messageBundle;
    }
}
