package bdd.parse;

import org.givwenzen.parse.*;

public class TestCustomObjectParser implements MethodParameterParser {
   @Override
   public boolean canParse(Class<?> paramerterType) {
      return paramerterType.equals(TestCustomObjectForParsing.class);
   }

   @Override
   public Object parse(Object paramerter, Class<?> paramerterType) throws Exception {
      return new TestCustomObjectForParsing(paramerter.toString());
   }
}
