package org.givwenzen.parse;

import static org.junit.Assert.*;

import org.junit.*;

public class StringToObjectParserTest {

   @Test
   public void shouldFindAdditionalMethodParameterParsersInBDDDotParse() throws Exception {
      String[] value = {"x"};
      Class<?>[] type = {TestCustomObjectForParsing.class};
      Object object = new StringToObjectParser().convertParamertersToTypes(value, type)[0];
      assertEquals(new TestCustomObjectForParsing("x"), object);
   }
}
