package cn.ictt.zhanghui.springboot_test.reactive;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.business.reactive.client.UserClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/11/2 11:04
 **/
public class UserClientTest extends SpringbootTestApplicationTests {

    @Autowired
    private UserClient userClient;

    @Test
    public void testGetUser(){
        System.out.println(userClient.getUser().block());
    }
}
