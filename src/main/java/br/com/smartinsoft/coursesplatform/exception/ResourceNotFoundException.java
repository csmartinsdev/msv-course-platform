package br.com.smartinsoft.coursesplatform.exception;

public class ResourceNotFoundException extends RuntimeException {

  public ResourceNotFoundException() { }

  public ResourceNotFoundException(String message) {
    super(message);
  }
}
