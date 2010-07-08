package org.givwenzen.parse;

import org.givwenzen.CustomParserFinder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringToObjectParserTest {

    @Test
    public void shouldFindAdditionalMethodParameterParsersInBDDDotParse() throws Exception {
        String[] value = {"x"};
        Class<?>[] type = {CustomObjectForParsing.class};
        Object object = new StringToObjectParser(new CustomParserFinder()).convertParamertersToTypes(value, type)[0];
        assertEquals(new CustomObjectForParsing("x"), object);
    }
}
