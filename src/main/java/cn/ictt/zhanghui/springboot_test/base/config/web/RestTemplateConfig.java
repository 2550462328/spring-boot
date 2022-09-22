package cn.ictt.zhanghui.springboot_test.base.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    private static final Integer DEFAULT_TIME_OUT_MILLS = 60000;

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory simpleClientHttpRequestFactory = new SimpleClientHttpRequestFactory();
        simpleClientHttpRequestFactory.setConnectTimeout(DEFAULT_TIME_OUT_MILLS);
        simpleClientHttpRequestFactory.setReadTimeout(DEFAULT_TIME_OUT_MILLS);
        return new RestTemplate(simpleClientHttpRequestFactory);
    }

    /**
     * 密码编码器
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}