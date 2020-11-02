package cn.ictt.zhanghui.springboot_test.util;

import cn.ictt.zhanghui.springboot_test.exception.ParamNotValidException;
import cn.ictt.zhanghui.springboot_test.util.cipher.StringUtil;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 
* @ClassName: Assert 
* @Description: map相关验证 
* @version 1.2.5
 */
public class Assert {
    
    public static void notNull(Map map,String... keys) {
    	for(String key : keys){
    		 Object o = map.get(key);
    	        if (o == null) {
    	            throw new ParamNotValidException("缺少入参:"+key);
    	        }
    	        if (o instanceof String&& StringUtil.isBlank((String) o)) {
    	        	 throw new ParamNotValidException("缺少入参:"+key);
    	        }
    	        if (o instanceof String&& StringUtil.equals((String) o, "null")) {
    	        	 throw new ParamNotValidException("缺少入参:"+key);
    	        }
    	}
    }
    
    public static void notNull(JSONObject data,String... keys) {
    	for(String key : keys){
    		 Object o = data.get(key);
    	        if (o == null) {
    	        	 throw new ParamNotValidException("缺少入参:"+key);
    	        }
    	        if (o instanceof String&& StringUtil.isBlank((String) o)) {
    	        	 throw new ParamNotValidException("缺少入参:"+key);
    	        }
    	        if (o instanceof String&& StringUtil.equals((String) o, "null")) {
    	        	 throw new ParamNotValidException("缺少入参:"+key);
    	        }
    	}
       
    }
}
