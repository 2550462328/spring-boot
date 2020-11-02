package cn.ictt.zhanghui.springboot_test.service;

import cn.ictt.zhanghui.springboot_test.domain.UserOperate;

public interface UserTestService{
    String addUser();

    UserOperate saveUser(UserOperate userOperate);

    UserOperate findByName(String name);
}

