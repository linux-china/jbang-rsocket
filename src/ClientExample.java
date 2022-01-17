///usr/bin/env jbang "$0" "$@" ; exit $?
//JAVA 8
//DEPS org.slf4j:slf4j-simple:1.7.33
//DEPS io.rsocket:rsocket-core:1.1.1
//DEPS io.rsocket:rsocket-transport-netty:1.1.1

import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.core.RSocketConnector;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;

public class ClientExample {

    public static void main(String[] args) {
        Hooks.onErrorDropped(e -> {
        });
        RSocket clientRSocket = RSocketConnector.connectWith(TcpClientTransport.create("localhost", 7000)).block();
        assert clientRSocket != null;
        try {
            // request response
            final Payload response = clientRSocket.requestResponse(DefaultPayload.create("Hello")).block();
            System.out.println("echo from RSocket Server: " + response.getDataUtf8());
            Flux<Payload> s = clientRSocket.requestStream(DefaultPayload.create("peace"));
            System.out.println("stream from RSocket Server: ");
            s.take(10).doOnNext(p -> System.out.println(p.getDataUtf8())).blockLast();
        } finally {
            clientRSocket.dispose();
        }
    }
}