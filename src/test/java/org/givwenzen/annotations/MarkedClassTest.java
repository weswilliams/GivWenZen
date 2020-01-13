package org.givwenzen.annotations;

import org.junit.Test;

import static org.junit.Assert.*;

public class MarkedClassTest {

   @Test
   public void testEquals() throws Exception {
      MarkedClass stringMarkedClass1 = new MarkedClass(String.class);
      MarkedClass stringMarkedClass2 = new MarkedClass(String.class);
      MarkedClass integerMarkedClass = new MarkedClass(Integer.class);
      assertEquals(stringMarkedClass1, stringMarkedClass2);
      assertFalse(stringMarkedClass1.equals(integerMarkedClass));
      assertFalse(stringMarkedClass1.equals(String.class));

      assertSame(String.class, stringMarkedClass1.getTargetClass());
      assertEquals(String.class.getName(), stringMarkedClass1.getName());
   }
}