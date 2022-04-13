///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 8
//DEPS org.springframework.boot:spring-boot-dependencies:2.6.6@pom
//DEPS org.springframework.boot:spring-boot-starter-rsocket
//JAVA_OPTIONS -Dspring.rsocket.server.port=7000

package spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class RSocketSpringBootServer {
    public static void main(String[] args) {
        SpringApplication.run(RSocketSpringBootServer.class, args);
    }

    @Controller
    public static class RSocketController {
        @MessageMapping("hello")
        public Mono<String> hello(String name) {
            return Mono.just("Hello " + name);
        }
    }

}
