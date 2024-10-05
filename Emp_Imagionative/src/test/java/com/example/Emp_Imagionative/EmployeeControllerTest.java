package com.example.controller;

import com.example.dto.EmployeeDto;
import com.example.dto.TaxDeductionDto;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateEmployee() {
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId("E123");
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");
        employeeDto.setSalary(5000.0);

        doNothing().when(employeeService).saveEmployee(any(EmployeeDto.class));

        ResponseEntity<String> response = employeeController.createEmployee(employeeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Employee created successfully", response.getBody());
    }

    @Test
    void testGetTaxDeductions() {
        String employeeId = "E123";
        TaxDeductionDto taxDeductionDto = new TaxDeductionDto(employeeId, "John", "Doe", 60000.0, 5000.0, 200.0);

        when(employeeService.calculateTaxDeductions(employeeId)).thenReturn(taxDeductionDto);

        ResponseEntity<TaxDeductionDto> response = employeeController.getTaxDeductions(employeeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(taxDeductionDto, response.getBody());
    }
}
