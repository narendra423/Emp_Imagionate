package com.example.controller;

import com.example.dto.EmployeeDto;
import com.example.dto.TaxDeductionDto;
import com.example.exception.CustomException;
import com.example.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createEmployee(@Valid @RequestBody EmployeeDto employeeDto) {
        employeeService.saveEmployee(employeeDto);
        return ResponseEntity.ok("Employee created successfully");
    }

    @GetMapping("/tax-deductions/{employeeId}")
    public ResponseEntity<TaxDeductionDto> getTaxDeductions(@PathVariable String employeeId) {
        TaxDeductionDto taxDeduction = employeeService.calculateTaxDeductions(employeeId);
        return ResponseEntity.ok(taxDeduction);
    }
}
