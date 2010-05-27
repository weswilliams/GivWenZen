package bdd.parse;

import org.givwenzen.parse.*;

public class CustomObjectParser implements MethodParameterParser {
   @Override
   public boolean canParse(Class<?> paramerterType) {
      return paramerterType.equals(CustomObjectForParsing.class);
   }

   @Override
   public Object parse(Object paramerter, Class<?> paramerterType) throws Exception {
      return new CustomObjectForParsing(paramerter.toString());
   }
}
