package cn.ictt.zhanghui.springboot_test.common.socket.netty;

import lombok.Data;

/**
 * @description: 传递从客户端传递过来的数据
 * @author: nickle
 * @create: 2019-09-09 10:02
 **/
@Data
public class HandleBean {
    private String url;
    private Object data;
}
