package cn.ictt.zhanghui.springboot_test.business.reactive.router;

import cn.ictt.zhanghui.springboot_test.business.reactive.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

/**
 * Description:
 *
 * 编程式 路由
 *
 * 需要注意的是 spring-boot-starter-web 和 spring-boot-starter-webflux 会导致Handler 404
 * 即有Mono 存在 不需要 Servlet，两者是冲突的
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/11/2 10:53
 **/
@Configuration
public class UserRequestRouter {

    @Bean
    public RouterFunction<ServerResponse> route(UserHandler userHandler) {
        return RouterFunctions.route(GET("/hello").and(accept(MediaType.APPLICATION_JSON)), userHandler::hello);
    }
}
