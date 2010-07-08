package org.givwenzen;

import org.givwenzen.parse.MethodParameterParser;

import java.util.List;

public interface ICustomParserFinder {
    void addCustomParsers(List<MethodParameterParser> accumulatedParsers);
}
