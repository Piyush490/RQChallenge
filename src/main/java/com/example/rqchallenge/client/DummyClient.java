package com.example.rqchallenge.client;

import com.example.rqchallenge.request.EmployeeRequest;
import com.example.rqchallenge.response.EmployeeCreateResponse;
import com.example.rqchallenge.response.EmployeeDeleteResponse;
import com.example.rqchallenge.response.EmployeeResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
@Slf4j
public class DummyClient {

    private final WebClient webClient;

    public DummyClient(@Qualifier("webClientDummy") WebClient webClient) {
        this.webClient = webClient;
    }

    public EmployeeResponse fetchAllEmployees() {
        ParameterizedTypeReference<EmployeeResponse> typeRef = new ParameterizedTypeReference<EmployeeResponse>() {
        };
        EmployeeResponse employeeResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/employees")
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatus::is5xxServerError, ClientResponse::createException)
                .bodyToMono(typeRef)
                .block();
        log.info("response: {}", employeeResponse);
        return employeeResponse;
    }

    public EmployeeCreateResponse fetchEmployeeById(String id) {
        ParameterizedTypeReference<EmployeeCreateResponse> typeRef = new ParameterizedTypeReference<EmployeeCreateResponse>() {
        };
        EmployeeCreateResponse employeeResponse = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/employee/" + id)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatus::is5xxServerError, ClientResponse::createException)
                .bodyToMono(typeRef)
                .block();
        log.info("response: {}", employeeResponse);
        return employeeResponse;
    }

    public EmployeeCreateResponse createEmployee(Map<String, Object> employeeInput) {
        ParameterizedTypeReference<EmployeeCreateResponse> typeRef = new ParameterizedTypeReference<EmployeeCreateResponse>() {
        };
        EmployeeRequest request = new EmployeeRequest(
                (String) employeeInput.get("name"),
                (Integer) employeeInput.get("salary"),
                (Integer) employeeInput.get("age")
        );

        EmployeeCreateResponse employeeResponse = webClient.post()
                .uri("/create")
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatus::is5xxServerError, ClientResponse::createException)
                .bodyToMono(typeRef)
                .block();
        log.info("response: {}", employeeResponse);
        return employeeResponse;
    }

    public EmployeeDeleteResponse deleteEmployeeById(String id) {
        ParameterizedTypeReference<EmployeeDeleteResponse> typeRef = new ParameterizedTypeReference<EmployeeDeleteResponse>() {
        };
        EmployeeDeleteResponse employeeDeleteResponse = webClient.delete()
                .uri(uriBuilder -> uriBuilder
                        .path("/delete/" + id)
                        .build())
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, ClientResponse::createException)
                .onStatus(HttpStatus::is5xxServerError, ClientResponse::createException)
                .bodyToMono(typeRef)
                .block();
        log.info("response: {}", employeeDeleteResponse);
        return employeeDeleteResponse;
    }
};
