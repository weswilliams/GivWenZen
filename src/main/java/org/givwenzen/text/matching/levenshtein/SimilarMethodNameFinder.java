package org.givwenzen.text.matching.levenshtein;

import org.givwenzen.*;
import org.givwenzen.text.matching.*;

import java.util.*;

public class SimilarMethodNameFinder implements Similarity {
   private int maximumDistance;
   private Distance distance;

   public SimilarMethodNameFinder(int maximumDistance) {
      this.maximumDistance = maximumDistance;
      distance = new Distance();
   }

   public boolean isSimilar(String name1, String name2) {
      return distance.LD(name1, name2) <= maximumDistance;
   }

   @Override
   public Collection<String> findSimilarMethods(
         String methodString, Collection<MethodAndInvocationTarget> steps) {

      ArrayList<String> foundMethods = new ArrayList<String>();

      for (MethodAndInvocationTarget step : steps) {
         try {
            String stepPattern = step.getDomainStepPattern();
            if (isSimilar(methodString, stepPattern)) foundMethods.add(stepPattern);
         } catch (Exception e) {
            System.err.println("eating error when problem with MethodAndInvocationTarget");
            e.printStackTrace(System.err);
         }
      }

      return foundMethods;
   }
}
