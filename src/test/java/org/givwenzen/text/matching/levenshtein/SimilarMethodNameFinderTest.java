package org.givwenzen.text.matching.levenshtein;

import org.givwenzen.*;
import org.givwenzen.text.matching.levenshtein.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SimilarMethodNameFinderTest {
   private SimilarMethodNameFinder nameFinder;
   private static final String TEST_NAME = "test";

   @Test
   public void okToEatExceptionsWhenSomethingIsWrongWithMethodBeingCompared() throws Exception {
      // given
      MethodAndInvocationTarget mockMethod1 = mock(MethodAndInvocationTarget.class);
      //noinspection ThrowableInstanceNeverThrown
      when(mockMethod1.getDomainStepPattern()).thenThrow(new NoSuchMethodException());

      Collection<MethodAndInvocationTarget> steps = new ArrayList<MethodAndInvocationTarget>();
      steps.add(mockMethod1);

      // when
      nameFinder.findSimilarMethods(TEST_NAME, steps);

      // then - no exceptioin is thrown
   }

   @Test
   public void shouldFindMethodStringsWithDistanceLessThanMaximumDistance() throws Exception {
      // given - see setup
      MethodAndInvocationTarget mockMethod1 = mock(MethodAndInvocationTarget.class);
      when(mockMethod1.getDomainStepPattern()).thenReturn(TEST_NAME + "!");

      MethodAndInvocationTarget mockMethod2 = mock(MethodAndInvocationTarget.class);
      Collection<MethodAndInvocationTarget> steps = new ArrayList<MethodAndInvocationTarget>();
      steps.add(mockMethod1);

      // when
      Collection<String> foundMethods = nameFinder.findSimilarMethods(TEST_NAME, steps);

      // then
      assertThat(foundMethods).contains(mockMethod1.getDomainStepPattern());
   }

   @Test
   public void shouldNotBeSimilarIfDistanceIsGreaterThanMaximumDistance() throws Exception {
      // given - see setup
      // when / then
      assertThat(nameFinder.isSimilar(TEST_NAME, TEST_NAME + ".!3"));
   }

   @Test
   public void shouldBeFoundWhenDistanceIsEqualToMaximumDistance() throws Exception {
      // given - see setup
      // when / then
      assertThat(nameFinder.isSimilar(TEST_NAME, TEST_NAME + ".!"));
   }

   @Test
   public void shouldBeFoundWhenDistanceIsLessThanMaximumDistance() throws Exception {
      // given - see setup
      // when / then
      assertThat(nameFinder.isSimilar(TEST_NAME, TEST_NAME + "!"));
   }

   @Before
   public void setUp() throws Exception {
      nameFinder = new SimilarMethodNameFinder(2);
   }
}
