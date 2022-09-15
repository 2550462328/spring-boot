package cn.ictt.zhanghui.springboot_test.base.exception.enums;

import cn.ictt.zhanghui.springboot_test.base.exception.assertion.ArgumentExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>参数校验异常返回结果</p>
 *
 * @author sprainkle
 * @date 2019/5/2
 */
@Getter
@AllArgsConstructor
public enum ArgumentResponseEnum implements ArgumentExceptionAssert {
    /**
     * 绑定参数校验异常
     */
    NOT_NULL("1000", "请求参数{}不能为空"),

    NOT_CORRECT("1001","请求参数{}类型不正确"),

    VALID_ERROR("1002", "参数校验异常");

    /**
     * 返回码
     */
    private String code;
    /**
     * 返回消息
     */
    private String message;

}
