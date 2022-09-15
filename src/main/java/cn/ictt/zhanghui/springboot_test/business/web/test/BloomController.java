package cn.ictt.zhanghui.springboot_test.business.web.test;

import cn.ictt.zhanghui.springboot_test.common.redis.RedisService;
import cn.ictt.zhanghui.springboot_test.common.redis.filter.BloomFilterHelper;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ZhangHui
 * @version 1.0
 * @className BloomController
 * @description bloomfilter的测试
 * @date 2020/7/8
 */
@Controller
@RequestMapping("/bloomfilter")
public class BloomController {

    private static final String BLOOMFILTER_USER = "bloomfilter_user";
    @Autowired(required = false)
    private RedisService redisService;

    @RequestMapping("/getUser")
    @ResponseBody
    public String getUserFromBloomFilter(@RequestBody UserOperate user_operate) {
        BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 100000, 0.1);
        if(redisService.includeByBloomFilter(myBloomFilterHelper,BLOOMFILTER_USER,user_operate.toString())){
            return "is exists";
        }
        return "not exists";
    }

    @RequestMapping("/addUser")
    @ResponseBody
    public String addUserIntoBloomFilter(@RequestBody UserOperate user_operate) {
        try {
            BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 100000, 0.1);
            redisService.addByBloomFilter(myBloomFilterHelper,BLOOMFILTER_USER,user_operate.toString());
        } catch (Throwable e) {
            e.printStackTrace();
            return "is not ok";
        }
        return "ok";
    }
}
