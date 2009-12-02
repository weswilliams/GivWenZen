package org.givwenzen.reflections.adapters;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

/**
 * A very naive implementation of parallel task computation using a thread pool.
 * 
 * @author justinsb
 * 
 */
public final class ThreadPoolParallelStrategy implements ParallelStrategy {
	private final ExecutorService executorService;
	private final int parallelismLevel;

	public static ThreadPoolParallelStrategy buildDefault() {
		int parallelismLevel = Runtime.getRuntime().availableProcessors();

		ExecutorService executorService = Executors.newFixedThreadPool(parallelismLevel);
		return new ThreadPoolParallelStrategy(parallelismLevel, executorService);
	}

	public ThreadPoolParallelStrategy(int parallelismLevel, ExecutorService executorService) {
		super();
		this.parallelismLevel = parallelismLevel;
		this.executorService = executorService;
	}

	public <K, V> List<V> transform(Iterator<K> source, final Function<K, V> function) throws InterruptedException, ExecutionException {
		List<V> destination = Lists.newArrayList();
		return transform(source, function, destination);
	}

	public <K, V, CollectionType extends Collection<V>> CollectionType transform(Iterator<K> source, final Function<K, V> function, CollectionType destination) throws InterruptedException, ExecutionException {
		final List<Future<V>> futures = Lists.newArrayList();

		while (source.hasNext()) {
			final K nextKey = source.next();
			Future<V> future = executorService.submit(new Callable<V>() {
				public V call() throws Exception {
					return function.apply(nextKey);
				}
			});

			futures.add(future);
		}

		for (Future<V> future : futures) {
			V result = future.get();
			if (destination != null) {
				destination.add(result);
			}
		}

		return destination;
	}

	public int getParallelismLevel() {
		return parallelismLevel;
	}
	
	public void shutdown() {
	    executorService.shutdown();
	}
}
