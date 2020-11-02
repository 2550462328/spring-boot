package cn.ictt.zhanghui.springboot_test.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class MyTask {
    private final String cron = "*/5 * * * * *";
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Scheduled(cron = cron)
    public void executeEveryFiveSeconds(){
        System.out.println("当前时间：" + sdf.format(new Date()));
    }
}
