package com.mg.sacrud.repository;

import com.mg.sacrud.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

  List<Employee> findBySurname(String surname);
}
