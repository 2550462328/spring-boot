package cn.ictt.zhanghui.springboot_test.web;

import cn.ictt.zhanghui.springboot_test.util.http.HttpClient;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangHui
 * @version 1.0
 * @className OAuthController
 * @description OAuth授权控制类
 * @date 2020/9/10
 */
@Controller
@RequestMapping("/oauth")
@Slf4j
public class OAuthController {

    @Value("${oauth.clientid}")
    private String clientid;

    @Value("${oauth.clientsecret}")
    private String clientsecret;

    @Autowired
    private HttpClient httpClient;

    @GetMapping("/index")
    public String index(){
        return "oauth/index";
    }

    @GetMapping("/redirect")
    public ModelAndView redirectUrl(@RequestParam(name = "code",required = false) String code) {

        ModelAndView mv = new ModelAndView();

        log.info("获取github官网用户授权码{}", code);

        String getAccessUrl = "https://github.com/login/oauth/access_token?client_id="
                + clientid + "&client_secret="
                + clientsecret + "&code=" + code;

        HttpHeaders headers = new HttpHeaders();
        List<MediaType> mediaTypes = new ArrayList<>();
        mediaTypes.add(MediaType.APPLICATION_JSON);
        headers.setAccept(mediaTypes);

        String access = httpClient.send(getAccessUrl,HttpMethod.POST,null,headers);
        JSONObject accessObj = JSONObject.parseObject(access);

        String accessToken = accessObj.getString("access_token");

        log.info("获取github官网用户登录令牌{}", accessToken);

        // 第一种方式
//        String getUserUrl = "https://api.github.com/user?access_token="+accessToken;

        String getUserUrl = "https://api.github.com/user";

        HttpHeaders headers_getUser = new HttpHeaders();
        List<MediaType> mediaTypes_getUser = new ArrayList<>();
        mediaTypes_getUser.add(MediaType.APPLICATION_JSON);

        headers_getUser.setAccept(mediaTypes_getUser);
        // 第二种方式
        headers_getUser.set("Authorization","token "+ accessToken);

        String user = httpClient.send(getUserUrl,HttpMethod.GET,null,headers_getUser);

        log.info("获取github官网用户信息{}", user);

        JSONObject userInfo = JSONObject.parseObject(user);

        mv.addObject("userName", userInfo.getString("login"));
        mv.setViewName("/oauth/success");
        return mv;
    }

}
