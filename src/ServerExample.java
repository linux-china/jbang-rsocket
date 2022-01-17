///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 8
//DEPS org.slf4j:slf4j-simple:1.7.33
//DEPS io.rsocket:rsocket-core:1.1.1
//DEPS io.rsocket:rsocket-transport-netty:1.1.1

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import io.rsocket.core.RSocketServer;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;

public class ServerExample {
    public static void main(String[] args) {
        Hooks.onErrorDropped(e -> {});
        RSocket handler = new RSocket() {
            @Override
            public Mono<Payload> requestResponse(Payload payload) {
                System.out.println("RequestResponse: " + payload.getDataUtf8());
                return Mono.just(payload);
            }

            @Override
            public Flux<Payload> requestStream(Payload payload) {
                System.out.println("RequestStream: " + payload.getDataUtf8());
                return Flux.just("First", "Second").map(DefaultPayload::create);
            }

            @Override
            public Mono<Void> fireAndForget(Payload payload) {
                System.out.println("FireAndForget: " + payload.getDataUtf8());
                return Mono.empty();
            }
        };
        RSocketServer.create(SocketAcceptor.with(handler))
                .bindNow(TcpServerTransport.create("localhost", 7000))
                .onClose()
                .doOnSubscribe(subscription -> System.out.println("RSocket Server listen on tcp://localhost:7000"))
                .block();
    }
}
