package org.givwenzen.experimental;

import org.givwenzen.GivWenZen;
import org.givwenzen.experimental.GivWenZenForJUnit;
import org.junit.Test;

public class GivWenZenWithJUnitTest  {
    
	GivWenZen gwz = new GivWenZenForJUnit(this);
	
	@Test
	public void shouldVisitCoffeeShop() throws Exception {
		gwz.when("i go to the coffee shop");
		gwz.then("i am happy");
	}
}