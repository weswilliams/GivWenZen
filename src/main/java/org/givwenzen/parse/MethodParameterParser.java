package org.givwenzen.parse;

public interface MethodParameterParser {
  boolean canParse(Class<?> paramerterType);

  Object parse(Object paramerter, Class<?> paramerterType) throws Exception;
}
