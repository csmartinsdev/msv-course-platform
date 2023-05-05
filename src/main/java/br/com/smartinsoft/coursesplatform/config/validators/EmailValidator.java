package br.com.smartinsoft.coursesplatform.config.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {

  @Override
  public boolean isValid(String email, ConstraintValidatorContext context) {
    if (!StringUtils.isBlank(email)) {
      return org.apache.commons.validator.routines.EmailValidator.getInstance().isValid(email);
    }
    return Boolean.TRUE;
  }
}
