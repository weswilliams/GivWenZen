package org.givwenzen.parse;

import org.junit.*;
import static org.fest.assertions.Assertions.*;

public class ArrayParserTest {
   private ArrayParser parser;

   @Before
   public void before() {
      parser = new ArrayParser(new PropertyEditorParser());
   }

   @Test
   public void shouldHandleMethodParameterThatIsNativeArray() throws Exception {
      assertThat(parser.canParse((new int[0]).getClass()));
   }

   @Test
   public void shouldHandleMethodParameterThatIsObjectArray() throws Exception {
      assertThat(parser.canParse((new Object[0]).getClass()));
   }

   @Test
   public void shouldParseObjectArrayString() throws Exception {
      String arrayString = "1,2,3";
      String[] values = (String[]) parser.parse(arrayString, (new String[0]).getClass());
      assertThat(values.length).isEqualTo(3);
      assertThat(values[0]).isEqualTo("1");
      assertThat(values[1]).isEqualTo("2");
      assertThat(values[2]).isEqualTo("3");
   }

//   @Test TODO
//   public void shouldParseNativeArrayString() throws Exception {
//      String arrayString = "1,2,3";
//      int[] values = (int[]) parser.parse(arrayString, (new int[0]).getClass());
//      assertThat(values.length).isEqualTo(3);
//      assertThat(values[0]).isEqualTo(1);
//      assertThat(values[1]).isEqualTo(2);
//      assertThat(values[2]).isEqualTo(3);
//   }

   class ForArrayParserTest {
      public void nativeArrayMethod(int[] integers) {

      }
   }
}
