package org.givwenzen.parse;

import java.lang.reflect.*;

public class ArrayParser implements MethodParameterParser {
   private MethodParameterParser propertyEditorParser;

   public ArrayParser(MethodParameterParser propertyEditorParser) {
      this.propertyEditorParser = propertyEditorParser;
   }

   @Override
   public boolean canParse(Class<?> paramerterType) {
      return paramerterType.isArray() && propertyEditorParser.canParse(paramerterType.getComponentType());
   }

   @Override
   public Object parse(Object paramerter, Class<?> paramerterType) throws Exception {
      String[] values = paramerter.toString().split(",");
      Object array = Array.newInstance(paramerterType.getComponentType(), values.length);
      for (int i = 0; i < values.length; i++) {
         Array.set(array, i, propertyEditorParser.parse(values[i].trim(), paramerterType.getComponentType()));
      }
      return array;
   }
}
