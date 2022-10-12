package cn.ictt.zhanghui.springboot_test.web;

import cn.ictt.zhanghui.springboot_test.business.service.UserTestService;
import cn.ictt.zhanghui.springboot_test.business.web.UserController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Description:
 * <p>
 * 在写对 Controller 进行单元测试时，会将 Service 层进行 Mock
 * <p>
 * 核心 @MockBean + Mockito.when 测试 Controller层时 模拟 Service层返回的结果
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/10/12 10:41
 **/
@Rollback
@RunWith(SpringRunner.class)
@WebMvcTest(value = {UserController.class}) // 测试controller层可以不需要加载整个spring环境
public class WebMvcMockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserTestService userTestService;

    /**
     * controller 集成测试
     */
    @Test
    public void testAddUser() throws Exception {
        // Mockito.when 希望在执行特定方法的时候 执行指定的逻辑 常用于第三方 调用情况模拟
        // 屏蔽service层调用细节
        Mockito.when(userTestService.addUser()).thenReturn("ok");

        // 测试controller层
        this.mockMvc.perform(get("/users/addUser"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ok")))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }
}
