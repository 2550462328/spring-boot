package cn.ictt.zhanghui.springboot_test.common.lock;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author ZhangHui
 * @version 1.0
 * @className ReentrantSpinLock
 * @description 可重入的自旋锁
 * @date 2020/5/11
 */
public class ReentrantSpinLock {
    private AtomicReference<Thread> lockThread = new AtomicReference<>();

    private int count;

    public void lock(){
        Thread currentThread = Thread.currentThread();

        if(currentThread == lockThread.get()){
            count++;
            return;
        }

        while(!lockThread.compareAndSet(null,currentThread)){
            //DO NOTHING
        }
    }

    public void unlock(){
        Thread currentThread = Thread.currentThread();

        if(currentThread == lockThread.get() && --count == 0){
            lockThread.compareAndSet(currentThread,null);
        }
    }

}
