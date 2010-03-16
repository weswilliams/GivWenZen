package bdd.steps;

import org.givwenzen.GivWenZen;
import org.givwenzen.GivWenZenExecutor;
import org.givwenzen.annotations.DomainStep;
import org.givwenzen.annotations.DomainSteps;
import org.junit.Test;

@DomainSteps
public final class GivWenZenExecutorFunctionalTest {

	@DomainStep("gwz rocks!")
	public void foo() {}
	
	@Test
	public void shouldExecuteStepsWhenCreatedViaDefaultConstructor() throws Exception {
		//given
		GivWenZen gwz = new GivWenZenExecutor();
		//when
		gwz.given("gwz rocks!");
		//then no exception is thrown
	}
}