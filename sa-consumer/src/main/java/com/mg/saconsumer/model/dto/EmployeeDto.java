package com.mg.saconsumer.model.dto;

import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class EmployeeDto {

  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String surname;
  private String patronymic;
  private String department;
  private String position;
  private String employmentDate;
  private String firedDate;
}
