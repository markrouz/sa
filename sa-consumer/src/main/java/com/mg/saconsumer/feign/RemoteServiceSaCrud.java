package com.mg.saconsumer.feign;

import com.mg.saconsumer.model.dto.EmployeeDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient("sa-crud")
public interface RemoteServiceSaCrud {

  @GetMapping("/employees-fetch-all")
  List<EmployeeDto> fetchAllEmployees();
}
