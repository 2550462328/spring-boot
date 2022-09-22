package cn.ictt.zhanghui.springboot_test.base.config.web;

import cn.ictt.zhanghui.springboot_test.base.filter.JwtAuthenticationFilter;
import cn.ictt.zhanghui.springboot_test.base.filter.JwtAuthorizationFilter;
import cn.ictt.zhanghui.springboot_test.base.handler.JwtAccessDeniedHandler;
import cn.ictt.zhanghui.springboot_test.base.handler.JwtAuthenticationEntryPoint;
import cn.ictt.zhanghui.springboot_test.business.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 基于SpringSecurity的安全配置
 * 加上@EnableGlobalMethodSecurity注释 @PreAuthorize才会生效
 * @author ZhangHui
 * @date 2020/9/10
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailServiceImpl userDetailsServiceImpl;

    /**
     * 验证数据来源
     * @author ZhangHui
     * @date 2020/9/17
     */
    @Bean
    public UserDetailsService getUserDetailsService() {
        return userDetailsServiceImpl;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 本地验证
//        auth
//                .inMemoryAuthentication()
//                //springboot5.0后需要对密码进行加密，推荐使用bcrypt加密
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .withUser("admin").password(bCryptPasswordEncoder().encode("admin")).roles("USER");
        //  数据库验证
                auth
                .userDetailsService(getUserDetailsService())
                //springboot5.0后需要对密码进行加密，推荐使用bcrypt加密
                .passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                .antMatchers("/", "/home").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login").permitAll()
//                .and()
//                .logout().permitAll();

        http.cors().and()
                // 禁用 CSRF
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/auth/login").permitAll()
                // 指定路径下的资源需要验证了的用户才能访问
                .antMatchers("/user/auth/**").authenticated()
                .antMatchers(HttpMethod.DELETE, "/user/auth/**").hasRole("ADMIN")
                // 其他都放行了
                .anyRequest().permitAll()
                .and()
                //添加自定义Filter
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsServiceImpl))
                // 不需要session（不创建会话）
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 授权异常处理
                .exceptionHandling().authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());
        // 防止H2 web 页面的Frame 被拦截
        http.headers().frameOptions().disable();
    }
}
