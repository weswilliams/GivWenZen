package org.givwenzen.parse;

import fitnesse.slim.Converter;
import fitnesse.slim.converters.ConverterRegistry;

public class PropertyEditorParser implements MethodParameterParser {
    public PropertyEditorParser() {
    }

    public static Converter getPropertyConverterFor(Class<?> paramerterType) {
        return ConverterRegistry.getConverterForClass(paramerterType);
    }

    public boolean canParse(Class<?> paramerterType) {
        return getPropertyConverterFor(paramerterType) != null;
    }

    public Object parse(Object paramerter, Class<?> paramerterType) {
        Converter propertyConverter = getPropertyConverterFor(paramerterType);
        return propertyConverter.fromString(paramerter.toString());
    }
}