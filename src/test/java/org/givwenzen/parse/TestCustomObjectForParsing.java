package org.givwenzen.parse;

public class TestCustomObjectForParsing {
   private String value;

   public TestCustomObjectForParsing(String value) {
      this.value = value;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      TestCustomObjectForParsing that = (TestCustomObjectForParsing) o;
      return !(value != null ? !value.equals(that.value) : that.value != null);
   }

   @Override
   public int hashCode() {
      return value != null ? value.hashCode() : 0;
   }
}
