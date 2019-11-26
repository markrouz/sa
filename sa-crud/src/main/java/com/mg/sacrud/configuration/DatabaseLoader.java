package com.mg.sacrud.configuration;

import com.mg.sacrud.model.enums.Department;
import com.mg.sacrud.model.Employee;
import com.mg.sacrud.model.enums.Position;
import com.mg.sacrud.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
@Slf4j
@Profile("!test")
public class DatabaseLoader {

  private static final String LOAD_TO_DB_MESSAGE = "load employee to db: ";
  private static final int INITIAL_DB_COUNT = 12;

  private static final List<String> NAMES = List
      .of("Геннадий", "Арсений", "Алексей", "Джон", "Ибрагим", "Антон", "Вячеслав");
  private static final List<String> SURNAMES = List
      .of("Иванов", "Петров", "Сидров", "Медведев", "Розенбаум", "Обломов");
  private static final List<String> PATRONIMICS = List
      .of("Олегович", "Сергеевич", "Алексеевич", "Владимирович", "Константинович");

  @Bean
  CommandLineRunner initDatabase(EmployeeRepository employeeRepository) {
    return args -> {
      for (int i = 0; i < INITIAL_DB_COUNT; i++) {
        Department[] availableDepartments = Department.values();
        Position[] availablePositions = Position.values();
        log.info(LOAD_TO_DB_MESSAGE + employeeRepository.save(Employee.builder()
            .name(NAMES.get(ThreadLocalRandom.current().nextInt(NAMES.size())))
            .surname(SURNAMES.get(ThreadLocalRandom.current().nextInt(SURNAMES.size())))
            .patronymic(PATRONIMICS.get(ThreadLocalRandom.current().nextInt(PATRONIMICS.size())))
            .department(availableDepartments[ThreadLocalRandom.current()
                .nextInt(availableDepartments.length)])
            .position(
                availablePositions[ThreadLocalRandom.current().nextInt(availablePositions.length)])
            .employmentDate(LocalDate.of(ThreadLocalRandom.current().nextInt(1960, 2020),
                ThreadLocalRandom.current().nextInt(1, 13),
                ThreadLocalRandom.current().nextInt(1, 27)))
            .firedDate(ThreadLocalRandom.current().nextBoolean() ? LocalDate.now() : null)
            .build()));
      }
    };
  }

}
