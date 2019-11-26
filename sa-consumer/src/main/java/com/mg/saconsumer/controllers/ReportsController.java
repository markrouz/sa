package com.mg.saconsumer.controllers;

import com.mg.saconsumer.model.dto.ReportDto;
import com.mg.saconsumer.services.CalculateReportsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
public class ReportsController {

  private final CalculateReportsService calculateReportsService;

  public ReportsController(CalculateReportsService calculateReportsService) {
    this.calculateReportsService = calculateReportsService;
  }

  @GetMapping("/report")
  public ReportDto getReportAboutEmployees() {
    return ReportDto.builder().avgWorkPeriod(calculateReportsService.calculateAvgWorkPeriod())
        .mostPopularDepartments(calculateReportsService.calculateMostPopularDepartments()).build();
  }
}
