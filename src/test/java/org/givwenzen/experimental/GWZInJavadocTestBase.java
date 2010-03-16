package org.givwenzen.experimental;

import org.givwenzen.experimental.GWZInJavadoc;
import org.junit.Test;

public class GWZInJavadocTestBase {
	
	@Test
	public void specification() throws Exception {
	    new GWZInJavadoc(this, "src/test/java").execute();
	}
}