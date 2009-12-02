package org.givwenzen.reflections.scanners;

import java.util.List;

/** scans for superclass and interfaces of a class, allowing a reverse lookup for subtypes */
public class SubTypesScanner extends AbstractScanner {
    public static final String indexName = "SubTypes";

    @SuppressWarnings({"unchecked"})
    public void scan(final Object cls) {
		String className = getMetadataAdapter().getClassName(cls);
		String superclass = getMetadataAdapter().getSuperclassName(cls);
		List<String> interfaces = getMetadataAdapter().getInterfacesNames(cls);

		if (!superclass.equals(Object.class.getName()) && accept(superclass)) {
            store.put(superclass, className);
        }

		for (String anInterface : interfaces) {
			if (accept(anInterface)) {
                store.put(anInterface, className);
            }
        }
    }

	public String getIndexName() {
        return indexName;
    }
}
