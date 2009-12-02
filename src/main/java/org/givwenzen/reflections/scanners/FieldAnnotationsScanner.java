package org.givwenzen.reflections.scanners;

import java.util.List;

/**
 *
 */
/** scans for field's annotations */
@SuppressWarnings({"unchecked"})
public class FieldAnnotationsScanner extends AbstractScanner {

    public static final String indexName = "FieldAnnotations";

    public void scan(final Object cls) {
        final String className = getMetadataAdapter().getClassName(cls);
        List<Object> fields = getMetadataAdapter().getFields(cls);
        for (final Object field : fields) {
            List<String> fieldAnnotations = getMetadataAdapter().getFieldAnnotationNames(field);
            for (String fieldAnnotation : fieldAnnotations) {

                if (accept(fieldAnnotation)) {
                    String fieldName = getMetadataAdapter().getFieldName(field);
                    store.put(fieldAnnotation, String.format("%s.%s", className, fieldName));
                }
            }
        }
    }

    public String getIndexName() {
        return indexName;
    }
}
