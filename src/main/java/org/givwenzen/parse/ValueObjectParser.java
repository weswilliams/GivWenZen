package org.givwenzen.parse;

import java.lang.reflect.Method;

public class ValueObjectParser  implements MethodParameterParser {
   public boolean canParse(Class<?> paramType) {
      try {
         return getParseMethod(paramType) != null;
      } catch (NoSuchMethodException e) {
         return false;
      }
   }

   public Object parse(Object param, Class<?> paramType) {
      try {
         return getParseMethod(paramType).invoke(paramType, param.toString());
      } catch (Exception e) {
         throw new RuntimeException("cound not call parse on " + paramType, e);
      }
   }

   private static Method getParseMethod(Class<?> paramType) throws NoSuchMethodException {
      return paramType.getMethod("parse", String.class);
   }

}
