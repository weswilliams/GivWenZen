package org.givwenzen.parse;

public interface MethodParameterParser {
   boolean canParse(Class paramType);

   Object parse(Object param, Class paramType) throws Exception;
}
