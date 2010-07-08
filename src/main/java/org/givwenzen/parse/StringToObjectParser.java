package org.givwenzen.parse;

import org.givwenzen.ICustomParserFinder;

import java.util.ArrayList;
import java.util.List;

public class StringToObjectParser {

    private List<MethodParameterParser> parsersInDesiredOrder = new ArrayList<MethodParameterParser>();
    private ICustomParserFinder customParserFinder;

    public StringToObjectParser(ICustomParserFinder customParserFinder) {
        customParserFinder.addCustomParsers(parsersInDesiredOrder);
        parsersInDesiredOrder.add(new StringParser());
        parsersInDesiredOrder.add(new PropertyEditorParser());
        parsersInDesiredOrder.add(new ArrayParser(new PropertyEditorParser()));
        parsersInDesiredOrder.add(new ValueObjectParser());
    }

    public Object[] convertParamertersToTypes(Object[] paramerters, Class<?>[] paramerterTypes) throws Exception {
        List<Object> convertedParamerters = new ArrayList<Object>();
        for (int index = 0; index < paramerterTypes.length; index++) {
            convertedParamerters.add(convertStringToParamerterType(paramerters[index], paramerterTypes[index]));
        }
        return convertedParamerters.toArray(new Object[convertedParamerters.size()]);
    }

    private Object convertStringToParamerterType(Object paramerter, Class<?> paramerterType) throws Exception {
        for (MethodParameterParser parser : parsersInDesiredOrder) {
            if (parser.canParse(paramerterType)) {
                return parser.parse(paramerter, paramerterType);
            }
        }
        return paramerter;
    }
}
