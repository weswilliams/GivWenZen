package org.givwenzen;

import org.givwenzen.text.matching.levenshtein.*;

import java.util.Collection;

public class MissingStepMethodAndInvocationTarget extends MethodAndInvocationTarget {

    public static final String MATCH_ANY_STRING = ".*";
   private Collection<MethodAndInvocationTarget> steps;
   private static final int MAXIMUM_DISTANCE = Integer.parseInt(System.getProperty("GWZ_METHOD_DISTANCE", "10"));

   public MissingStepMethodAndInvocationTarget(Collection<MethodAndInvocationTarget> steps) {
        super(null, null, null);
      this.steps = steps;
   }

    @Override
    public Object invoke(String methodString) throws Exception {
        throw new DomainStepNotFoundException(
                "\nYou need a step class with an annotated method matching this pattern: '" + methodString + "'\n" +
                        "The step class should be placed in the package or sub-package of bdd.steps or your custom package if defined.\n" +
                        "Example:\n" +
                        "  @DomainSteps\n" +
                        "  public class StepClass {\n" +
                        "    @DomainStep(\"" + methodString + "\")\n" +
                        "    public void domainStep() {\n" +
                        "      // TODO implement step\n" +
                        "    }\n" +
                        "  }\n" +
                        "Typical causes of this error are:\n" +
                        "  * StepClass is missing the @DomainSteps annotation\n" +
                        "  * StepMethod is missing the @DomainStep annotation\n" +
                        "  * The step method annotation has a regular expression that is not matching the current test step\n" +
                        buildStringOfSimilarMethods(methodString));
    }

   private String buildStringOfSimilarMethods(String methodString) {
      StringBuilder similarMethodsString = new StringBuilder();
      similarMethodsString.append("\n").append("Methods with similar patterns:");
      Collection<String> similarMethods = new SimilarMethodNameFinder(MAXIMUM_DISTANCE)
            .findSimilarMethods(methodString, steps);
      for (String similarMethod : similarMethods) {
         similarMethodsString.append("\n  * \"").append(similarMethod).append("\"");
      }
      return similarMethodsString.toString();
   }

   @Override
    public String getMethodAsString() {
        return ".stepnotfound";
    }
}
