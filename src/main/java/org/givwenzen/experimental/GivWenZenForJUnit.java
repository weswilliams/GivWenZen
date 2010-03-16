package org.givwenzen.experimental;

import org.givwenzen.GivWenZen;
import org.givwenzen.GivWenZenExecutor;
import org.givwenzen.GivWenZenExecutorCreator;

public class GivWenZenForJUnit implements GivWenZen {

    private GivWenZenExecutor executor;

    /**
     * This executor can be run from jUnit.
     * <p>
     * The main use case is to 'gently' introduce the idea of GWZ to the
     * developers.
     * <p>
     * Here is the idea: there is a new team that didn't use fitnesse at all.
     * For first couple of iterations they write some jUnits with GWZ. After a
     * while the team is familar with the GWZ concept. The issues related to
     * functional testing are resolved (e.g. there is a DB for tests,
     * auto-rollback on tearDown is ready, tests pass consistently) This is
     * finally the time to introduce Fitnesse and refactor the tests to be used
     * in Fitnesse.
     * <p>
     * Example:
     * 
     * <pre>
     * &#064;DomainSteps
     * public class GivWenZenWithJUnitTest {
     * 
     *     GivWenZen gwz = new GivWenZenForJUnit(this);
     * 
     *     boolean happy = false;
     * 
     *     &#064;DomainStep(&quot;i go to the coffee shop&quot;)
     *     public void iGoToShop() {
     *         happy = true;
     *     }
     * 
     *     &#064;DomainStep(&quot;i am happy&quot;)
     *     public void iAmInShop() {
     *         assertTrue(happy);
     *     }
     * 
     *     &#064;Test
     *     public void shouldVisitCoffeeShop() throws Exception {
     *         gwz.when(&quot;i go to the coffee shop&quot;);
     *         gwz.then(&quot;i am happy&quot;);
     *     }
     * }
     * </pre>
     * 
     * @param test
     */
    public GivWenZenForJUnit(Object test) {
        if (test == null) {
            throw new RuntimeException("test cannot be null");
        }
        // String basePackageForSteps = DomainStepFinder.DEFAULT_STEP_PACKAGE;
        String basePackageForSteps = test.getClass().getPackage().getName()
                + ".";

        executor = GivWenZenExecutorCreator.instance().stepClassBasePackage(
                basePackageForSteps).create();
    }

    @Override
    public Object and(String methodString) throws Exception {
        return executor.and(methodString);
    }

    @Override
    public Object given(String methodString) throws Exception {
        return executor.given(methodString);
    }

    @Override
    public Object then(String methodString) throws Exception {
        return executor.then(methodString);
    }

    @Override
    public Object when(String methodString) throws Exception {
        return executor.when(methodString);
    }
}