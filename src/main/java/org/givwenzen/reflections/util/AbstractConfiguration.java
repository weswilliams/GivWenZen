package org.givwenzen.reflections.util;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;

import org.givwenzen.reflections.Configuration;
import org.givwenzen.reflections.adapters.*;
import org.givwenzen.reflections.scanners.Scanner;

import java.net.URL;
import java.util.Collection;
import java.util.Set;

/**
 * an abstract implementation of {@link org.givwenzen.reflections.Configuration}
 * <p>uses reasonalbe defaults, such as {@link #parallelStrategy}={@link ThreadPoolParallelStrategy}, {@link #filter}=accept all
 */
public class AbstractConfiguration implements Configuration {
    private Set<Scanner> scanners;
    private Set<URL> urls;
    @SuppressWarnings("unchecked")
    private MetadataAdapter metadataAdapter = new JavassistAdapter();
    private Predicate<String> filter = Predicates.alwaysTrue();
    private ParallelStrategy parallelStrategy = ThreadPoolParallelStrategy.buildDefault();

    public Set<Scanner> getScanners() {
		return scanners;
	}

    /** set the scanners instances for scanning different metadata */
    public void setScanners(final Scanner... scanners) {
        this.scanners = ImmutableSet.of(scanners);
    }

    public Set<URL> getUrls() {
        return urls;
    }

    /** set the urls to be scanned
     * <p>use {@link org.givwenzen.reflections.util.ClasspathHelper} convenient methods to get the relevant urls
     * */
    public void setUrls(final Collection<URL> urls) {
		this.urls = ImmutableSet.copyOf(urls);
	}

    @SuppressWarnings("unchecked")
    public MetadataAdapter getMetadataAdapter() {
        return metadataAdapter;
    }

    /** sets the metadata adapter used to fetch metadata from classes */
    @SuppressWarnings("unchecked")
    public void setMetadataAdapter(final MetadataAdapter metadataAdapter) {
        this.metadataAdapter = metadataAdapter;
    }

    public Predicate<String> getFilter() {
        return filter;
    }

    /** sets the fully qualified name used to filter types to be scanned
     * <p> supply a {@link com.google.common.base.Predicate} or use the {@link FilterBuilder}*/
    public void setFilter(Predicate<String> qNameFilter) {
        this.filter = qNameFilter;
    }

	public ParallelStrategy getParallelStrategy() {
		return parallelStrategy;
	}

	public void setParallelStrategy(ParallelStrategy parallelStrategy) {
		this.parallelStrategy = parallelStrategy;
	}
}
