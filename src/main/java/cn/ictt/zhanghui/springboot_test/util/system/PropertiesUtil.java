package cn.ictt.zhanghui.springboot_test.util.system;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;


/**
 * @author ZhangHui
 * @ClassName:PropertiesUtil
 * @Description 属性工具类
 * @date 2019年6月20日 下午4:09:52
 */
public class PropertiesUtil {
    private static Properties props;

//    static {
//        loadProps();
//    }

    private synchronized static void loadProps() throws Exception {
        props = new Properties();
        InputStream in = null;

        in = PropertiesUtil.class.getResourceAsStream("/config.properties");
        try {
            props.load(new InputStreamReader(in, "utf-8"));
        }  finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String getProperty(String key) throws Exception {
        if (props == null) {
            loadProps();
        }
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) throws Exception {
        if (props == null) {
            loadProps();
        }
        return props.getProperty(key, defaultValue);
    }
}	
