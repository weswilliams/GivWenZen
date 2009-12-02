package org.givwenzen.reflections;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Store {

	final Map<String/*indexName*/, Multimap<String, String>> store = new MapMaker().makeComputingMap(new Function<String, Multimap<String, String>>() {
		public Multimap<String, String> apply(String indexName) {
			return Multimaps.newSetMultimap(new MapMaker().<String, Collection<String>>makeMap(), new Supplier<Set<String>>() {
				public Set<String> get() {
					return Sets.newHashSet();
				}
			});
		}
	});

    /** get all values of given keys from given indexName */
	public Set<String> get(String indexName, String... keys) {
        Set<String> result = Sets.newHashSet();

        Multimap<String, String> map = store.get(indexName);
        for (String key : keys) {
            result.addAll(map.get(key));
        }

        return result;
	}

    Multimap<String, String> getStore(String indexName) {
        return store.get(indexName);
    }

    /** merges given store into this */    
	public void merge(final Store outer) {
		for (String indexName : outer.store.keySet()) {
			this.store.get(indexName).putAll(outer.store.get(indexName));
		}
	}
}
