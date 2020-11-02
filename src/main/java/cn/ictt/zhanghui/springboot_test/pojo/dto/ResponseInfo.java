package cn.ictt.zhanghui.springboot_test.pojo.dto;

import java.io.Serializable;

/**
 * @author ZhangHui
 * @version 1.0
 * @className ResponseInfo
 * @description 接口回应信息
 * @date 2020/3/10
 */
public class ResponseInfo implements Serializable {

    private static final long serialVersionUID = -1L;

    private Object data;

    private Boolean result;

    private String message;

    public ResponseInfo(Object data) {
        this.data = data;
    }

    public ResponseInfo(Boolean result, String message){
        this.result = result;
        this.message = message;
    }

    public ResponseInfo(Object data, Boolean result, String message) {
        this.data = data;
        this.result = result;
        this.message = message;
    }

    public  static ResponseInfo OK(){
        return new ResponseInfo(true, "successful");
    }

    public  static ResponseInfo FAIL(){
        return new ResponseInfo(false, "failed");
    }

    public Object getData() {
        return data;
    }

    public Boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }
}
