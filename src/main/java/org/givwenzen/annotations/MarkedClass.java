package org.givwenzen.annotations;

public class MarkedClass {
   private Class<?> markedClass;
   public static final DummyMarkedClass DUMMY_MARKED_CLASS = new DummyMarkedClass();

   public MarkedClass(Class<?> markedClass) {
      this.markedClass = markedClass;
   }

   public String getName() {
      return markedClass.getName();
   }

   @Override
   public int hashCode() {
      return markedClass.hashCode();
   }

   @Override
   public boolean equals(Object obj) {
      return obj instanceof MarkedClass && markedClass.equals(((MarkedClass) obj).markedClass);
   }

   public Class<?> getTargetClass() {
      return markedClass;
   }

   public static final class DummyMarkedClass {
   }

}
