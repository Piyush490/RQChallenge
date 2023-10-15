package com.example.rqchallenge.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import javax.net.ssl.SSLException;

@Configuration
public class WebClientConfiguration {

    private final DummyConfig dummyConfig;

    @Autowired
    public WebClientConfiguration(DummyConfig dummyConfig) {
        this.dummyConfig = dummyConfig;
    }

    @Bean(name = "webClientDummy")
    public WebClient webClientDummy(final WebClient.Builder webClientBuilder) throws SSLException {

        // Create a custom SslContext to trust all certificates (for testing or development purposes)
        SslContext sslContext = SslContextBuilder
                .forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        TcpClient tcpClient =
                TcpClient.create().secure(t -> t.sslContext(sslContext))
                        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, dummyConfig.getConnectionTimeout())
                        .doOnConnected(connection ->
                                connection.addHandlerLast(new ReadTimeoutHandler(dummyConfig.getReadTimeout()))
                                        .addHandlerLast(new WriteTimeoutHandler(dummyConfig.getWriteTimeout())));

        return webClientBuilder
                .clientConnector(new ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .baseUrl(dummyConfig.getBaseUrl())
                .build();
    }
};
