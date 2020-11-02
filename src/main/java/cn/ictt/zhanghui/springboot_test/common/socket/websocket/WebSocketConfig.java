package cn.ictt.zhanghui.springboot_test.common.socket.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * WebSocket配置
 * 创建者  科帮网
 * 创建时间	2018年5月29日
 */
//@Configuration
public class WebSocketConfig {  
    @Bean  
    public ServerEndpointExporter serverEndpointExporter() {  
        return new ServerEndpointExporter();  
    }  
}  
