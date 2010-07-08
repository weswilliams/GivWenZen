package org.givwenzen;

import org.givwenzen.parse.MethodParameterParser;
import org.givwenzen.reflections.Reflections;
import org.givwenzen.reflections.ReflectionsBuilder;

import java.util.List;
import java.util.Set;

/**
 * Author: Szczepan Faber
 */
public class CustomParserFinder implements ICustomParserFinder {

    @Override
    public void addCustomParsers(List<MethodParameterParser> accumulatedParsers) {
        Reflections reflections = new ReflectionsBuilder()
                .basePackage("bdd.parse.")
                .subTypeScanner()
                .build();
        Set<Class<? extends MethodParameterParser>> classes = reflections.getSubTypesOf(MethodParameterParser.class);
        for (Class<? extends MethodParameterParser> aClass : classes) {
            try {
                accumulatedParsers.add(aClass.newInstance());
            } catch (InstantiationException e) {
                throw new GivWenZenException(
                        "Unable to instantiate " + aClass.getName() +
                                ".  The usual cause of this is the class is an interface or abstract class.", e);
            } catch (IllegalAccessException e) {
                throw new GivWenZenException(
                        "Unable to access the constructor for " + aClass.getName() +
                                ".  The usual cause of this is the constructor is private or protected.", e);
            }
        }
    }
}
