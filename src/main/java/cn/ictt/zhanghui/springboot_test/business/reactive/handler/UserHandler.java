package cn.ictt.zhanghui.springboot_test.business.reactive.handler;

import cn.ictt.zhanghui.springboot_test.business.pojo.po.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/11/2 10:48
 **/
@Component
public class UserHandler {

    public Mono<ServerResponse> hello(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromObject(new User(1L, "张辉", 18)));
    }
}
