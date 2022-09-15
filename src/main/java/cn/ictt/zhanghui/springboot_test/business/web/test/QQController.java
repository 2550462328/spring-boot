package cn.ictt.zhanghui.springboot_test.business.web.test;

import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ZhangHui
 * @version 1.0
 * @className QQController
 * @description 这是描述信息
 * @date 2019/12/17
 */
@RestController
@RequestMapping("/qq/receiveMessage")
public class QQController {

    private static String[] str;

    static {
        str = crawl("https://www.sohu.com/a/246140402_115470");
    }

    @PostMapping(produces = "application/json;charset=utf-8")
    public String receive() throws UnsupportedEncodingException {
        String[] str1 = new String[]{
                "老婆你最好了", "最爱我老婆了", "老婆今天乖不乖啊", "我怎么会有世界上最好的老婆", "老婆我想你了"
        };
        int length = str.length;
        String[] str_old = str;
        str = new String[length + str1.length];

        System.arraycopy(str_old, 0, str, 0, length);
        System.arraycopy(str1, 0, str, length - 1, str1.length);

        int index = new Random().nextInt(str.length);
        while (!StringUtils.hasText(str[index])) {
            index = new Random().nextInt(str.length);
        }
        JSONObject result = new JSONObject();
        result.put("msg", str[index]);
        return result.toJSONString();
    }


    public static String[] crawl(String urlTo) {
        URL url = null;
        URLConnection urlconn = null;
        BufferedReader br = null;
        //url匹配规则
        String regex = "</strong>.*</p>";
        Pattern p = Pattern.compile(regex);
        List<String> result = new ArrayList<>();
//        PrintWriter pw = null;
        try {
            //爬取的网址、这里爬取的是一个生物网站
            url = new URL(urlTo);
            urlconn = url.openConnection();
//            pw = new PrintWriter(new FileWriter("D:/SiteURL.txt"), true);//将爬取到的链接放到D盘的SiteURL文件中
            br = new BufferedReader(new InputStreamReader(
                    urlconn.getInputStream()));
            String buf = null;

            while ((buf = br.readLine()) != null) {
//                pw.println(br.readLine());
                Matcher buf_m = p.matcher(buf);
                while (buf_m.find()) {
                    result.add(buf_m.group().replace("</strong>", "").replace("</p>", "").replace(" ", "").replace("\"", "").replace("<strong>", ""));
                }
            }
            System.out.println("爬取成功^_^");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result.toArray(new String[0]);
    }
}
