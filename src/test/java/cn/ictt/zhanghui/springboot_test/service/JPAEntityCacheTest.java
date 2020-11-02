package cn.ictt.zhanghui.springboot_test.service;

import cn.ictt.zhanghui.springboot_test.domain.UserOpeareRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * 测试JPA的本地缓存 @CacheValue 和 @CacheEvict
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JPAEntityCacheTest {
    @Autowired
    private UserOpeareRepository userRepository;
    //等于RedisTemplate<String, String>
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private CacheManager cacheManager;

    @Test
    public void test1(){
       /* redisTemplate.opsForValue().set("user1",new User_Operate(1L,"张辉",18));
        System.out.println(redisTemplate.opsForValue().get("user1"));*/
    }

    @Transactional
    @Test
    public void test2()throws InterruptedException{
        /*User_Operate user_operate = userRepository.findByName("张辉");
        System.out.println("age的旧值是：" + user_operate);
        userRepository.editAgeById(54,user_operate.getId());
        User_Operate user_operate1 = userRepository.findByName("张辉");
        System.out.println("age的新值是：" + user_operate1);*/
    }
}
