package org.givwenzen;

import org.givwenzen.annotations.DomainStep;
import org.junit.Test;

import java.lang.reflect.Method;

public class MethodAndInvocationTargetTest {

    @Test(expected = InvalidDomainStepParameterException.class)
    public void invokeComplainsWhenTheMethodParametersAreInvalid() throws Exception {
        Object target = new TestTarget();
        Method method = target.getClass().getMethod("methodTakesAnInt", int.class);
        MethodAndInvocationTarget invocation = new MethodAndInvocationTarget(method, target, null);
        invocation.invoke("method takes an int " + Integer.MAX_VALUE + "0");
    }

    static class TestTarget {

        @DomainStep("method takes an int (\\d+)")
        public int methodTakesAnInt(int value) {
            return value;
        }
    }
}
