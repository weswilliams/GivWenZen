package org.givwenzen;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.givwenzen.DomainStepFactory;
import org.givwenzen.GivWenZen;
import org.givwenzen.annotations.DomainSteps;
import org.givwenzen.annotations.MarkedClass;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DomainStepFactoryTest {
   private DomainStepFactory domainStepFactory;

   @Test
   public void testFindStepsWithNoParameterConstructor() throws Exception {
      assertStepDefinitionsCanBeCreatedFrom(StepsClassForUnitTest.class);
   }

   @Test
   public void testFindStepsWithObjectParameterConstructor() throws Exception {
      assertStepDefinitionsCanBeCreatedFrom(StepsClassWithObjectConstructorForUnitTesting.class);
   }

   public List assertStepDefinitionsCanBeCreatedFrom(Class<?> aClass){
      Set<MarkedClass> classes = createMarkedClasses(aClass);
      Object[] adapters = new Object[]{this};
      List list = domainStepFactory.createStepDefinitions(classes, adapters);
      assertEquals(1, list.size());
      assertTrue(aClass.isInstance(list.get(0)));
      return list;
   }

   @Test
   public void testFindStepsWithInterfaceParameterConstructor() throws Exception {
       List list = createStepDefinitions(StepClassWithInterfaceConstructorForUnitTesting.class);
      assertTrue(list.get(0) instanceof StepClassWithInterfaceConstructorForUnitTesting);
   }

   @Test
   public void testFindStepsWithDomainStepsParameterConstructor() throws Exception {
      Set<MarkedClass> classes = createMarkedClasses(StepsClassWithDomainStepNamesConstructor.class);
      Object[] adapters = new Object[]{new SerializableClassForConstructorTest(), mock(GivWenZen.class)};
      List list = domainStepFactory.createStepDefinitions(classes, adapters);
      assertEquals(1, list.size());
      assertTrue(list.get(0) instanceof StepsClassWithDomainStepNamesConstructor);
      assertNotNull(((StepsClassWithDomainStepNamesConstructor) list.get(0)).getDomainStepNames());
   }

   @Test
   public void testWhatHappensWhenOnlyStepClassIsUnusable() throws Exception {
       Class<?> aClass = StepClassWhoseOnlyConstructorIsUnrelatedToAnythingStepFinderKnowsAbout.class;
       List list = createStepDefinitions(aClass);
      assertTrue(list.get(0) instanceof MarkedClass.DummyMarkedClass);
   }

    private List createStepDefinitions(Class<?> aClass) {
        Set<MarkedClass> classes = createMarkedClasses(aClass);
        Object[] adapters = new Object[]{new SerializableClassForConstructorTest()};
        List list = domainStepFactory.createStepDefinitions(classes, adapters);
        assertEquals(1, list.size());
        return list;
    }

    @Test
   public void testCreateDoesNotScreamWhenClassThrowsAnException() throws Exception {
      MarkedClass markedClass = new MarkedClass(TestMarkedClass.class);
      assertTrue(domainStepFactory.create(markedClass, this) instanceof MarkedClass.DummyMarkedClass);
   }

   @Test
   public void testCreateDoesNotScreamWhenClassDoesNotHaveMatchConstructor() throws Exception {
      MarkedClass markedClass = new MarkedClass(TestMarkedClassWithInvalidConstructor.class);
      assertTrue(domainStepFactory.create(markedClass, this) instanceof MarkedClass.DummyMarkedClass);
   }

   public class TestMarkedClass {
      public TestMarkedClass() {
         throw new RuntimeException();
      }
   }

   class TestMarkedClassWithInvalidConstructor {
      TestMarkedClass forTheConstructor;

      TestMarkedClassWithInvalidConstructor(TestMarkedClass forTheConstructor) {
         this.forTheConstructor = forTheConstructor;
      }
   }

   private Set<MarkedClass> createMarkedClasses(Class<?> aClass) {
      Class[] markedClasses = new Class[]{aClass};
      return getMarkedClasses(markedClasses);
   }

   private Set<MarkedClass> getMarkedClasses(Class... markedClasses) {
      Set<MarkedClass> stepsClasses = new HashSet<MarkedClass>();
      for (Class markedClass : markedClasses) {
         stepsClasses.add(new MarkedClass(markedClass));
      }
      return stepsClasses;
   }

   @Before public void setUp() throws Exception {
      domainStepFactory = new DomainStepFactory();
   }

   @DomainSteps
   public class StepsClassForUnitTest {

   }

   public class SerializableClassForConstructorTest implements Serializable {
   }
}