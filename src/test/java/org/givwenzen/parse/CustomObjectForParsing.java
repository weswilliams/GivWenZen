package org.givwenzen.parse;

public class CustomObjectForParsing {
   private String value;

   public CustomObjectForParsing(String value) {
      this.value = value;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      CustomObjectForParsing that = (CustomObjectForParsing) o;
      return !(value != null ? !value.equals(that.value) : that.value != null);
   }

   @Override
   public int hashCode() {
      return value != null ? value.hashCode() : 0;
   }
}
