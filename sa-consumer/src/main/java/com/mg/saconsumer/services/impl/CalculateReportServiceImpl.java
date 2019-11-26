package com.mg.saconsumer.services.impl;

import com.mg.saconsumer.model.WorkPeriod;
import com.mg.saconsumer.model.dto.EmployeeDto;
import com.mg.saconsumer.feign.RemoteServiceSaCrud;
import com.mg.saconsumer.model.enums.Department;
import com.mg.saconsumer.services.CalculateReportsService;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class CalculateReportServiceImpl implements CalculateReportsService {

  private final RemoteServiceSaCrud remoteServiceSaCrud;

  public CalculateReportServiceImpl(RemoteServiceSaCrud remoteServiceSaCrud) {
    this.remoteServiceSaCrud = remoteServiceSaCrud;
  }

  @Override
  public Double calculateAvgWorkPeriod() {
    List<EmployeeDto> employeeList = remoteServiceSaCrud.fetchAllEmployees();
    return employeeList.stream()
        .filter(employeeDto -> !StringUtils.isEmpty(employeeDto.getEmploymentDate()))
        .map(employeeDto -> {
          LocalDate firedDate = StringUtils.isEmpty(employeeDto.getFiredDate()) ? LocalDate.now()
              : LocalDate.parse(employeeDto.getFiredDate());
          return new WorkPeriod(LocalDate.parse(employeeDto.getEmploymentDate()), firedDate);
        })
        .mapToLong(WorkPeriod::getCountOfDays)
        .average()
        .orElse(0L);
  }

  @Override
  public List<Department> calculateMostPopularDepartments() {
    List<EmployeeDto> employeeList = remoteServiceSaCrud.fetchAllEmployees();

    Map<String, Long> departmentNameToCount = employeeList.stream()
        .filter(employeeDto -> !StringUtils.isEmpty(employeeDto.getDepartment()))
        .map(EmployeeDto::getDepartment)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    if (departmentNameToCount.isEmpty()) {
      return Collections.emptyList();
    }

    Long maxCount = departmentNameToCount.values().stream().max(Comparator.naturalOrder())
        .orElse(null);

    return departmentNameToCount.entrySet().stream()
        .filter(entry -> entry.getValue().equals(maxCount))
        .map(Map.Entry::getKey)
        .map(Department::valueOf)
        .collect(Collectors.toList());
  }
}
