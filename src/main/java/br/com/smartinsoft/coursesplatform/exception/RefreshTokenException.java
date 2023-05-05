package br.com.smartinsoft.coursesplatform.exception;

public class RefreshTokenException extends RuntimeException{
  private static final long serialVersionUID = -7704360949329303583L;

  public RefreshTokenException(String message) {
    super(message);
  }

}
