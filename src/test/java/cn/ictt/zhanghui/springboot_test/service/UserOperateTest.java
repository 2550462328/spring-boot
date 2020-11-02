package cn.ictt.zhanghui.springboot_test.service;

import cn.ictt.zhanghui.springboot_test.common.dynamicquery.DynamicQuery;
import cn.ictt.zhanghui.springboot_test.domain.UserOperate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserOperateTest {

    @Autowired
    private DynamicQuery dynamicQuery;

    @Test
    @Transactional
    public void testSave(){
//      dynamicQuery.save(new UserOperate("pcc","1314"));
        List<UserOperate> list = dynamicQuery.nativeQueryListModel(UserOperate.class,"select * from user_test where username = ?1","zhanghui");

        list.stream().forEach(user ->{
            System.out.println(user);
        });
    }

}
