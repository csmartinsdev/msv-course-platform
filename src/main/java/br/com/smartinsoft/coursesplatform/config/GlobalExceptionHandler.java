package br.com.smartinsoft.coursesplatform.config;

import br.com.smartinsoft.coursesplatform.config.component.MessageProperty;
import br.com.smartinsoft.coursesplatform.config.dto.ErrorValidationDTO;
import br.com.smartinsoft.coursesplatform.config.response.ExceptionResponse;
import br.com.smartinsoft.coursesplatform.exception.BusinessErrorsException;
import br.com.smartinsoft.coursesplatform.exception.BusinessException;
import br.com.smartinsoft.coursesplatform.exception.RefreshTokenException;
import br.com.smartinsoft.coursesplatform.exception.ResourceNotFoundException;
import br.com.smartinsoft.coursesplatform.exception.ServiceUnavailableException;
import br.com.smartinsoft.coursesplatform.exception.UserNotAuthorizedException;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import java.lang.reflect.Field;
import java.net.ConnectException;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
  @Autowired
  private MessageProperty messageProperty;

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Object> handleResourceNotFound(ResourceNotFoundException e) {
    return new ResponseEntity<>(ExceptionResponse
        .builder()
        .message(e.getMessage())
        .code(HttpStatus.NOT_FOUND.value())
        .build(), HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler({
      BusinessException.class,
      MissingRequestHeaderException.class,
      HttpRequestMethodNotSupportedException.class,
      HttpMessageNotReadableException.class
  })
  public ResponseEntity<Object> handleNotValid(RuntimeException e) {
    return new ResponseEntity<>(ExceptionResponse
        .builder()
        .message(e.getMessage())
        .code(HttpStatus.BAD_REQUEST.value())
        .build(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BusinessErrorsException.class)
  public ResponseEntity<Object> handleBusinessErrors(BusinessErrorsException e) {
    return ResponseEntity.badRequest().body(e.getErrors());
  }

  @ResponseStatus(code = HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public List<ErrorValidationDTO> handleValidationField(MethodArgumentNotValidException e) {
    return e.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(field -> new ErrorValidationDTO(HttpStatus.BAD_REQUEST.value(),
              getJsonFieldName(field),
              field.getDefaultMessage()))
        .collect(Collectors.toList());
  }

  @ExceptionHandler(ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public List<ErrorValidationDTO> handleValidationException(ConstraintViolationException exception){
    return exception.getConstraintViolations()
        .stream()
        .map(field -> new ErrorValidationDTO(HttpStatus.BAD_REQUEST.value(),
            ((PathImpl)field.getPropertyPath()).getLeafNode().getName(),
            field.getMessage()))
        .collect(Collectors.toList());
  }

  @ExceptionHandler({
      HttpClientErrorException.class,
      UserNotAuthorizedException.class,
      MalformedJwtException.class,
      ExpiredJwtException.class,
      SignatureException.class
  })
  public ResponseEntity<Object> handleNotAuthorized(RuntimeException e){
    return new ResponseEntity<>(ExceptionResponse.builder()
        .message(messageProperty.getProperty("error.notAuthorized",
                messageProperty.getProperty("user")))
        .code(HttpStatus.UNAUTHORIZED.value())
        .build(), HttpStatus.UNAUTHORIZED);

  }

  @ExceptionHandler({RefreshTokenException.class, AccessDeniedException.class})
  public ResponseEntity<Object> handleTokenRefreshException(RuntimeException ex, WebRequest request) {
    return new ResponseEntity<>(ExceptionResponse.builder()
        .message(ex.getMessage())
        .code(HttpStatus.FORBIDDEN.value())
        .build(), HttpStatus.FORBIDDEN);
  }

  @ExceptionHandler({ConnectException.class})
  public ResponseEntity<Object> handleBadGateway(RuntimeException e){
    return new ResponseEntity<>(ExceptionResponse.builder()
        .message(messageProperty.getProperty("error.badGateway"))
        .code(HttpStatus.BAD_GATEWAY.value())
        .build(), HttpStatus.BAD_GATEWAY);
  }

  @ExceptionHandler({ServiceUnavailableException.class})
  public ResponseEntity<Object> handleServiceUnavailable(RuntimeException e){
    return new ResponseEntity<>(ExceptionResponse.builder()
        .message(e.getMessage())
        .code(HttpStatus.SERVICE_UNAVAILABLE.value())
        .build(), HttpStatus.SERVICE_UNAVAILABLE);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Object> handleExceptionGeneral(Exception e){
    log.error("error:", e);
    return new ResponseEntity<>(ExceptionResponse.builder()
        .message(messageProperty.getProperty("error.internalServerError"))
        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * Try to get the @JsonProperty annotation value from the field. If not present then the
   * fieldError.getField() is returned.
   * @param fieldError
   * @return fieldName
   */
  private String getJsonFieldName(final FieldError fieldError) {
    try {
      final Field violation = fieldError.getClass().getDeclaredField("violation");
      violation.setAccessible(true); //NOSONAR

      var constraintViolation = (ConstraintViolation) violation.get(fieldError);
      final Field declaredField = constraintViolation.getRootBeanClass()
          .getDeclaredField(fieldError.getField());

      final JsonProperty annotation = declaredField.getAnnotation(JsonProperty.class);

      //Check if JsonProperty annotation is present and if value is set
      if (annotation != null && annotation.value() != null && !annotation.value().isEmpty()) {
        return annotation.value();
      } else {
        return fieldError.getField();
      }

    } catch (Exception e) {
      return fieldError.getField();
    }
  }
}
