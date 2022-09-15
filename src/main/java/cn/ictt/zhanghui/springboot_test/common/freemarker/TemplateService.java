package cn.ictt.zhanghui.springboot_test.common.freemarker;

import cn.ictt.zhanghui.springboot_test.base.exception.enums.BusinessResponseEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author ZhangHui
 * @version 1.0
 * @className TemplateUtil
 * @description 页面生成工具类
 * @date 2020/3/19
 */
@Component
public class TemplateService {

    @Autowired
    private Configuration configuration;

    @Value("${spring.freemarker.html.path}")
    private String htmlPath;

    private final static String HTML_SUFFIX = ".html";

    public  void createHtml(Object pageData, String templateName, String htmlName) throws Exception{
        FileOutputStream ops = null;
        Writer writer = null;
        try {
            Template template = configuration.getTemplate(templateName);
            File htmlFile = new File(htmlPath+htmlName+HTML_SUFFIX);
            if(htmlFile.exists()){
                BusinessResponseEnum.TEMPLATE_IS_EXISTS.assertFail(htmlName+HTML_SUFFIX,htmlPath);
            }
            ops = new FileOutputStream(htmlFile);
            writer = new OutputStreamWriter(ops,"UTF-8");
            template.process(pageData,writer);
        } finally {
            if(ops!=null){
                ops.close();
            }
            if(writer != null){
                writer.close();
            }
        }
    }
}
