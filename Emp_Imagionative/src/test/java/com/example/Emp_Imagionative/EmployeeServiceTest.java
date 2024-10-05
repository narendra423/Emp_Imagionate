package com.example.service;

import com.example.dto.EmployeeDto;
import com.example.dto.TaxDeductionDto;
import com.example.entity.Employee;
import com.example.exception.EmployeeNotFoundException;
import com.example.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmployee() {
        // Given
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setEmployeeId("E123");
        employeeDto.setFirstName("John");
        employeeDto.setLastName("Doe");
        employeeDto.setEmail("john.doe@example.com");
        employeeDto.setSalary(5000.0);

        Employee employee = new Employee();
        employee.setEmployeeId("E123");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@example.com");
        employee.setSalary(5000.0);

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        employeeService.saveEmployee(employeeDto);

        verify(employeeRepository, times(1)).save(any(Employee.class));

        assertEquals("E123", employee.getEmployeeId());
        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("john.doe@example.com", employee.getEmail());
        assertEquals(5000.0, employee.getSalary());
    }


    @Test
    void testCalculateTaxDeductions() {
        String employeeId = "E123";
        Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setSalary(5000.0);
        employee.setDoj(LocalDate.now().minusMonths(12)); // 1 year ago

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));

        TaxDeductionDto result = employeeService.calculateTaxDeductions(employeeId);

        assertNotNull(result);
        assertEquals(employeeId, result.getEmployeeId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(60000.0, result.getYearlySalary());
        assertEquals(5000.0, result.getTaxAmount());
        assertEquals(200.0, result.getCessAmount());
    }

    @Test
    void testCalculateTaxDeductions_EmployeeNotFound() {
        String employeeId = "E123";

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.calculateTaxDeductions(employeeId);
        });
    }
}
