package com.mg.sacrud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeDto {

  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String surname;
  private String patronymic;
  private String department;
  private String position;
  // todo дата увольнения не должна быть раньше даты принятия на работу
  private String employmentDate;
  private String firedDate;
}
