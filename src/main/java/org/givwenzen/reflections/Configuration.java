package org.givwenzen.reflections;

import com.google.common.base.Predicate;

import org.givwenzen.reflections.adapters.MetadataAdapter;
import org.givwenzen.reflections.adapters.ParallelStrategy;
import org.givwenzen.reflections.scanners.Scanner;

import java.net.URL;
import java.util.Set;

/**
 * Configuration is used to create a configured instance of {@link Reflections}
 * <p>it is prefered to use {@link org.givwenzen.reflections.util.AbstractConfiguration}
 */
public interface Configuration {
    /** the scanner instances used for scanning different metadata */
    Set<Scanner> getScanners();

    /** the urls to be scanned */
    Set<URL> getUrls();

    /** the metadata adapter used to fetch metadata from classes */
    @SuppressWarnings("unchecked")
    MetadataAdapter getMetadataAdapter();

    /** the fully qualified name filter used to filter types to be scanned */
    Predicate<String> getFilter();

    /** An adapter that lets us plug in various strategies for scanning types in parallel (or not!) */
    ParallelStrategy getParallelStrategy();
}
