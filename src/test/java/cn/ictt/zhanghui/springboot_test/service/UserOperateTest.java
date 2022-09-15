package cn.ictt.zhanghui.springboot_test.service;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import cn.ictt.zhanghui.springboot_test.common.dynamicquery.DynamicQuery;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class UserOperateTest extends SpringbootTestApplicationTests {

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
