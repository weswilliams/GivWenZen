package org.givwenzen;

import bdd.parse.CustomObjectParser;
import org.givwenzen.parse.MethodParameterParser;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Author: Szczepan Faber
 */
public class CustomParserFinderTest {

    CustomParserFinder finder = new CustomParserFinder();

    @Test
    public void should() throws Exception {
        //given
        List<MethodParameterParser> parsers = new LinkedList<MethodParameterParser>();

        //when
        finder.addCustomParsers(parsers);

        //then
        assertThat(parsers).hasSize(1);
        assertThat(parsers.get(0)).isInstanceOf(CustomObjectParser.class);
    }
}
