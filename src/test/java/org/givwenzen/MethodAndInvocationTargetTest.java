package org.givwenzen;

import java.lang.reflect.Method;
import org.givwenzen.annotations.DomainStep;
import org.junit.Test;

public class MethodAndInvocationTargetTest {

  @Test(expected=InvalidDomainStepParameterException.class)
  public void invokeComplainsWhenTheMethodParametersAreInvalid() throws Exception {
    Object target = new TestTarget();
    Method method = target.getClass().getMethod("methodTakesAnInt", int.class);
    MethodAndInvocationTarget invocation = new MethodAndInvocationTarget(method, target);
    invocation.invoke("method takes an int " + Integer.MAX_VALUE + "0");
  }
  
  class TestTarget {
    
    @DomainStep("method takes an int (\\d+)")
    public int methodTakesAnInt(int value) {
      return value;
    }
  }
}
