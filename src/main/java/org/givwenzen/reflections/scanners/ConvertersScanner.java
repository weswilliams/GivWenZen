package org.givwenzen.reflections.scanners;

import java.util.List;

/**
 *
 */
@SuppressWarnings({"unchecked"})
/** scans for methods that take one class as an argument and returns another class */
public class ConvertersScanner extends AbstractScanner {
    public static final String indexName = "Converters";

    public void scan(final Object cls) {
        String className = getMetadataAdapter().getClassName(cls);
        List<Object> methods = getMetadataAdapter().getMethods(cls);
        for (Object method : methods) {
            List<String> parameterNames = getMetadataAdapter().getParameterNames(method);

            if (parameterNames.size() == 1) {
                String from = parameterNames.get(0);
                String to = getMetadataAdapter().getReturnTypeName(method);

                if (!to.equals("void") && (accept(from) || accept(to))) {
                    String methodKey = getMetadataAdapter().getMethodKey(cls, method);
                    store.put(getConverterKey(from, to), String.format("%s.%s", className, methodKey));
                }
            }
        }
    }

    public static String getConverterKey(String from, String to) {
        return from + " to " + to;
    }

    public String getIndexName() {
        return indexName;
    }

    public static String getConverterKey(Class<?> from, Class<?> to) {
        return getConverterKey(from.getName(), to.getName());
    }
}
