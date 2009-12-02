package org.givwenzen.reflections.scanners;

import com.google.common.base.Predicate;
import com.google.common.collect.Multimap;

import org.givwenzen.reflections.Configuration;

/**
 *
 */
public interface Scanner {

	void scan(final Object cls);

	void setConfiguration(Configuration configuration);

	void setStore(Multimap<String, String> store);

	String getIndexName();

	Scanner filterBy(Predicate<String> filter);
}
