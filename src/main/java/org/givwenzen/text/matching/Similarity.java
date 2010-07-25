package org.givwenzen.text.matching;

import org.givwenzen.*;

import java.util.*;

public interface Similarity {
   Collection<String> findSimilarMethods(
         String methodString, Collection<MethodAndInvocationTarget> steps);
}
