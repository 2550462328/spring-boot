package cn.ictt.zhanghui.springboot_test.base;

import cn.ictt.zhanghui.springboot_test.SpringbootTestApplicationTests;
import cn.ictt.zhanghui.springboot_test.business.schedule.AsyncTaskService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;

public class AsyncTaskServiceTest extends SpringbootTestApplicationTests {

    @Autowired
    private AsyncTaskService asyncTaskService;

    @Test
    public void asyncTest() throws  InterruptedException{
        long start = System.currentTimeMillis();
        Future<String> task1 = asyncTaskService.doTask1();
        Future<String> task2 =asyncTaskService.doTask2();
        Future<String> task3 =asyncTaskService.doTask3();
        while(true){
            if(task1.isDone() && task2.isDone() && task3.isDone()){
                System.out.println("任务全部执行结束");
                break;
            }
            Thread.sleep(1000);
        }
        long end = System.currentTimeMillis();
        System.out.println("任务全部完成，总耗时：" + (end - start) + "毫秒");
    }
    @Test
    public void threadPoolTest1() throws  InterruptedException{
        asyncTaskService.doTask1();
        asyncTaskService.doTask2();
        asyncTaskService.doTask3();
        Thread.currentThread().join();
    }
    /*
    模拟高并发场景下关闭程序
    使用System.exit(0)会因为资源未释放带来问题
     */
    @Test
    public void threadPoolTest2() throws  InterruptedException{
        for (int i = 0; i < 10000; i++) {
            asyncTaskService.doTask1();
            asyncTaskService.doTask2();
            asyncTaskService.doTask3();
            if (i == 9998) {
                System.exit(0);
            }
        }
    }
}
