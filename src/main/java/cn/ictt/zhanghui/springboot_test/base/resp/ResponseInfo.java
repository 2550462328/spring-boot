package cn.ictt.zhanghui.springboot_test.base.resp;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author ZhangHui
 * @version 1.0
 * @className ResponseInfo
 * @description 接口回应信息
 * @date 2020/3/10
 */
@Data
@NoArgsConstructor
public class ResponseInfo<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    private T data;

    private Boolean result = true;

    private String message;

    private String code;

    public static ResponseInfo OK() {
        return OK(null);
    }

    public static ResponseInfo OK(Object data) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setResult(true);
        responseInfo.setCode("0");
        responseInfo.setData(data);
        return responseInfo;
    }

    public static ResponseInfo FAIL() {
        return FAIL("1", "");
    }

    public static ResponseInfo FAIL(String code, String message) {
        ResponseInfo responseInfo = new ResponseInfo();
        responseInfo.setResult(false);
        responseInfo.setCode(code);
        return responseInfo;
    }
}
