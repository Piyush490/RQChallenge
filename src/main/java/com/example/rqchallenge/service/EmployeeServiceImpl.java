package com.example.rqchallenge.service;

import com.example.rqchallenge.client.DummyClient;
import com.example.rqchallenge.response.EmployeeCreateResponse;
import com.example.rqchallenge.response.EmployeeDeleteResponse;
import com.example.rqchallenge.response.EmployeeDto;
import com.example.rqchallenge.response.EmployeeResponse;
import exception.NoEmployeeFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final DummyClient dummyClient;

    @Autowired
    public EmployeeServiceImpl(DummyClient dummyClient) {
        this.dummyClient = dummyClient;
    }

    @Override
    public List<EmployeeDto> getAllEmployees() throws IOException, NoEmployeeFoundException {
        try {
            EmployeeResponse response = dummyClient.fetchAllEmployees();

            if (Objects.equals(response.getStatus(), "success")) {
                List<EmployeeDto> employees = response.getData();
                if (!employees.isEmpty()) {
                    return response.getData();
                } else {
                    log.warn("No employees found in the response.");
                    throw new NoEmployeeFoundException(404, "no employee found");
                }
            } else {
                log.error("Response Error: " + response.getStatus());
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "API Response Error: " + response.getStatus());
            }
        }
        catch (NoEmployeeFoundException e){
            throw e;
        }
        catch (WebClientException e) {
            log.error("HTTP Error: " + e.getMessage());
            throw new IOException("HTTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            throw new IOException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public List<EmployeeDto> getEmployeesByNameSearch(String searchString) throws IOException {
        try {
            EmployeeResponse response = dummyClient.fetchAllEmployees();

            if (Objects.equals(response.getStatus(), "success")) {
                List<EmployeeDto> employees = response.getData();
                List<EmployeeDto> filteredEmployees = employees.stream().filter(employee -> employee.getEmployeeName().contains(searchString)).collect(Collectors.toList());
                return filteredEmployees;
            } else {
                log.error("Response Error: " + response.getStatus());
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "API Response Error: " + response.getStatus());
            }
        } catch (WebClientException e) {
            log.error("HTTP Error: " + e.getMessage());
            throw new IOException("HTTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            throw new IOException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeDto getEmployeeById(String id) throws IOException {
        try {
            EmployeeCreateResponse response = dummyClient.fetchEmployeeById(id);

            if (Objects.equals(response.getStatus(), "success")) {
                return response.getData();
            } else {
                log.error("Response Error: " + response.getStatus());
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "API Response Error: " + response.getStatus());
            }
        } catch (WebClientException e) {
            log.error("HTTP Error: " + e.getMessage());
            throw new IOException("HTTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            throw new IOException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public Integer getHighestSalaryOfEmployee() throws IOException {
        try {
            EmployeeResponse response = dummyClient.fetchAllEmployees();

            if (Objects.equals(response.getStatus(), "success")) {
                List<EmployeeDto> employees = response.getData();
                if (!employees.isEmpty()) {
                    return employees.stream().map(EmployeeDto::getEmployeeSalary).max(Integer::compareTo).orElse(0);
                } else {
                    log.warn("No employees found in the response.");
                    throw new NoEmployeeFoundException(404, "no employee found");
                }
            } else {
                log.error("API Response Error: " + response.getStatus());
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "API Response Error: " + response.getStatus());
            }
        } catch (WebClientException e) {
            log.error("HTTP Error: " + e.getMessage());
            throw new IOException("HTTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            throw new IOException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> getTopTenHighestEarningEmployeeNames() throws IOException {
        try {
            EmployeeResponse response = dummyClient.fetchAllEmployees();
            if (Objects.equals(response.getStatus(), "success")) {
                List<EmployeeDto> employees = response.getData();
                if (!employees.isEmpty()) {
                    // Sort employees by salary in descending order
                    List<String> topTenNames = employees.stream().sorted(Comparator.comparing(EmployeeDto::getEmployeeSalary).reversed()).limit(10).map(EmployeeDto::getEmployeeName).collect(Collectors.toList());
                    return topTenNames;
                } else {
                    log.warn("No employees found in the response.");
                    throw new NoEmployeeFoundException(404, "no employee found");
                }
            } else {
                log.error("API Response Error: " + response.getStatus());
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "API Response Error: " + response.getStatus());
            }
        } catch (WebClientException e) {
            log.error("HTTP Error: " + e.getMessage());
            throw new IOException("HTTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            throw new IOException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public EmployeeDto createEmployee(Map<String, Object> employeeInput) throws IOException {
        try {
            EmployeeCreateResponse response = dummyClient.createEmployee(employeeInput);

            if (Objects.equals(response.getStatus(), "success")) {
                return response.getData();
            } else {
                log.error("Response Error: " + response.getStatus());
                throw new IOException("API Response Error: " + response.getStatus());
            }
        } catch (WebClientException e) {
            log.error("HTTP Error: " + e.getMessage());
            throw new IOException("HTTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            throw new IOException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

    @Override
    public String deleteEmployeeById(String id) throws IOException {
        try {
            EmployeeDeleteResponse response = dummyClient.deleteEmployeeById(id);

            if (Objects.equals(response.getStatus(), "success")) {
                return response.getMessage();
            } else {
                log.error("Response Error: " + response.getMessage());
                throw new IOException("API Response Error: " + response.getMessage());
            }
        } catch (WebClientException e) {
            log.error("HTTP Error: " + e.getMessage());
            throw new IOException("HTTP Error: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("An unexpected error occurred: " + e.getMessage());
            throw new IOException("An unexpected error occurred: " + e.getMessage(), e);
        }
    }

}
