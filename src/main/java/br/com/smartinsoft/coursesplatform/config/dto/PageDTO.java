package br.com.smartinsoft.coursesplatform.config.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@JsonPropertyOrder({
    "content",
    "size",
    "sorted",
    "currentPage",
    "totalPage",
    "totalElement",
})
public class PageDTO {

  private final Page<?> page;

  public PageDTO() {
    this.page = new PageImpl<>(Collections.emptyList(), Pageable.ofSize(1), 0);

  }

  public PageDTO(Page<?> page) {
    if(page.isEmpty()){
      page = new PageImpl<>(Collections.emptyList(), Pageable.ofSize(1), 0);
    }
    this.page = page;

  }

  public int getTotalPage(){
    return this.page.getTotalPages();
  }

  public int getSize() {
    return this.page.getSize();
  }

  public long getTotalElement() {
    return this.page.getTotalElements();
  }

  public int getCurrentPage() {
    return this.page.getPageable().getPageNumber();
  }

  public boolean isSorted() {
    return this.page.getPageable().getSort().isSorted();
  }

  public List<Object> getContent() {
    return (List<Object>) this.page.getContent();
  }
}
