package org.givwenzen.reflections.adapters;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.common.base.Function;

/**
 * An abstract strategy for how we run bulk-operations in parallel (or single-threaded if we prefer!)
 *
 */
public interface ParallelStrategy {
	<K, V> List<V> transform(Iterator<K> source, final Function<K, V> function) throws InterruptedException, ExecutionException;

	int getParallelismLevel();
	
	public void shutdown();
}
