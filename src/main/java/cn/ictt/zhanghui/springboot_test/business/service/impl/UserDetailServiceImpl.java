package cn.ictt.zhanghui.springboot_test.business.service.impl;

import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import cn.ictt.zhanghui.springboot_test.business.pojo.dto.JwtUser;
import cn.ictt.zhanghui.springboot_test.business.service.UserTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author ZhangHui
 * @version 1.0
 * @className UserDetailServiceImpl
 * @description SpringSecurity 配置帮助类，从name到UserDetails
 * @date 2020/9/17
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserTestService userTestService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserOperate userOperate = userTestService.findByName(s);

        return new JwtUser(userOperate);
    }
}
