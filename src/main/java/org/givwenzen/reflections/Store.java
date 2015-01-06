package org.givwenzen.reflections;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.MapMaker;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 *
 */
public class Store {

//	final Map<String, Multimap<String, String>> store = new MapMaker().makeComputingMap(new Function<String, Multimap<String, String>>() {
//		public Multimap<String, String> apply(String indexName) {
//			return Multimaps.newSetMultimap(new MapMaker().<String, Collection<String>>makeMap(), new Supplier<Set<String>>() {
//				public Set<String> get() {
//					return Sets.newHashSet();
//				}
//			});
//		}
//	});

	final private CacheLoader<String, Multimap<String, String>> cacheLoader = new CacheLoader<String, Multimap<String, String>>() {
		public Multimap<String, String> load(String key) {
			return Multimaps.newSetMultimap(new MapMaker().<String, Collection<String>>makeMap(), new Supplier<Set<String>>() {
				public Set<String> get() {
					return Sets.newHashSet();
				}
			});
		}
	};

	final private LoadingCache<String, Multimap<String, String>> loadingCache = CacheBuilder.newBuilder().build(cacheLoader);

//	final Map<String, Multimap<String, String>> store = loadingCache.asMap();

	public Set<String> get(String indexName, String... keys) {
        Set<String> result = Sets.newHashSet();

		Multimap<String, String> map = null;
		try {
			map = loadingCache.get(indexName);
		} catch (ExecutionException e) {
			throw new ReflectionsException("error loading reflections cache ", e);
		}
		for (String key : keys) {
            result.addAll(map.get(key));
        }

        return result;
	}

    Multimap<String, String> getStore(String indexName) {
			try {
				return loadingCache.get(indexName);
			} catch (ExecutionException e) {
				throw new ReflectionsException("error loading reflections cache ", e);
			}
		}

	public Collection<Multimap<String, String>> values() {
		return loadingCache.asMap().values();
	}
}
