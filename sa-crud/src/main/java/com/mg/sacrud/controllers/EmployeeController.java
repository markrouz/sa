package com.mg.sacrud.controllers;

import com.mg.sacrud.dto.EmployeeDto;
import com.mg.sacrud.exceptions.EmployeeNotFoundException;
import com.mg.sacrud.model.Employee;
import com.mg.sacrud.repository.EmployeeRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class EmployeeController {

  private final EmployeeRepository employeeRepository;
  private final ModelMapper modelMapper;

  EmployeeController(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
    this.employeeRepository = employeeRepository;
    this.modelMapper = modelMapper;
  }

  @GetMapping("/employees-fetch-all")
  public List<EmployeeDto> fetchAllEmployees() {
    return employeeRepository.findAll().stream().map(this::convertToDto)
        .collect(Collectors.toList());
  }

  @GetMapping("/employees")
  public Page<EmployeeDto> getEmployees(@RequestParam(defaultValue = "0") Integer pageNumber,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    Page<Employee> employeesPage = employeeRepository.findAll(PageRequest.of(pageNumber, pageSize));
    return employeesPage.map(this::convertToDto);
  }

  @PostMapping("/employees")
  public EmployeeDto addEmployee(@RequestBody @Valid EmployeeDto newEmployee) {
    Employee employee = convertToEntity(newEmployee);
    Employee employeeCreated = employeeRepository.save(employee);
    return convertToDto(employeeCreated);
  }

  @GetMapping("/employees/{id}")
  public EmployeeDto findEmployeeById(@PathVariable Long id) {
    return convertToDto(employeeRepository.findById(id)
        .orElseThrow(() -> new EmployeeNotFoundException(id)));
  }

  @PutMapping("/employees/{id}")
  public EmployeeDto updateEmployee(@RequestBody @Valid EmployeeDto employeeDto,
      @PathVariable Long id) {
    if (employeeRepository.existsById(id)) {
      Employee updatedEmployee = convertToEntity(employeeDto);
      updatedEmployee.setId(id);
      return convertToDto(employeeRepository.save(updatedEmployee));
    } else {
      throw new EmployeeNotFoundException(id);
    }
  }

  @DeleteMapping("/employees/{id}")
  public void deleteEmployee(@PathVariable Long id) {
    employeeRepository.deleteById(id);
  }

  private EmployeeDto convertToDto(Employee employee) {
    return modelMapper.map(employee, EmployeeDto.class);
  }

  private Employee convertToEntity(EmployeeDto employeeDto) {
    return modelMapper.map(employeeDto, Employee.class);
  }

}
