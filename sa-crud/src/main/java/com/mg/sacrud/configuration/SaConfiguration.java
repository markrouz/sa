package com.mg.sacrud.configuration;

import org.modelmapper.AbstractConverter;
import org.modelmapper.AbstractProvider;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Configuration
public class SaConfiguration {

  @Bean
  public ModelMapper modelMapper() {

    ModelMapper modelMapper = new ModelMapper();

    Provider<LocalDate> localDateProvider = new AbstractProvider<>() {
      @Override
      public LocalDate get() {
        return LocalDate.now();
      }
    };

    Converter<String, LocalDate> toStringDate = new AbstractConverter<>() {
      @Override
      protected LocalDate convert(String source) {
        if (StringUtils.isEmpty(source)) {
          return null;
        }
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(source, format);
      }
    };

    modelMapper.createTypeMap(String.class, LocalDate.class);
    modelMapper.addConverter(toStringDate);
    modelMapper.getTypeMap(String.class, LocalDate.class).setProvider(localDateProvider);

    return modelMapper;
  }

}
