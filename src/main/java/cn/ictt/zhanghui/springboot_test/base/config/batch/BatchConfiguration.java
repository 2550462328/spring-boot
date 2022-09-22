package cn.ictt.zhanghui.springboot_test.base.config.batch;

import cn.ictt.zhanghui.springboot_test.business.batch.listener.JobCompletionNotificationListener;
import cn.ictt.zhanghui.springboot_test.business.batch.processor.UserItemProcessor;
import cn.ictt.zhanghui.springboot_test.business.pojo.domain.UserOperate;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/9/21 16:37
 **/
@Configuration
@ConditionalOnProperty(name = "spring.batch.job.enabled", havingValue = "true", matchIfMissing = false)
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    /**
     * 输入 + 转换
     *
     * @return
     */
    @Bean
    public FlatFileItemReader<UserOperate> reader() {
        return new FlatFileItemReaderBuilder<UserOperate>()
                .name("userItemReader")
                .resource(new ClassPathResource("/csv/2.csv"))
                .delimited()
                .names(new String[]{"username", "password"})
                .fieldSetMapper(new BeanWrapperFieldSetMapper<UserOperate>() {{
                    setTargetType(UserOperate.class);
                }}).build();
    }

    @Bean
    public UserItemProcessor userItemProcessor() {
        return new UserItemProcessor();
    }

    /**
     * 映射 + 输出
     *
     * @param dataSource
     * @return
     */
    @Bean
    public JdbcBatchItemWriter<UserOperate> writer(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<UserOperate>()
                .itemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>())
                .sql("insert into user_test(username, password) values(:username, :password)")
                .dataSource(dataSource)
                .build();
    }

    @Bean
    public Step step1(JdbcBatchItemWriter<UserOperate> writer) {
        return stepBuilderFactory.get("step1")
                .<UserOperate, UserOperate>chunk(10)
                .reader(reader())
                .processor(userItemProcessor())
                .writer(writer)
                .build();
    }

    /**
     * job >>  step >> processor
     * reader + listener + writer
     *
     * @param listener
     * @param step1
     * @return
     */
    @Bean
    public Job importUserJob(JobCompletionNotificationListener listener, Step step1) {
        return jobBuilderFactory.get("importUserJob")
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(step1)
                .end()
                .build();
    }


}
