package org.givwenzen.text.matching.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.*;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.*;
import org.givwenzen.*;
import org.givwenzen.text.matching.Similarity;

import java.io.*;
import java.util.*;

public class LuceneSimilarity implements Similarity {

   final Analyzer analyzer = new StandardAnalyzer();
   String contentFieldName = "contents";

   @Override
   public Collection<String> findSimilarMethods(String methodString, Collection<MethodAndInvocationTarget> steps) {
      Collection<String> methodAnnotations = new ArrayList<>();
      try {
         Directory indexDir = buildLuceneIndexFrom(steps);
         DirectoryReader reader = DirectoryReader.open(indexDir);
         IndexSearcher searcher = new IndexSearcher(reader);
         ScoreDoc[] methods = findSimilarMethods(methodString, searcher);
         methodAnnotations = translateToMethodAnnotationStrings(methods, searcher);
      } catch (IOException e) {
         // using an in memory directory and not doing IO, problem with lucene interface
         e.printStackTrace();
      }
      return methodAnnotations;
   }

   private Collection<String> translateToMethodAnnotationStrings(ScoreDoc[] methods, IndexSearcher searcher) {
      List<String> methodAnnotations = new ArrayList<>();
      try {
         for (ScoreDoc method : methods) {
            methodAnnotations.add(searcher.doc(method.doc).get(contentFieldName));
         }
      } catch (IOException e) {
         // using an in memory directory and not doing IO, problem with lucene interface
         e.printStackTrace();
      }
      return methodAnnotations;
   }

   private ScoreDoc[] findSimilarMethods(String methodString, IndexSearcher searcher) {
      ScoreDoc[] hits = new ScoreDoc[0];
      try {
         QueryParser parser = new QueryParser(contentFieldName, analyzer);
         Query query = parser.parse(methodString);
         hits = searcher.search(query, 5).scoreDocs;
      } catch (Exception e) {
         // using an in memory directory and not doing IO, problem with lucene interface
         e.printStackTrace();
      }
      return hits;
   }

   private Directory buildLuceneIndexFrom(Collection<MethodAndInvocationTarget> steps) {
      final Directory indexDir = new RAMDirectory();
      try {
         IndexWriterConfig config = new IndexWriterConfig(analyzer);
         IndexWriter writer = new IndexWriter(indexDir, config);
         for (MethodAndInvocationTarget step : steps) {
            Document documentA = new Document();
            documentA.add(new Field(contentFieldName, step.getDomainStepPattern(), TextField.TYPE_STORED));
            writer.addDocument(documentA);
         }
         writer.close();
      } catch (Exception e) {
         // using an in memory directory and not doing IO, problem with lucene interface
         e.printStackTrace();
      }

      return indexDir;
   }
}
