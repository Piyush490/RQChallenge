package com.example.rqchallenge;

import com.example.rqchallenge.client.DummyClient;
import com.example.rqchallenge.response.EmployeeDto;
import com.example.rqchallenge.response.EmployeeResponse;
import com.example.rqchallenge.service.EmployeeServiceImpl;
import exception.NoEmployeeFoundException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.reactive.function.client.WebClientException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EmployeeServiceImplTest {

    @Mock
    private DummyClient dummyClient;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @Before
    public void setUp() {
        // Define test data and behaviors here
    }

    @Test
    public void testGetAllEmployees_Success() throws IOException {
        // Define a sample response from the DummyClient
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeName("John");
        List<EmployeeDto> dummyResponseData = new ArrayList<>();
        dummyResponseData.add(employeeDto);

        // Mock the behavior of the DummyClient to return a successful response
        when(dummyClient.fetchAllEmployees()).thenReturn(EmployeeResponse.builder()
            .status("success")
            .message("ok")
            .data(dummyResponseData).build());

        // Call the service method
        List<EmployeeDto> result = employeeService.getAllEmployees();

        // Assert the result
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getEmployeeName());
    }

    @Test(expected = NoEmployeeFoundException.class)
    public void testGetAllEmployees_NoEmployeeFound() throws IOException {
        // Mock the behavior of the DummyClient to return a response with no employees
        when(dummyClient.fetchAllEmployees()).thenReturn(EmployeeResponse.builder()
                .status("success")
                .message("ok")
                .data(new ArrayList<>()).build());

        // Call the service method, which is expected to throw a NoEmployeeFoundException
        employeeService.getAllEmployees();
    }

    @Test(expected = IOException.class)
    public void testGetAllEmployees_HttpError() throws IOException {
        when(dummyClient.fetchAllEmployees()).thenThrow(new WebClientException("API Response Error") {
        });

        // Call the service method, which is expected to throw a HttpClientErrorException
        employeeService.getAllEmployees();
    }
}
