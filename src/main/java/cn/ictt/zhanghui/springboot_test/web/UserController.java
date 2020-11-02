package cn.ictt.zhanghui.springboot_test.web;

import cn.ictt.zhanghui.springboot_test.domain.UserOperate;
import cn.ictt.zhanghui.springboot_test.pojo.dto.ResponseInfo;
import cn.ictt.zhanghui.springboot_test.service.UserTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author ZhangHui
 * @version 1.0
 * @className UserController
 * @description 基于User_test的Controller入口
 * @date 2020/9/17
 */
@RestController
@RequestMapping("/users")
@Validated
public class UserController {

    @Autowired
    private UserTestService userTestService;

    @GetMapping("/addUser")
    public String addUser(){
        return userTestService.addUser();
    }

    @PostMapping("/registerUser")
    public ResponseInfo registerUser(@RequestBody @Valid UserOperate userOperate) {
        userTestService.saveUser(userOperate);
        return ResponseInfo.OK();
    }

    @PostMapping("/auth/getUser/{name}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    public ResponseInfo getUser(@PathVariable("name") @Valid @NotNull String name) {
        UserOperate userOperate= userTestService.findByName(name);
        return new ResponseInfo(userOperate);
    }
}
