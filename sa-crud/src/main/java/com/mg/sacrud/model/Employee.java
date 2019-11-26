package com.mg.sacrud.model;

import com.mg.sacrud.model.enums.Department;
import com.mg.sacrud.model.enums.Position;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String surname;
  private String patronymic;
  private Department department;
  private Position position;
  // todo валидация на дату увольнения
  private LocalDate employmentDate;
  private LocalDate firedDate;
}
