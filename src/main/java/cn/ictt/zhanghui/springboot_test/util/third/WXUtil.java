//package cn.ictt.zhanghui.springboot_test.util;
//
//import cn.ictt.zhanghui.springboot_test.util.http.HttpDelegate;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import org.apache.lucene.spatial3d.geom.Tools;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class WXUtil {
//	// 云森物联网
//	// private static final String WX_APPId="wxf5338f2a09e9ad72";
//	// private static final String
//	// WX_AppSecret="9189dd33b3461af1e87b8a1be90e1966";
//	// 云森物联网管家
//	// private static final String WX_APPId="wx47332e7b0f2806df";
//	// private static final String
//	// WX_AppSecret="00b087f5f0df2be41a2565c81331cf63";
//
//	// 获取acess_token
//	private static String gainAccessToken() {
//		String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
//				+ ConstProperties.WX_APPId + "&secret=" + ConstProperties.WX_AppSecret;
//		HttpDelegate httpdelegate = new HttpDelegate();
//		String backData = httpdelegate.executeGet(url, "utf-8");
//		System.out.println(backData);
//		return (String) JSONObject.fromObject(backData).get("access_token");
//	}
//
//	// 获取模板列表
//	public static String gainTemplateList() {
//		String url = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token="
//				+ WXUtil.gainAccessToken();
//		HttpDelegate httpdelegate = new HttpDelegate();
//		String backData = httpdelegate.executeGet(url);
//		System.out.println(backData);
//		return backData;
//	}
//
//	// 获取OpenId
//	public static String gainOpenId(String code) {
//		if (Tools.isEmpty(code))
//			return null;
//		String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + ConstProperties.WX_APPId + "&secret="
//				+ ConstProperties.WX_AppSecret + "&grant_type=authorization_code&code=" + code;
//		HttpDelegate httpdelegate = new HttpDelegate();
//		String result = httpdelegate.executeGet(url, "utf-8");
//		JSONObject jsonObject = JSONObject.fromObject(result);
//		String openId = jsonObject.get("openid") + "";
//		return openId;
//	}
//
//	public static void main(String[] args) {
//		// WXUtil.gainOpenId("001cE2Ql03HGDk1QOvPl0EFdQl0cE2Qs");
//		WXUtil.gainTemplateList();
//	    //WXUtil.sendMessage("ofiOm04tdZGsmtvduiy8mqRupeB4", "您好，XXX已到校", "2018-01-01 11:11:11", "万思智慧校区", "感谢您的使用！");
//	}
//
//	// 发送模板信息
//	public static String sendMessage(String template_id, String openId,String firstValue, String keyword1Value, String keyword2Value,String remarkValue) {
//		String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + WXUtil.gainAccessToken();
//		HttpDelegate httpdelegate = new HttpDelegate();
//		Map<String, Object> map = new HashMap<>();
//		map.put("touser", openId);
//		map.put("template_id", template_id);
//		//map.put("template_id", "RTVopaNDzKkY0PEieZCp_UhGJrJuUMt5ZbcblJGFJPE");
//			Map<String, Object> data = new HashMap<>();
//				Map<String, Object> first = new HashMap<>();
//				first.put("value", firstValue);
//				first.put("color", "#173177");
//			data.put("first", first);
//				Map<String, Object> keyword1 = new HashMap<>();
//				keyword1.put("value", keyword1Value);
//				keyword1.put("color", "#173177");
//			data.put("keyword1", keyword1);
//				Map<String, Object> keyword2 = new HashMap<>();
//				keyword2.put("value", keyword2Value);
//				keyword2.put("color", "#173177");
//			data.put("keyword2", keyword2);
//				Map<String, Object> remark = new HashMap<>();
//				remark.put("value", remarkValue);
//				remark.put("color", "#173177");
//			data.put("remark", remark);
//		map.put("data", data);
//		String backData = httpdelegate.executePost(url, "utf-8", JSON.toJSONString(map));
//		System.out.println(backData);
//		return backData;
//	}
//
//}