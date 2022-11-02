package cn.ictt.zhanghui.springboot_test.business.reactive.client;

import cn.ictt.zhanghui.springboot_test.business.pojo.po.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Description:
 *
 * @author createdBy huizhang43.
 * @date createdAt 2022/11/2 10:56
 **/
@Component
public class UserClient {
    private final WebClient webClient;

    public UserClient(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8082").build();
    }

    public Mono<String> getUser(){
        return this.webClient.get().uri("/hello").accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(User.class)
                .map(User::getName);
    }
}
