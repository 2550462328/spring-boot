package cn.ictt.zhanghui.springboot_test.business.web;

import cn.ictt.zhanghui.springboot_test.base.resp.ResponseInfo;
import cn.ictt.zhanghui.springboot_test.base.util.http.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IndexController {

    @Value("${zhanghui.springboot.test.content}")
    private String content;

    private final CurrentUserUtils currentUserUtils;

    @GetMapping("/{url}.htm")
    public String hello(@PathVariable("url") String url) {
        return url;
    }

    @GetMapping("/errorInfo")
    public void errorInfo() throws Exception {
        throw new Exception("抛出自定义异常");
        /* System.out.println(1/0);*/
    }

    /**
     * 基于shiro的登录验证
     *
     * @param userName
     * @param userPassword
     * @return org.springframework.web.servlet.ModelAndView
     * @author ZhangHui
     * @date 2020/9/11
     */
//    @RequestMapping("/login")
//    public ModelAndView login(String userName, String userPassword){
//        ModelAndView mv = new ModelAndView();
//        log.info(userName+":"+userPassword);
//        if (StringUtils.isEmpty(userName)){
//            mv.setViewName("login");
//            mv.addObject("errorInfo", "NotEmpty UserName!");
//            return mv;
//        }
//        if (StringUtils.isEmpty(userPassword)){
//            mv.setViewName("login");
//            mv.addObject("errorInfo", "NotEmpty UserPassword!");
//            return mv;
//        }
//        Subject currentUser = SecurityUtils.getSubject();
//        try {
//            //登录  admin/admin
//            currentUser.login( new UsernamePasswordToken(userName, userPassword) );
//            //向session中存储用户信息
//            //返回登录用户的信息给前台，含用户的所有角色和权限
//            mv.setViewName("login_success");
//            return mv;
//        } catch ( UnknownAccountException uae ) {
//            log.warn("用户帐号不正确");
//        } catch ( IncorrectCredentialsException ice ) {
//            log.warn("用户密码不正确");
//        } catch ( LockedAccountException lae ) {
//            log.warn("用户帐号被锁定");
//        } catch ( AuthenticationException ae ) {
//            log.warn("登录出错");
//        }
//        mv.setViewName("login");
//        mv.addObject("errorInfo", "RunTimeException!");
//        return mv;
//    }

    @PostMapping("/auth/login")
    public ResponseInfo getUser(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("rememberMe") String rememberMe) {
        return ResponseInfo.OK();
    }
}
