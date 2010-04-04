package bdd.steps;

import java.beans.*;

public class ExampleObjectEditor extends PropertyEditorSupport {
   @Override
   public void setAsText(String text) throws IllegalArgumentException {
      setValue(new ExampleObject(text));
   }
}
