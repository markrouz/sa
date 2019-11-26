package com.mg.saconsumer.model.dto;

import com.mg.saconsumer.model.enums.Department;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ReportDto {

  List<Department> mostPopularDepartments;
  Double avgWorkPeriod;
}
