package org.givwenzen.experimental;

import org.givwenzen.GivWenZen;
import org.givwenzen.experimental.GivWenZenForJUnit;
import org.junit.Test;

public class GivWenZenWithJUnitTest  {
	
	@Test
	public void shouldVisitCoffeeShop() throws Exception {
	    GivWenZen gwz = new GivWenZenForJUnit(this);
	    
		gwz.when("i go to the coffee shop");
		gwz.then("i am happy");
	}
}