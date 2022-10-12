package cn.ictt.zhanghui.springboot_test.web;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOpeareRepository;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

/**
 * Description:
 * <p>
 * 针对Mapper层进行测试
 * <p>
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/10/12 10:41
 **/
@Rollback
public class WebMvcMockMapperTest extends SpringbootTestApplicationTests {

    @Autowired
    private UserOpeareRepository userOpeareRepository;

    /**
     * controller 集成测试
     */
    @Test
    @Sql(statements = "delete from user_test")
    public void testAddUser() throws Exception {
        // Mockito.when 希望在执行特定方法的时候 执行指定的逻辑 常用于第三方 调用情况模拟
        // 屏蔽Mapper层调用细节
        UserOperate userOperate = new UserOperate();
        userOperate.setUsername("huizhang43");
        userOperate.setPassword("123456");

        Assert.assertNotNull(userOpeareRepository.save(userOperate));
    }
}
