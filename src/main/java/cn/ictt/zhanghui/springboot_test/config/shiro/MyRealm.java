//package cn.ictt.zhanghui.springboot_test.config.shiro;
//
//import cn.ictt.zhanghui.springboot_test.domain.Permission;
//import cn.ictt.zhanghui.springboot_test.domain.User_OpeareRepository;
//import cn.ictt.zhanghui.springboot_test.domain.User_Operate;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.shiro.authc.*;
//import org.apache.shiro.authz.AuthorizationInfo;
//import org.apache.shiro.authz.SimpleAuthorizationInfo;
//import org.apache.shiro.realm.AuthorizingRealm;
//import org.apache.shiro.subject.PrincipalCollection;
//import org.apache.shiro.util.ByteSource;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
///**
// * shiro的自定义认证和授权
// * @author ZhangHui
// * @date 2020/9/11
// */
//@Slf4j
//public class MyRealm extends AuthorizingRealm {
//
//    @Autowired
//    private User_OpeareRepository userService;
//
//    @Override
//    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        log.info("登录认证");
//        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
//        User_Operate user = userService.findByName(token.getUsername());
//        if(user == null) {
//            throw new UnknownAccountException(); // 没找到帐号
//        }
//        /*if(Boolean.TRUE.equals(user.getLocked())) {
//            throw new LockedAccountException(); //帐号锁定
//        }*/
//        //第一个参数：用户名/用户对象
//        String username =token.getUsername();
//        //第二个参数：用户的密码
//        String password = user.getPassword();
//        //第三个参数：盐值(这个盐是 username)
//        ByteSource solt = ByteSource.Util.bytes(username);
//        //第四个参数：获取这个Realm的信息
//        String realmName =this.getName();
//        //交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以在此判断或自定义实现
//        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(username, password, solt, realmName);
//        return authenticationInfo;
//    }
//
//    @Override
//    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        log.info("授权");
//        String username = (String) principals.getPrimaryPrincipal();
//        SimpleAuthorizationInfo authorizationInfo = null;
//        try {
//            authorizationInfo = new SimpleAuthorizationInfo();
//            User_Operate user = userService.findByName(username);
//            authorizationInfo.addRole(user.getRole().getCode());
//            List<Permission> list = user.getRole().getPermissionList();
//            for(Permission p:list){
//                authorizationInfo.addStringPermission(p.getCode());
//            }
//        } catch (Exception e) {
//            log.error("授权错误{}", e.getMessage());
//            e.printStackTrace();
//        }
//        return authorizationInfo;
//    }
//}
