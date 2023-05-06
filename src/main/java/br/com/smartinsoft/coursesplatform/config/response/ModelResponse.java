package br.com.smartinsoft.coursesplatform.config.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModelResponse {
  private String value;
  private String label;
  private List<String> categories;
}
