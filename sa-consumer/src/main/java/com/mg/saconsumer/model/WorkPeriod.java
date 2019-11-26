package com.mg.saconsumer.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class WorkPeriod {

  private LocalDate beginDate;
  private LocalDate endDate;

  public Long getCountOfDays() {
    if (Objects.isNull(beginDate) || Objects.isNull(endDate)) {
      return 0L;
    }
    return DAYS.between(beginDate, endDate);
  }

}
