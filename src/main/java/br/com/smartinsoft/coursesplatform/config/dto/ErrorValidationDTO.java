package br.com.smartinsoft.coursesplatform.config.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorValidationDTO {
  private Integer code;
  private String field;
  private String message;
}
