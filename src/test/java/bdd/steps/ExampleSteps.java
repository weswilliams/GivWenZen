package bdd.steps;


import java.util.ArrayList;
import java.util.List;

import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;

@DomainSteps
public class ExampleSteps {
   private List<Integer> numbers = new ArrayList<Integer>();
   private Integer total;
   private static final String SOME_NUMBER = "(\\d+)";

   @DomainStep("i have entered " + SOME_NUMBER + " into the calculator")
   public void enterNumber(int number) {
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
   public boolean theTotalIs(int exepectedTotal) {
      return exepectedTotal == total;
   }

   @DomainStep("what is the total")
   public Integer getTotal() {
      return total;
   }
}
