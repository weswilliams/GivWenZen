package org.givwenzen.annotations;

import junit.framework.TestCase;

import java.util.Set;

import org.givwenzen.annotations.MarkedClass;
import org.givwenzen.annotations.MarkedClassFinder;
import org.givwenzen.annotations.left.MarkedClassA;
import org.givwenzen.annotations.middle.MarkedClassX;
import org.givwenzen.annotations.middle.sub.MarkedClassY;
import org.givwenzen.annotations.right.MarkedClassB;

public class MarkedClassFinderTest extends TestCase {

   @Override
   protected void setUp() throws Exception {
      super.setUp();
   }

   public void testFindClassWithMarkerAnnotationWithNoType() throws Exception {
      MarkedClassFinder markedClassFinder = new MarkedClassFinder(TestMarker.class, "org.givwenzen.annotations.");
      assertTrue(markedClassFinder.findMarkedClasses().contains(new MarkedClass(MyMarkedClass.class)));
   }

   public void testSubpackagesNotSpecifiedAreStillSearched() throws Exception {
      MarkedClassFinder markedClassFinder = new MarkedClassFinder(TestMarker.class, "org.givwenzen.annotations.left.");
      Set<MarkedClass> classes = markedClassFinder.findMarkedClasses();
      assertTrue(classes.contains(new MarkedClass(MarkedClassA.class)));
      assertFalse(classes.contains(new MarkedClass(MarkedClassB.class)));
      assertEquals(1, classes.size());

      markedClassFinder = new MarkedClassFinder(TestMarker.class, "org.givwenzen.annotations.right.");
      classes = markedClassFinder.findMarkedClasses();
      assertTrue(classes.contains(new MarkedClass(MarkedClassB.class)));
      assertFalse(classes.contains(new MarkedClass(MarkedClassA.class)));
      assertEquals(1, classes.size());
   }

   public void testIgnoreSubPackages() throws Exception {
      MarkedClassFinder markedClassFinder = new MarkedClassFinder(TestMarker.class, "org.givwenzen.annotations.middle.");
      Set<MarkedClass> classes = markedClassFinder.findMarkedClasses();
      assertTrue(classes.contains(new MarkedClass(MarkedClassX.class)));
      assertTrue(classes.contains(new MarkedClass(MarkedClassY.class)));
      assertEquals(2, classes.size());
   }

   public void testSupportMultiplePackages() throws Exception {
      MarkedClassFinder markedClassFinder = new MarkedClassFinder(TestMarker.class, "org.givwenzen.annotations.left.," +
                                                                                    "org.givwenzen.annotations.middle.," +
                                                                                    "org.givwenzen.annotations.right.");
      Set<MarkedClass> classes = markedClassFinder.findMarkedClasses();
      assertTrue(classes.contains(new MarkedClass(MarkedClassA.class)));
      assertTrue(classes.contains(new MarkedClass(MarkedClassB.class)));
      assertTrue(classes.contains(new MarkedClass(MarkedClassX.class)));
      assertTrue(classes.contains(new MarkedClass(MarkedClassY.class)));
      assertEquals(4, classes.size());
   }

   @TestMarker
   private class MyMarkedClass {

   }

}