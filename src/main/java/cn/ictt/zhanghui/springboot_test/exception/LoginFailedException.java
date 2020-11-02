package cn.ictt.zhanghui.springboot_test.exception;

public class LoginFailedException extends RuntimeException{

	public LoginFailedException(){
		super();

	}

	public LoginFailedException(String message){
		super(message);
	}
}
