package org.givwenzen.reflections.scanners;

import org.givwenzen.reflections.Configuration;
import org.givwenzen.reflections.adapters.MetadataAdapter;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Multimap;

/**
 *
 */
public abstract class AbstractScanner implements Scanner {

	private Configuration configuration;
	protected Multimap<String, String> store;
	private Predicate<String> filter = Predicates.alwaysTrue();

	public Scanner filterBy(Predicate<String> filter) {
        this.filter = Predicates.or(this.filter, filter);
        return this;
    }

	public void setConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	public void setStore(final Multimap<String, String> store) {
		this.store = store;
	}

	@SuppressWarnings("unchecked")
    protected MetadataAdapter getMetadataAdapter() {
		return configuration.getMetadataAdapter();
	}

    protected boolean accept(final String fqn) {
		return fqn != null && filter.apply(fqn);
	}
}
