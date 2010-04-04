package org.givwenzen.parse;

import java.lang.reflect.Method;

public class ValueObjectParser  implements MethodParameterParser {
   public boolean canParse(Class<?> paramerterType) {
      try {
         return getParseMethod(paramerterType) != null;
      } catch (NoSuchMethodException e) {
         return false;
      }
   }

   public Object parse(Object paramerter, Class<?> paramerterType) {
      try {
         return getParseMethod(paramerterType).invoke(paramerterType, paramerter.toString());
      } catch (Exception e) {
         throw new RuntimeException("cound not call parse on " + paramerterType, e);
      }
   }

   private static Method getParseMethod(Class<?> paramType) throws NoSuchMethodException {
      return paramType.getMethod("parse", String.class);
   }

}
