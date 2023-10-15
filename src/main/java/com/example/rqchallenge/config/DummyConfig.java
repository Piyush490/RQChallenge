package com.example.rqchallenge.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@PropertySources(value = {@PropertySource("classpath:dummy.properties")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DummyConfig {

    @Value("${dummy.client.connection.timeout}")
    private Integer connectionTimeout;

    @Value("${dummy.client.read.timeout}")
    private Integer readTimeout;

    @Value("${dummy.client.write.timeout}")
    private Integer writeTimeout;

    @Value("${dummy.base.url}")
    private String baseUrl;

}
