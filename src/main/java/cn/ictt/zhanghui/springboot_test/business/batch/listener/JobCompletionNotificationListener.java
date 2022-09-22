package cn.ictt.zhanghui.springboot_test.business.batch.listener;

import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/9/21 17:04
 **/
@Component
@Slf4j
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED){
            log.info("job have finished, and get verify");
            jdbcTemplate.query("select username,password from user_test",(rs,row)->new UserOperate(rs.getString(1),rs.getString(2))).forEach(user ->{
                log.info("verify result:" + user.toString());
            });
        }

    }

    @Override
    public void beforeJob(JobExecution jobExecution) {
        super.beforeJob(jobExecution);
    }
}
