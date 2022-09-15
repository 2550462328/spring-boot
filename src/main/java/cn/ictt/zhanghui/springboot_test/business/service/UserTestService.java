package cn.ictt.zhanghui.springboot_test.business.service;

import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;

public interface UserTestService{
    String addUser();

    UserOperate saveUser(UserOperate userOperate);

    UserOperate findByName(String name);
}

