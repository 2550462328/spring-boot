package cn.ictt.zhanghui.springboot_test.base.util.encry;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import org.springframework.util.StringUtils;

/**
 * 验签
 * @author ZhangHui
 * @date 2020/5/11
 * @return
 */
public class RSASignature {
	
	static Logger log = LoggerFactory.getLogger(RSASignature.class);

	public static final String SIGN_ALGORITHMS= "SHA1WithRSA";
	
	public static final String SIGN = "sign";
	
	public static boolean doCheck(String content,String sign,String publicKey,String encode,String type){
		
		try {
			KeyFactory keyFactory = KeyFactory.getInstance(type);
			byte[] encodedKey = Base64.decode(publicKey);
			PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));
			
			Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
			signature.initVerify(pubKey);
			signature.update(content.getBytes(encode));
			
			boolean bverify = signature.verify(Base64.decode(sign));
			return bverify;
		} catch (Exception e) {
			log.error("签名验证出错:"+e.getMessage());
		}
		return false;
	}
	
	public static String getSignContent(Map<String,String> map){
		StringBuffer content =new StringBuffer();
		List<String> keys = new ArrayList<String>(map.keySet());
		Collections.sort(keys);
		int index = 0;
		
		for(int i=0; i < keys.size() ; i++){
			String key = keys.get(i);
			if(SIGN.endsWith(key)){
				continue;
			}
			String value = map.get(key);
			if(StringUtils.hasText(key) && StringUtils.hasText(value)){
				content.append((index == 0 ? "": "&") + key + "=" + value);
				index ++;
			}
		}
		return content.toString();
	}
	
	public static String sign(String content,String privateKey,String encode){
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			
			Signature signature = Signature.getInstance("SHA1WithRSA");
			signature.initSign(priKey);
			signature.update(content.getBytes(encode));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		} catch (Exception e) {
			log.error("签名失败！" + e.getMessage(), e);
		}
		return null;
	}
	
	public static void main(String[] args) {
		String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKJoA2wb/aIVcTeT/34pKDE0Qy9yxr60zfVLJ66c0m/PM0Wk1KKkfA3myXDcE/3mRm3KzfDS1B7qEnJwnTV7pBnHqaeTob6XLSQiNzyv63czGT1jooQmgs963+iElBXdpIt3YZVmS0Q6J2Ez7o/iJfFLAFGPAP8CzDTRiXFie5edAgMBAAECgYEAjrr1DwVGkjVgDCVgrIMFVMEGruOjcaU7QPHtDRO8ChHCkNl9GveOIzWKIkr4svK3QPS0q4rOa+Dxl2wLbh6cp7f2myhwIy9TvyHD/bs99EGd+nEr1i1XrMu3vV94w1PMbUUjW+FJ962lB5dRlc1/5CwoeLjtuIwjmsS/kfstsG0CQQDc/durrBdnhkRr7as8cNWR5hJi3vS42RWnpBuhzz24n720139C3VUttjH83PH5zuoUc32NEwwzs1xtiPONXuHbAkEAvCJF88bTXTkx7Ea8FVi9DiRsuu2MLbNIBHgmVX+tWmwyDA0yCOU4WzrQ7Jj06EQIeIB1iwYHEYSCUoYhfI7R5wJAL2MMCKrVNfC3mUEUd9hmI2vnQ2Y1qbF7EECDYFYVERLH1hBjjI3zZuLroyIpjPyOeyYspOSRuEBcsGwudYEEAwJAN5KsNTFzOCNwAsp2PpwC5I2P7LvE368IxAE12aWocIBrXDH9qLEFL7W1B167YWzPT8Eqd5GlPuB0mqnydFe/2wJALMYBuUdiAdHc7esVUlAUiITPuixJ0zRXajS8FSOddOHkdfXHQxJeHEyrH4r+CzY9pdM3HgPq+5jjVXkijtHSKw==";
		Map map = new HashMap();
		map.put("priority", "1");
		map.put("appId", "ap1905071569");
		map.put("areaCode", "330106");
		map.put("sign", "1");
		map.put("accessType", "1");
		map.put("serialNum", "999991");
		map.put("tradeCode", "ssc.personal.personalInfo");
		Map data = new HashMap();
		data.put("frontBusinessType", "5");
		data.put("name", "黄浙英");
		data.put("idNo", "330722197404070027");
		
		
		map.put("data", JSON.toJSONString(data));
		
		String content = getSignContent(map);
		System.out.println(sign(content, privateKey, "utf-8"));
	}
}
