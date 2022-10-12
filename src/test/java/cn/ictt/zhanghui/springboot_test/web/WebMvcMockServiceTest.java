package cn.ictt.zhanghui.springboot_test.web;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOpeareRepository;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import cn.ictt.zhanghui.springboot_test.business.service.UserTestService;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

/**
 * Description:
 * <p>
 * 在写对 Service 进行单元测试时，会将 Mapper 层进行 Mock
 * <p>
 * 核心 @MockBean + Mockito.when 测试 Service层时模拟Mapper层返回的结果
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/10/12 10:41
 **/
@Rollback
public class WebMvcMockServiceTest extends SpringbootTestApplicationTests {

    @MockBean
    private UserOpeareRepository userOpeareRepository;

    @Autowired
    private UserTestService userTestService;

    /**
     * controller 集成测试
     */
    @Test
    @Sql(statements = "delete from role_test")
    public void testAddUser() throws Exception {
        // Mockito.when 希望在执行特定方法的时候 执行指定的逻辑 常用于第三方 调用情况模拟
        // 屏蔽Mapper层调用细节
        UserOperate userOperate = new UserOperate();
        userOperate.setUsername("huizhang43");
        userOperate.setPassword("123456");

        // Mockito.when 希望在执行特定方法的时候 执行指定的逻辑 常用于第三方 调用情况模拟
        // 屏蔽Mapper层调用细节
        Mockito.when(userOpeareRepository.save(userOperate)).thenReturn(userOperate);

        // 测试controller层
        String result = userTestService.addUser();
        Assert.assertEquals(result, "ok");
    }
}
