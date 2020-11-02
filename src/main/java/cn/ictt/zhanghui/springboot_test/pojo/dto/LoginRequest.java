package cn.ictt.zhanghui.springboot_test.pojo.dto;

import lombok.Data;


/**
 * 用户登录请求DTO
 * @author ZhangHui
 * @date 2020/9/17
 */
@Data
public class LoginRequest {
    private String username;
    private String password;
    private Boolean rememberMe;
}
