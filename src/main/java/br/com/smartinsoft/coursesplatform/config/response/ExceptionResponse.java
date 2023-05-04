package br.com.smartinsoft.coursesplatform.config.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "message",
    "code"
})
public class ExceptionResponse {

  @JsonProperty("message")
  private String message;

  @JsonProperty("code")
  private Integer code;

}
