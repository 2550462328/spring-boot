package cn.ictt.zhanghui.springboot_test;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/9/14 10:40
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class SpringbootTestApplicationTests {

    @Before
    public void setUp() {
    }
}
