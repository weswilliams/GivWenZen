package org.givwenzen;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import fitlibrary.closure.*;
import fitlibrary.table.Row;
import fitlibrary.traverse.Evaluator;
import fitlibrary.typed.TypedObject;

public class GivWenZenLookUpMethodTarget implements LookupMethodTarget {

    private LookupMethodTarget target = new LookupMethodTargetStandard();

    public Class<?> findClassFromFactoryMethod(Evaluator arg0, Class<?> arg1, String arg2)
                    throws IllegalAccessException, InvocationTargetException {
        return target.findClassFromFactoryMethod(arg0, arg1, arg2);
    }

    public Closure findFixturingMethod(Evaluator arg0, String arg1, Class<?>[] arg2) {
        return target.findFixturingMethod(arg0, arg1, arg2);
    }

    public CalledMethodTarget findGetterUpContextsToo(TypedObject arg0, Evaluator arg1, String arg2, boolean arg3) {
        return target.findGetterUpContextsToo(arg0, arg1, arg2, arg3);
    }

    public CalledMethodTarget findMethod(String arg0, List<String> arg1, String arg2, Evaluator arg3) {
        System.out.println("find method: " + arg0);
        return target.findMethod(arg0, arg1, arg2, arg3);
    }

    public CalledMethodTarget findMethodInEverySecondCell(Evaluator arg0, Row arg1, int arg2) {
        return target.findMethodInEverySecondCell(arg0, arg1, arg2);
    }

    public Closure findNewInstancePluginMethod(Evaluator arg0) {
        return target.findNewInstancePluginMethod(arg0);
    }

    public CalledMethodTarget findPostfixSpecialMethod(Evaluator arg0, String arg1) {
        return target.findPostfixSpecialMethod(arg0, arg1);
    }

    public CalledMethodTarget findSetter(String arg0, Evaluator arg1) {
        return target.findSetter(arg0, arg1);
    }

    public CalledMethodTarget findSpecialMethod(Evaluator arg0, String arg1) {
        return target.findSpecialMethod(arg0, arg1);
    }

    public CalledMethodTarget findTheMethod(String arg0, List<String> arg1, String arg2, Evaluator arg3) {
        return target.findTheMethod(arg0, arg1, arg2, arg3);
    }

    public CalledMethodTarget findTheMethodMapped(String arg0, int arg1, Evaluator arg2) {
        return target.findTheMethodMapped(arg0, arg1, arg2);
    }

    public String identifiedClassesInOutermostContext(Object arg0, boolean arg1) {
        return target.identifiedClassesInOutermostContext(arg0, arg1);
    }

    public String identifiedClassesInSUTChain(Object arg0) {
        return target.identifiedClassesInSUTChain(arg0);
    }

    public void mustBeThreadSafe() {
        target.mustBeThreadSafe();
    }
}
