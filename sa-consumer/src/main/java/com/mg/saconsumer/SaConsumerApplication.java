package com.mg.saconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SaConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(SaConsumerApplication.class, args);
  }

}
