package org.givwenzen.experimental;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.givwenzen.GivWenZenExecutor;
import org.givwenzen.GivWenZenExecutorCreator;

public class GWZInJavadoc {

    private GivWenZenExecutor executor;
    private final Object test;
    private final String relativePathToSources;

    /**
     * This executor can be run from jUnit with GWZ text in <b>javadoc</b>
     * <p>
     * The main use case is to 'gently' introduce the idea of GWZ to the
     * developers.
     * <p>
     * Here is the idea: there is a new team that didn't use fitnesse at all.
     * For first couple of iterations they write some jUnits with GWZ. After a
     * while the team is familiar with the GWZ concept. The issues related to
     * functional testing are resolved (e.g. there is a DB for tests,
     * auto-rollback on tearDown is ready, tests pass consistently) This is
     * finally the time to introduce Fitnesse and refactor the tests to be used
     * in Fitnesse.
     * 
     * <pre>
     *   //<b>in Javadoc</b> here you put the test specification 
     *   public class SomeTest {
     *     //this test method can go to the base class - this way you can make tests cleaner 
     *     &#064;Test public void specification() throws Exception {
     *       new GWZInJavadoc(this, "src/test/java").execute();
     *     }
     * </pre>
     * 
     * Example of test you can put in javadoc:
     * <pre>
     * Given: i go to the coffee shop
     * And:   i buy 2 cups of coffee
     * When:  i drink coffee
     * Then:  i feel as if I drank 2 cups of coffee
     * </pre>
     * <b>Hints</b>
     * <li>It is useful to put the steps between *pre* tags so that javadoc displays correctly
     * <li>You can create a base class for tests and put there a test method that calls <code>new GWZInJavadoc().execute();</code>
     * @param test instance of jUnit test case (usually you simply pass
     *        <i>this</i> here)
     * @param relativePathToSources
     *            - for example: src/test/java
     */
    public GWZInJavadoc(Object test, String relativePathToSources) {
        if (test == null) {
            throw new RuntimeException("test cannot be null");
        }
        if (relativePathToSources == null) {
            throw new RuntimeException("relativePathToSources cannot be null");
        }
        this.test = test;
        this.relativePathToSources = relativePathToSources;
        // String basePackageForSteps = DomainStepFinder.DEFAULT_STEP_PACKAGE;
        String basePackageForSteps = test.getClass().getPackage().getName()
                + ".";

        executor = GivWenZenExecutorCreator.instance().stepClassBasePackage(
                basePackageForSteps).create();
    }

    public void execute() {
        try {
            String pkg = test.getClass().getPackage().getName().replaceAll(
                    "\\.", "/");
            String fileName = test.getClass().getSimpleName() + ".java";
            File source = new File(relativePathToSources + "/" + pkg, fileName);
            BufferedReader r = new BufferedReader(new FileReader(source));
            String line = r.readLine();
            while (line != null) {
                String step = extractStep(line);
                if (step != null) {
                    executor.when(step);
                }
                // end of javadoc
                if (line.trim().startsWith("*/")) {
                    break;
                }
                line = r.readLine();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    Pattern p = Pattern.compile("\\s*?\\*\\s*?(Given|And|When|Then)\\s*?:(.*)");

    private String extractStep(String line) {
        Matcher m = p.matcher(line);
        if (m.matches()) {
            return m.group(2).trim();
        }
        return null;
    }
}