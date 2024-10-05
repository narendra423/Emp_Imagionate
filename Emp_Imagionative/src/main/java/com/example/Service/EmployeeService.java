import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import com.example.Emp_Imagionative.repository.EmployeeRepository;
import com.example.Emp_Imagionative.dto.TaxDeductionDto;
import com.example.Emp_Imagionative.exception.EmployeeNotFoundException;
import com.example.Emp_Imagionative.entity.Employee;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public void saveEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee();
        employee.setEmployeeId(employeeDto.getEmployeeId());
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhoneNumbers(employeeDto.getPhoneNumbers());
        employee.setDoj(employeeDto.getDoj());
        employee.setSalary(employeeDto.getSalary());
        this.employeeRepository.save(employee);
    }

    public TaxDeductionDto calculateTaxDeductions(String employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found"));

        double yearlySalary = calculateYearlySalary(employee.getDoj(), employee.getSalary());
        double taxAmount = calculateTax(yearlySalary);
        double cessAmount = calculateCess(yearlySalary);

        return new TaxDeductionDto(employee.getEmployeeId(), employee.getFirstName(), employee.getLastName(), yearlySalary, taxAmount, cessAmount);
    }

    private double calculateYearlySalary(LocalDate doj, double monthlySalary) {
        int monthsWorked = (int) ChronoUnit.MONTHS.between(doj, LocalDate.now());
        return monthsWorked * monthlySalary;
    }

    private double calculateTax(double yearlySalary) {
        if (yearlySalary <= 250000) return 0;
        if (yearlySalary <= 500000) return (yearlySalary - 250000) * 0.05;
        if (yearlySalary <= 1000000) return 12500 + (yearlySalary - 500000) * 0.1;
        return 62500 + (yearlySalary - 1000000) * 0.2;
    }

    private double calculateCess(double yearlySalary) {
        return yearlySalary > 2500000 ? (yearlySalary - 2500000) * 0.02 : 0;
    }
}
