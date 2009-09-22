package org.givwenzen.annotations;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertSame;

import org.givwenzen.annotations.MarkedClass;
import org.junit.Test;

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