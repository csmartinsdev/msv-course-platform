package br.com.smartinsoft.coursesplatform.config.validators;

import br.com.caelum.stella.validation.CPFValidator;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class CpfValidator implements ConstraintValidator<CpfValidation, String> {

  @Override
  public boolean isValid(String cpf, ConstraintValidatorContext context) {
    if (!StringUtils.isBlank(cpf)) {
      return cpf.length() == 11 && !new CPFValidator().invalidMessagesFor(cpf).isEmpty()
          ? Boolean.FALSE
          : Boolean.TRUE;
    }
    return Boolean.TRUE;
  }
}
