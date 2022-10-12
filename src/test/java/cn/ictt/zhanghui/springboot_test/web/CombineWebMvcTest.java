package cn.ictt.zhanghui.springboot_test.web;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Description:
 * 集成测试
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/10/12 10:41
 **/
@Rollback
@AutoConfigureMockMvc(addFilters = false)
public class CombineWebMvcTest extends SpringbootTestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * controller 集成测试
     */
    @Test
    @Sql(statements = {"delete from user_test","delete from role_test"})
    public void testAddUser() throws Exception {
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
