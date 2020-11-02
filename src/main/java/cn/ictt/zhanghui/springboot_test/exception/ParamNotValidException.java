package cn.ictt.zhanghui.springboot_test.exception;

public class ParamNotValidException extends RuntimeException{

	public ParamNotValidException(){
		super();
		
	}
	
	public ParamNotValidException(String message){
		super(message);
	}
}
