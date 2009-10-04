package bdd.steps;

import java.beans.PropertyEditorSupport;

public class CustomTypeEditor extends PropertyEditorSupport {

  
  @Override
  public String getAsText() {
    Object value = super.getValue();
    return value == null ? null : value.toString();
  }

  @Override
  public void setAsText(String arg0) throws IllegalArgumentException {
    setValue(new CustomType(arg0));
  }
  
}
