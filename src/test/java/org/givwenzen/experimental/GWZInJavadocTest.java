package org.givwenzen.experimental;

import org.givwenzen.experimental.GWZInJavadoc;
import org.junit.Test;

/**
 * <pre>
 * When: i go to the coffee shop
 * Then: i am happy
 * </pre>
 */
public class GWZInJavadocTest {
	
	@Test
	public void specification() throws Exception {
	    new GWZInJavadoc(this, "src/test/java").execute();
	}
}