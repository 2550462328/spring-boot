package cn.ictt.zhanghui.springboot_test.util.http;

import cn.ictt.zhanghui.springboot_test.domain.UserOperate;
import cn.ictt.zhanghui.springboot_test.service.UserTestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 获取当前请求的用户
 * @author ZhangHui
 * @date 2020/9/17
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CurrentUserUtils {

    private final UserTestService userService;

    public UserOperate getCurrentUser() {
        return userService.findByName(getCurrentUserName());
    }

    private static String getCurrentUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }
}
