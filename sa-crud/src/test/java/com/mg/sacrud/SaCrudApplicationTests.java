package com.mg.sacrud;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mg.sacrud.dto.EmployeeDto;
import com.mg.sacrud.model.Employee;
import com.mg.sacrud.model.enums.Department;
import com.mg.sacrud.model.enums.Position;
import com.mg.sacrud.repository.EmployeeRepository;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

// integration tests for crud api
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class SaCrudApplicationTests {

  static {
    System.setProperty("eureka.client.enabled", "false");
    System.setProperty("spring.cloud.config.failFast", "false");
  }

  private static final String ENDPOINT_NAME = "/employees";

  @Autowired
  private EmployeeRepository employeeRepository;

  @Autowired
  private ModelMapper modelMapper;

  @Autowired
  private TestRestTemplate testRestTemplate;

  private List<Employee> initialEmployees;

  @BeforeEach
  public void initTestDb() {
    initialEmployees = new ArrayList<>();
    initialEmployees.add(employeeRepository.save(Employee.builder()
        .name("Алексей").surname("Фролов")
        .patronymic("Леонидович")
        .department(Department.IT)
        .position(Position.ANALYST)
        .employmentDate(LocalDate.of(2017, 3, 21))
        .firedDate(LocalDate.now())
        .build()));
    initialEmployees.add(employeeRepository.save(Employee.builder()
        .name("Андрей").surname("Иванов")
        .patronymic("Иванович")
        .department(Department.SALES)
        .position(Position.MANAGER)
        .employmentDate(LocalDate.of(2017, 3, 21))
        .firedDate(LocalDate.now())
        .build()));
    initialEmployees.add(employeeRepository.save(Employee.builder()
        .name("Геннадий").surname("Корнеев")
        .patronymic("Вячеславович")
        .department(Department.IT)
        .position(Position.DEVELOPER)
        .build()));
  }

  @AfterEach
  public void clearTestDb() {
    employeeRepository.deleteAll();
  }

  @Test
  public void getAllEmployeesIntegrationTest() {
    ParameterizedTypeReference<TestPageImpl<EmployeeDto>> responseType = new ParameterizedTypeReference<>() {
    };

    ResponseEntity<TestPageImpl<EmployeeDto>> response = testRestTemplate
        .exchange(ENDPOINT_NAME, HttpMethod.GET, null, responseType);
    Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    Page<EmployeeDto> responseBody = response.getBody();

    assertNotNull(responseBody);
    assertEquals(initialEmployees.size(), responseBody.getTotalElements());

    List<EmployeeDto> employeeDtoList = responseBody.getContent();
    assertNotNull(employeeDtoList);

    List<Employee> employees = employeeDtoList.stream()
        .map(employeeDto -> modelMapper.map(employeeDto, Employee.class))
        .collect(Collectors.toList());
    employees.forEach(employee -> assertTrue(initialEmployees.contains(employee)));
  }

  @Test
  public void addEmployeeIntegrationTest() {

    EmployeeDto employeeDto = EmployeeDto.builder()
        .name("Алексей").surname("Радостный")
        .patronymic("Леонидович")
        .department("IT")
        .position("ANALYST")
        .employmentDate("2017-12-03")
        .firedDate("2019-01-02").build();

    ResponseEntity<EmployeeDto> response = testRestTemplate
        .postForEntity(ENDPOINT_NAME, employeeDto, EmployeeDto.class);
    Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    EmployeeDto addedEmployee = response.getBody();
    assertNotNull(addedEmployee);

    employeeDto.setId(addedEmployee.getId());
    assertEquals(employeeDto, addedEmployee);

    assertEquals(initialEmployees.size() + 1, employeeRepository.count());
    assertTrue(employeeRepository.findBySurname(employeeDto.getSurname())
        .contains(modelMapper.map(employeeDto, Employee.class)));
  }

  @Test
  public void addIncorrectEmployeeIntegrationTest() {
    EmployeeDto incorrectEmployeeDto = EmployeeDto.builder()
        .name("").surname("Радостный")
        .patronymic("Леонидович")
        .department("IT")
        .position("ANALYST")
        .employmentDate("2017-12-03")
        .firedDate("2019-01-02").build();

    ResponseEntity response = testRestTemplate
        .postForEntity(ENDPOINT_NAME, incorrectEmployeeDto, EmployeeDto.class);
    Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    assertEquals(initialEmployees.size(), employeeRepository.count());
    assertFalse(employeeRepository.findBySurname(incorrectEmployeeDto.getSurname())
        .contains(modelMapper.map(incorrectEmployeeDto, Employee.class)));
  }

  @Test
  public void findEmployeeByIdIntegrationTest() {
    final Long id = initialEmployees.get(0).getId();
    ResponseEntity<EmployeeDto> response = testRestTemplate
        .getForEntity(ENDPOINT_NAME + "/{id}", EmployeeDto.class, id);
    Assert.assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));

    EmployeeDto findingEmployeeDto = response.getBody();
    assertNotNull(findingEmployeeDto);
    assertEquals(id, findingEmployeeDto.getId());
  }

  @Test
  public void findEmployeeByIdThrowsExceptionIntegrationTest() {
    assertThrows(RestClientException.class, () -> {
      testRestTemplate
          .getForEntity(ENDPOINT_NAME + "/{id}", EmployeeDto.class, Integer.MAX_VALUE);
    });
  }

  @Test
  public void updateEmployeeIntegrationTest() {
    final Long updatedEmployeeId = initialEmployees.get(0).getId();
    EmployeeDto employeeDto = EmployeeDto.builder()
        .name("Алексей").surname("Перешедний")
        .patronymic("Леонидович")
        .department("IT")
        .position("ANALYST")
        .employmentDate("2017-12-03")
        .firedDate("2019-01-02").build();
    testRestTemplate.put(ENDPOINT_NAME + "/{id}", employeeDto, updatedEmployeeId);

    assertEquals(initialEmployees.size(), employeeRepository.count());

    Employee updatedEmployee = employeeRepository.findById(updatedEmployeeId).orElseThrow();
    employeeDto.setId(updatedEmployee.getId());
    assertEquals(employeeDto, modelMapper.map(updatedEmployee, EmployeeDto.class));
  }

  @Test
  public void updateEmployeeWithIncorrectData() {
    Employee employeeForUpdate = initialEmployees.get(0);
    final Long updatedEmployeeId = employeeForUpdate.getId();
    EmployeeDto employeeDto = EmployeeDto.builder()
        .name("").surname("Перешедний")
        .patronymic("Леонидович")
        .department("IT")
        .position("ANALYST")
        .employmentDate("2017-12-03")
        .firedDate("2019-01-02").build();
    testRestTemplate.put(ENDPOINT_NAME + "/{id}", employeeDto, updatedEmployeeId);

    assertEquals(initialEmployees.size(), employeeRepository.count());

    Employee notUpdatedEmployee = employeeRepository.findById(updatedEmployeeId).orElseThrow();
    assertEquals(employeeForUpdate.getName(), notUpdatedEmployee.getName());
  }

  @Test
  public void deleteEmployeeIntegrationTest() {
    final Long deletedEmployeeId = initialEmployees.get(0).getId();
    testRestTemplate.delete(ENDPOINT_NAME + "/{id}", deletedEmployeeId);
    assertEquals(initialEmployees.size() - 1, employeeRepository.count());
    assertThrows(NoSuchElementException.class,
        () -> employeeRepository.findById(deletedEmployeeId).orElseThrow());
  }

}
