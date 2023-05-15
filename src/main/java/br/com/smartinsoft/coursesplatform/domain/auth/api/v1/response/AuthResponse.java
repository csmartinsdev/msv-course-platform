package br.com.smartinsoft.coursesplatform.domain.auth.api.v1.response;

import lombok.Getter;

@Getter
public class AuthResponse {
  private final String accessTokenType;
  private final String accessToken;
  private final String refreshToken;

  public AuthResponse(String accessToken, String refreshToken) {
    this.accessTokenType = "Bearer";
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

  @Override
  public String toString() {
    return "AuthResponse{" +
        "accessTokenType='" + accessTokenType + '\'' +
        ", accessToken='" + accessToken + '\'' +
        ", refreshToken='" + refreshToken + '\'' +
        '}';
  }
}
