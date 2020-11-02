//package cn.ictt.zhanghui.springboot_test.service;
//
//import cn.ictt.zhanghui.springboot_test.service.impl.HelloWorldImpl;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.lang.reflect.Proxy;
//
//import static org.hamcrest.Matchers.equalTo;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///*
//模拟测试http请求
// */
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class HelloWorldTest {
//    private MockMvc mvc;
//
//    @Before
//    public void setUp() {
//        mvc = MockMvcBuilders.standaloneSetup(new HelloController()).build();
//    }
//
//    //Mockito测试
//    @Test
//    public void getHello() {
//        try {
//            //Mockito
//
//            //MockMVC
//            mvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(content().string(equalTo("hello world")));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void main(String[] args) {
//        HelloWorld helloWorld = new HelloWorldImpl();
//        HelloWorldHandler handler = new HelloWorldHandler(helloWorld);
//        HelloWorld helloWorldProxy = (HelloWorld) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), helloWorld.getClass().getInterfaces(), handler);
//        helloWorldProxy.printStr();
//    }
//}
