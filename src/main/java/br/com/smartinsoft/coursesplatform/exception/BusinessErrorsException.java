package br.com.smartinsoft.coursesplatform.exception;

import br.com.smartinsoft.coursesplatform.config.dto.ErrorValidationDTO;
import java.util.List;

public class BusinessErrorsException extends RuntimeException {
  private final transient List<ErrorValidationDTO> errors;

  public BusinessErrorsException(List<ErrorValidationDTO> errors) {
    this.errors = errors;
  }

  public List<ErrorValidationDTO> getErrors() {
    return errors;
  }
}
