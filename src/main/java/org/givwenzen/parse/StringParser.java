package org.givwenzen.parse;

public class StringParser implements MethodParameterParser {
   public boolean canParse(Class paramType) {
      return String.class.equals(paramType);
   }

   public Object parse(Object param, Class paramType) {
      return param.toString();
   }
}
