package cn.ictt.zhanghui.springboot_test.exception;

public class MyException extends RuntimeException {
    public MyException() {
        super();
    }
    public MyException(String message) {
        super(message);
    }
}
