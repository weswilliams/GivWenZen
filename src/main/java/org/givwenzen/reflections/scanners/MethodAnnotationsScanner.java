package org.givwenzen.reflections.scanners;

import java.util.List;

/**
 *
 */
@SuppressWarnings({"unchecked"})
/** scans for method's annotations */
public class MethodAnnotationsScanner extends AbstractScanner {
    public static final String indexName = "MethodAnnotations";

    public void scan(final Object cls) {
        for (Object method : getMetadataAdapter().getMethods(cls)) {
            for (String methodAnnotation : (List<String>) getMetadataAdapter().getMethodAnnotationNames(method)) {
                if (accept(methodAnnotation)) {
                    store.put(methodAnnotation, getMetadataAdapter().getMethodKey(cls, method));
                }
            }
        }
    }

    public String getIndexName() {
        return indexName;
    }
}
