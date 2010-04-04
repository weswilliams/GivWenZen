package org.givwenzen.parse;

public class StringParser implements MethodParameterParser {
   public boolean canParse(Class<?> paramerterType) {
      return String.class.equals(paramerterType);
   }

   public Object parse(Object paramerter, Class<?> paramerterType) {
      return paramerter.toString();
   }
}
