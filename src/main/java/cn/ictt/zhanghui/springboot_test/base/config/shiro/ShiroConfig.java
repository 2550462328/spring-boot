//package cn.ictt.zhanghui.springboot_test.config.shiro;
//
//import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
//import org.apache.shiro.mgt.SecurityManager;
//import org.apache.shiro.mgt.SessionsSecurityManager;
//import org.apache.shiro.realm.Realm;
//import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
//import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
//import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
//import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
//import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
//import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
//import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
//
//import javax.servlet.Filter;
//import java.util.LinkedHashMap;
//import java.util.Map;
//import java.util.Properties;
//
///**
// * shiro配置类
// * @author ZhangHui
// * @date 2020/9/10
// */
////@Configuration
//public class ShiroConfig {
//
//    /**
//     * 注入自定义的realm，告诉shiro如何获取用户信息来做登录认证和授权
//     */
//    @Bean
//    public Realm realm() {
//        MyRealm myRealm = new MyRealm();
//        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
//        return myRealm;
//    }
//    @Bean
//    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager) {
//        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
//        // 必须设置SecuritManager
//        shiroFilterFactoryBean.setSecurityManager(securityManager);
//        Map<String, Filter> filters = shiroFilterFactoryBean.getFilters();
//        //配置拦截器,实现无权限返回401,而不是跳转到登录页
//        /*filters.put("authc", new FormLoginFilter());*/
//        // 如果不设置默认会自动寻找Web工程根目录下的"/login.html"页面
//        shiroFilterFactoryBean.setLoginUrl("/login.shtml");
//        // 登录成功后要跳转的链接
//        shiroFilterFactoryBean.setSuccessUrl("/index.shtml");
//        // 未授权界面;
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
//        // 拦截器
//        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
//        // 过滤链定义，从上向下顺序执行，一般将 /**放在最为下边
//        // authc:所有url都必须认证通过才可以访问; anon:所有url都都可以匿名访问
////        filterChainDefinitionMap.put("/login/**", "anon");
////        filterChainDefinitionMap.put("/sendQueue", "anon");
////        filterChainDefinitionMap.put("/sendTopic", "anon");
////        filterChainDefinitionMap.put("/swagger-ui.html", "anon");
////        filterChainDefinitionMap.put("/swagger-resources", "anon");
////        filterChainDefinitionMap.put("/swagger-resources/configuration/security", "anon");
////        filterChainDefinitionMap.put("/swagger-resources/configuration/ui", "anon");
////        filterChainDefinitionMap.put("/v2/api-docs", "anon");
////        filterChainDefinitionMap.put("/webjars/springfox-swagger-ui/**", "anon");
////        //除了以上的请求外，其它请求都需要登录
////        filterChainDefinitionMap.put("/**", "authc");
//        filterChainDefinitionMap.put("/**", "anon");
//        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
//        return shiroFilterFactoryBean;
//    }
//    /**
//     * 这里统一做鉴权，即判断哪些请求路径需要用户登录，哪些请求路径不需要用户登录。
//     * 这里只做鉴权，不做权限控制，因为权限用注解来做。
//     * @return
//     */
//    @Bean
//    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
//        DefaultShiroFilterChainDefinition chain = new DefaultShiroFilterChainDefinition();
//        //不需要在此处配置权限页面,因为上面的ShiroFilterFactoryBean已经配置过,
//        // 但是此处必须存在,因为shiro-spring-boot-web-starter或查找此Bean,没有会报错
//        return chain;
//    }
//
//    @Bean
//    public static DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
//        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
//        /**
//         * setUsePrefix(false)用于解决一个奇怪的bug。在引入spring aop的情况下。
//         * 在@Controller注解的类的方法中加入@RequiresRole注解，会导致该方法无法映射请求，导致返回404。
//         * 加入这项配置能解决这个bug
//         */
//        creator.setUsePrefix(true);
//        return creator;
//    }
//    /**
//     * 凭证匹配器
//     * （由于我们的密码校验交给Shiro的SimpleAuthenticationInfo进行处理了）
//     * @return
//     */
//    @Bean
//    public HashedCredentialsMatcher hashedCredentialsMatcher(){
//        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
//        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
//        hashedCredentialsMatcher.setHashIterations(1);//散列的次数，比如散列两次，相当于 md5(md5(""));
//        return hashedCredentialsMatcher;
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public SessionsSecurityManager securityManager(){
//        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
//        securityManager.setRealm(realm());
//        return securityManager;
//    }
//
//    /**
//     *  开启shiro aop注解支持.
//     *  使用代理方式;所以需要开启代码支持;
//     * @param securityManager
//     * @return
//     */
//    @Bean
//    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
//        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
//        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
//        return authorizationAttributeSourceAdvisor;
//    }
//
//    @Bean
//    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver r = new SimpleMappingExceptionResolver();
//        Properties mappings = new Properties();
//        mappings.setProperty("DatabaseException", "databaseError");//数据库异常处理
//        mappings.setProperty("UnauthorizedException","/user/403");
//        r.setExceptionMappings(mappings);  // None by default
//        r.setDefaultErrorView("error");    // No default
//        r.setExceptionAttribute("exception");     // Default is "exception"
//        //r.setWarnLogCategory("example.MvcLogger");     // No default
//        return r;
//    }
//
//}