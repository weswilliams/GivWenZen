package bdd.steps;

public class ExampleObject {
   String value;

   ExampleObject(String value) {
      this.value = value;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ExampleObject that = (ExampleObject) o;
      return !(value != null ? !value.equals(that.value) : that.value != null);
   }

   @Override
   public int hashCode() {
      return value != null ? value.hashCode() : 0;
   }
}
