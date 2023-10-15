package com.example.rqchallenge.controller;

import com.example.rqchallenge.response.EmployeeDto;
import com.example.rqchallenge.response.EmployeeResponse;
import com.example.rqchallenge.service.EmployeeService;
import com.example.rqchallenge.exception.NoEmployeeFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmployeeControllerImpl implements IEmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Override
    public ResponseEntity<EmployeeResponse> getAllEmployees() {

        try {
            List<EmployeeDto> employees = employeeService.getAllEmployees();
            return ResponseEntity.ok().body(EmployeeResponse.builder().data(employees).status(HttpStatus.OK.getReasonPhrase()).build());
        } catch (NoEmployeeFoundException e){
            return ResponseEntity.status(e.getStatus())
                    .body(EmployeeResponse.builder()
                            .status(e.getStatus().toString())
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EmployeeResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message(e.getMessage())
                    .build());
        }
    }

    @Override
    public ResponseEntity<EmployeeResponse> getEmployeesByNameSearch(String searchString) {
        try {
            List<EmployeeDto> employees = employeeService.getEmployeesByNameSearch(searchString);
            return ResponseEntity.ok().body(EmployeeResponse.builder().data(employees).status(HttpStatus.OK.getReasonPhrase()).build());
        } catch (NoEmployeeFoundException e){
            return ResponseEntity.status(e.getStatus())
                    .body(EmployeeResponse.builder()
                            .status(e.getStatus().toString())
                            .message(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EmployeeResponse.builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                    .message(e.getMessage())
                    .build());
        }
    }

    @Override
    public ResponseEntity<EmployeeDto> getEmployeeById(String id) {
        try {
            EmployeeDto employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok().body(employee);
        } catch (NoEmployeeFoundException e){
            return ResponseEntity.status(e.getStatus()).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try {
            Integer highestSalaryOfEmployee = employeeService.getHighestSalaryOfEmployee();
            return ResponseEntity.ok().body(highestSalaryOfEmployee);
        }
        catch (NoEmployeeFoundException e){
            return ResponseEntity.status(e.getStatus())
                    .body(0);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            List<String> topTenHighestEarningEmployeeNames = employeeService.getTopTenHighestEarningEmployeeNames();
            return ResponseEntity.ok().body(topTenHighestEarningEmployeeNames);
        }
        catch (NoEmployeeFoundException e) {
            return ResponseEntity.status(e.getStatus()).body(Collections.EMPTY_LIST);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }

    }

    @Override
    public ResponseEntity<EmployeeDto> createEmployee(Map<String, Object> employeeInput) {

        try {
            EmployeeDto createdEmployee = employeeService.createEmployee(employeeInput);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEmployee);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        try {
            String message = employeeService.deleteEmployeeById(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
        }
        catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("employee cannot be deleted");
        }
    }
}
