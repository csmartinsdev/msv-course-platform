package br.com.smartinsoft.coursesplatform.config.component;

import br.com.smartinsoft.coursesplatform.exception.BusinessException;
import java.lang.reflect.InvocationTargetException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeanUtilsNotNull extends BeanUtilsBean {

  @Override
  public void copyProperty(Object dest, String name, Object value) throws IllegalAccessException, InvocationTargetException {
    if (value == null) return;

    super.copyProperty(dest, name, value);
  }

  public void copy(Object target, Object source) {
    try{
      BeanUtilsBean notNull = new BeanUtilsNotNull();
      notNull.copyProperties(target, source);
    }catch (Exception e){
      log.error("error:", e);
      throw new BusinessException(e.getMessage());
    }
  }


  @Override
  protected Object convert(Object value, Class clazz) {
    if (clazz.isEnum()) {
      String vStr = (String) value;
      return Enum.valueOf(clazz, vStr);
    } else {
      return super.convert(value, clazz);
    }
  }
}
