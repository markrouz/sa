package com.mg.saconsumer.services;

import com.mg.saconsumer.model.enums.Department;
import java.util.List;

public interface CalculateReportsService {

  Double calculateAvgWorkPeriod();
  List<Department> calculateMostPopularDepartments();
}
