package cn.ictt.zhanghui.springboot_test.web.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ZhangHui
 * @version 1.0
 * @className CheckErrorController
 * @description 模拟jvm和内存出异常的场景，用于排查检测
 * @date 2020/4/27
 */
@Controller
@RequestMapping("/checkerror")
public class CheckErrorController {

    public static Object lock1 = new Object();
    public static Object lock2 = new Object();

    private AtomicInteger sum = new AtomicInteger(0);;
    /**
     * 模拟触发CPU超载
     * @author ZhangHui
     * @date 2020/5/7
     * @param
     * @return void
     */
    @GetMapping("/cpufull")
    public void cpufull() {
        System.out.println("CPU自循环中...");
        Thread.currentThread().setName("thread-cpufull");
        int num = 0;
        while(true){
            num++;
            if(num == Integer.MAX_VALUE){
                System.out.println("reset");
            }
            num = 0;
        }
    }

    /**
     * 模拟触发内存泄漏
     * @author ZhangHui
     * @date 2020/5/7
     * @param
     * @return void
     */
    @GetMapping("/memleak")
    public void memleak() {
        System.out.println("内存泄漏...");
        Thread.currentThread().setName("thread-memleak");

        ThreadLocal<Byte[]> threadLocal = new ThreadLocal<>();

        threadLocal.set(new Byte[4096 * 1024]);
    }

    /**
     * 模拟死锁的触发
     * @author ZhangHui
     * @date 2020/5/7
     * @param
     * @return void
     */
    @GetMapping("/deadlock")
    public void deadlock() {
        System.out.println("出现死锁...");
        Thread.currentThread().setName("thread-deadlock");
        Thread threadA = new Thread(()->{
            synchronized (CheckErrorController.lock1) {
                System.out.println(Thread.currentThread().getName() + "线程获取到lock1");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (CheckErrorController.lock2) {
                    System.out.println(Thread.currentThread().getName() + "线程获取到lock2");
                }
            }
        });
        threadA.setName("threadA");
        threadA.start();
        Thread threadB = new Thread(()->{
            synchronized (CheckErrorController.lock2) {
                System.out.println(Thread.currentThread().getName() + "线程获取到lock2");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (CheckErrorController.lock1) {
                    System.out.println(Thread.currentThread().getName() + "线程获取到lock1");
                }
            }
        });
        threadB.setName("threadB");
        threadB.start();
    }

    /**
     * 模拟线程频繁切换
     * @author ZhangHui
     * @date 2020/5/7
     * @param
     * @return void
     */
    @GetMapping("/threadyield")
    public void threadyield(int num) {
        System.out.println("线程频繁切换...");
        Thread.currentThread().setName("thread-threadyield");

        for(int i = 0; i < num; i++){
            new Thread(()->{
                while(true) {
                    sum.addAndGet(1);
                    Thread.yield();
                }
            }).start();
        }
    }

    class MyThread1 extends Thread {
        @Override
        public void run() {
            synchronized (CheckErrorController.lock1) {
                System.out.println(Thread.currentThread().getName() + "线程获取到lock1");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (CheckErrorController.lock2) {
                    System.out.println(Thread.currentThread().getName() + "线程获取到lock2");
                }
            }
        }
    }

    class MyThread2 extends Thread {
        @Override
        public void run() {
            synchronized (CheckErrorController.lock2) {
                System.out.println(Thread.currentThread().getName() + "线程获取到lock2");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (CheckErrorController.lock1) {
                    System.out.println(Thread.currentThread().getName() + "线程获取到lock1");
                }
            }
        }
    }
}