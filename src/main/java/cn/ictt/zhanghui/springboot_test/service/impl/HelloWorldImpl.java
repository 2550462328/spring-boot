package cn.ictt.zhanghui.springboot_test.service.impl;

import cn.ictt.zhanghui.springboot_test.service.HelloWorld;

/**
 * @author ZhangHui
 * @version 1.0
 * @className HelloWorldImpl
 * @description 这是描述信息
 * @date 2019/6/4
 */
public class HelloWorldImpl implements HelloWorld {

    @Override
    public void printStr() {
        System.out.println("hello world!");
    }
}
