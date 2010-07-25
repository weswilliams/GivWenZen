package org.givwenzen.text.matching.lucene;

import org.givwenzen.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;
import static org.mockito.Mockito.*;

public class LuceneSimilarityTest {
   private String helloTestAnnotation = "hello test";
   private String helloWorldAnnotation = "hello world";
   private List<MethodAndInvocationTarget> steps = new ArrayList<MethodAndInvocationTarget>();
   private LuceneSimilarity luceneSimilarity;
   private MethodAndInvocationTarget helloTest;
   private MethodAndInvocationTarget helloWorld;

   @Test
   public void shouldFindMethodWithAtLeastOneWordInCommon() throws Exception {
      // given
      steps.add(helloTest);

      //when
      Collection<String> methods = luceneSimilarity.findSimilarMethods("hello world", steps);

      //then
      assertThat(methods).contains(helloTestAnnotation);
   }

   @Test
   public void shouldListClosestNameFirst() throws Exception {
      //given
      steps.add(helloTest);
      steps.add(helloWorld);

      // when
      Collection<String> methods = luceneSimilarity.findSimilarMethods("hello lah world", steps);

      //then
      List<String> methodList = new ArrayList<String>(methods);
      assertThat(methodList.get(0)).isEqualTo(helloWorldAnnotation);
      assertThat(methodList.get(1)).isEqualTo(helloTestAnnotation);
   }
   
   @Before
   public void setUp() throws Exception {
      //given
      helloTest = mock(MethodAndInvocationTarget.class);
      when(helloTest.getDomainStepPattern()).thenReturn(helloTestAnnotation);

      helloWorld = mock(MethodAndInvocationTarget.class);
      when(helloWorld.getDomainStepPattern()).thenReturn(helloWorldAnnotation);

      luceneSimilarity = new LuceneSimilarity();
   }
}
