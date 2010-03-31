package org.givwenzen.parse;

import java.lang.reflect.*;

public class ArrayParser implements MethodParameterParser {
   private MethodParameterParser propertyEditorParser;

   public ArrayParser(MethodParameterParser propertyEditorParser) {
      this.propertyEditorParser = propertyEditorParser;
   }

   @Override
   public boolean canParse(Class<?> paramType) {
      return paramType.isArray() && propertyEditorParser.canParse(paramType.getComponentType());
   }

   @Override
   public Object parse(Object param, Class<?> paramType) throws Exception {
      String[] values = param.toString().split(",");
      Object array = Array.newInstance(paramType.getComponentType(), values.length);
      for (int i = 0; i < values.length; i++) {
         Array.set(array, i, propertyEditorParser.parse(values[i], paramType.getComponentType()));
      }
      return array;
   }
}
