package org.givwenzen.text.matching.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.givwenzen.*;
import org.junit.*;

import java.util.*;

import static org.fest.assertions.Assertions.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SecondLuceneSimilarityTest {
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
   
   @Before
   public void setUp() throws Exception {
      //given
      helloTest = mock(MethodAndInvocationTarget.class);
      when(helloTest.getDomainStepPattern()).thenReturn(helloTestAnnotation);

      helloWorld = mock(MethodAndInvocationTarget.class);
      luceneSimilarity = new LuceneSimilarity();
   }
}
