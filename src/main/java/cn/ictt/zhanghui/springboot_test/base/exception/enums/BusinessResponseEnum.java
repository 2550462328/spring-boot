package cn.ictt.zhanghui.springboot_test.base.exception.enums;

import cn.ictt.zhanghui.springboot_test.base.exception.assertion.BusinessExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回结果
 *
 * @author sprainkle
 * @date 2021/4/26
 */
@Getter
@AllArgsConstructor
public enum BusinessResponseEnum implements BusinessExceptionAssert {

    INVAID_GOOD_NUMBER("3000","无效的商品编号！"),
    LOGIN_FAIL_EXCEPTION("4000","登录失败！请检查用户名和密码。"),
    TEMPLATE_IS_EXISTS("5000","{}在{}下已存在！")

    ;
    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;
}
