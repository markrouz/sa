package com.mg.saconsumer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mg.saconsumer.model.dto.EmployeeDto;
import com.mg.saconsumer.model.enums.Department;
import com.mg.saconsumer.model.enums.Position;
import com.mg.saconsumer.feign.RemoteServiceSaCrud;
import com.mg.saconsumer.services.CalculateReportsService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class CalculateReportServiceTest {

  @Autowired
  private CalculateReportsService calculateReportsService;

  @MockBean
  private RemoteServiceSaCrud remoteServiceSaCrud;

  @Test
  public void calculateAvgWorkPeriodOnEmptyList() {
    Mockito.when(remoteServiceSaCrud.fetchAllEmployees()).thenReturn(Collections.emptyList());
    assertEquals(0L, calculateReportsService.calculateAvgWorkPeriod());
  }

  @Test
  public void calculateAvgWorkPeriodSimpleCaseTest() {
    Mockito.when(remoteServiceSaCrud.fetchAllEmployees()).thenReturn(Arrays.asList(
        EmployeeDto.builder()
            .name("fsdf").surname("fewfw")
            .patronymic("ewfwef")
            .department(Department.IT.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build(),
        EmployeeDto.builder()
            .name("asdf").surname("sfdfsd")
            .patronymic("fdsfsd")
            .department(Department.IT.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build()));

    assertEquals(2.0, calculateReportsService.calculateAvgWorkPeriod());
  }

  @Test
  public void calculateMostPopularDepartmentsOnEmptyList() {
    Mockito.when(remoteServiceSaCrud.fetchAllEmployees()).thenReturn(Collections.emptyList());
    List<Department> mostPopularDepartments = calculateReportsService
        .calculateMostPopularDepartments();
    assertNotNull(mostPopularDepartments);
    assertEquals(0, mostPopularDepartments.size());
  }

  @Test
  public void calculateMostPopularDepartmentOneDepartmentTest() {
    Mockito.when(remoteServiceSaCrud.fetchAllEmployees()).thenReturn(Arrays.asList(
        EmployeeDto.builder()
            .name("dsada").surname("vdvf")
            .patronymic("dfgdsfg")
            .department(Department.IT.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build(),
        EmployeeDto.builder()
            .name("tytr").surname("hhjk")
            .patronymic("hfrth")
            .department(Department.IT.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build(),
        EmployeeDto.builder()
            .name("fdsf").surname("fsdafw")
            .patronymic("yturt")
            .department(Department.SALES.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build()));

    List<Department> mostPopularDepartments = calculateReportsService
        .calculateMostPopularDepartments();
    assertNotNull(mostPopularDepartments);
    assertEquals(1, mostPopularDepartments.size());
    assertEquals(Department.IT, mostPopularDepartments.get(0));
  }

  @Test
  public void calculateMostPopularDepartmentsListDepartmentsTest() {
    Mockito.when(remoteServiceSaCrud.fetchAllEmployees()).thenReturn(Arrays.asList(
        EmployeeDto.builder()
            .name("Алексей").surname("Фролов")
            .patronymic("Леонидович")
            .department(Department.IT.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build(),
        EmployeeDto.builder()
            .name("Алексей").surname("Фролов")
            .patronymic("Леонидович")
            .department(Department.IT.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build(),
        EmployeeDto.builder()
            .name("fdsf").surname("iuyouy")
            .patronymic("weqwg")
            .department(Department.SALES.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build(),
        EmployeeDto.builder()
            .name("fdsf").surname("sdfsa")
            .patronymic("sdfsa")
            .department(Department.SALES.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build(),
        EmployeeDto.builder()
            .name("khjkhj").surname("wqeq")
            .patronymic("vasva")
            .department(Department.FINANCE.name())
            .position(Position.ANALYST.name())
            .employmentDate(LocalDate.now().minusDays(2).toString())
            .firedDate(LocalDate.now().toString())
            .build()));
    List<Department> mostPopularDepartments = calculateReportsService
        .calculateMostPopularDepartments();
    assertNotNull(mostPopularDepartments);
    assertEquals(2, mostPopularDepartments.size());
    assertTrue(mostPopularDepartments.contains(Department.SALES));
    assertTrue(mostPopularDepartments.contains(Department.IT));
    assertFalse(mostPopularDepartments.contains(Department.FINANCE));
  }
}
