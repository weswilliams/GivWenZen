package bdd.steps;

import org.givwenzen.*;
import org.givwenzen.annotations.*;

import java.util.*;

@DomainSteps
public class ExampleSteps {
   private static final String SOME_NUMBER = "(\\d+)";
   private GivWenZen givWenZen;
   private List<Integer> numbers = new ArrayList<Integer>();
   private Integer total;
   private String[] values;

   public ExampleSteps(GivWenZen givWenZen) {
      this.givWenZen = givWenZen;
   }

   @DomainStep("i turn on the calculator")
   public void reset() {
      numbers.clear();
   }

   @DomainStep("i have entered " + SOME_NUMBER + " into the calculator")
   public void enterNumber(int number) throws Exception {
      numbers.add(number);
   }

   @DomainStep("i press add")
   public void addNumbers() {
      total = 0;
      for (Integer number : numbers) {
         total += number;
      }
   }

   @DomainStep("the total is " + SOME_NUMBER)
   public boolean theTotalIs(int exepectedTotal) throws Exception {
      // simple example calling another step
      return givWenZen.then("what is the total").equals(exepectedTotal);
   }

   @DomainStep("what is the total")
   public Integer getTotal() {
      return total;
   }

   @DomainStep("an array parameter method (.*)")
   public void arrayParameterMethod(String[] values) {
      this.values = values;
   }

   @DomainStep("array " + SOME_NUMBER + " has (.*)")
   public boolean verifyArrayParameterMethod(int index, String value) {
      System.out.println("value[" + index + "]=" + values[index]);
      return values[index].equals(value);
   }
}