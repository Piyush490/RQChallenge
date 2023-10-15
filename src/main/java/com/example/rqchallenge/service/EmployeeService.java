package com.example.rqchallenge.service;

import com.example.rqchallenge.response.EmployeeDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface EmployeeService {

    List<EmployeeDto> getAllEmployees() throws IOException;

    List<EmployeeDto> getEmployeesByNameSearch(String searchString) throws IOException;

    EmployeeDto getEmployeeById(String id) throws IOException;

    Integer getHighestSalaryOfEmployee() throws IOException;

    List<String> getTopTenHighestEarningEmployeeNames() throws IOException;

    EmployeeDto createEmployee(Map<String, Object> employeeInput) throws IOException;

    String deleteEmployeeById(String id) throws IOException;
}
