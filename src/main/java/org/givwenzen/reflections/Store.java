package org.givwenzen.reflections;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.*;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class Store {
    final Cache<String, Multimap<String, String>> store = CacheBuilder.newBuilder().build(
            new CacheLoader<String, Multimap<String, String>>() {
                    public Multimap<String, String> load(String key) {
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

        Multimap<String, String> map = store.getIfPresent(indexName);
        if(map == null) {
            map = ArrayListMultimap.create();
            store.put(indexName, map);
        }
        for (String key : keys) {
            result.addAll(map.get(key));
        }

        return result;
	}

    Multimap<String, String> getStore(String indexName) {
        return store.getIfPresent(indexName);
    }

    /** merges given store into this */    
	public void merge(final Store outer) {
		for (String indexName : outer.store.asMap().keySet()) {
			this.store.getIfPresent(indexName).putAll(outer.store.getIfPresent(indexName));
		}
	}
}
