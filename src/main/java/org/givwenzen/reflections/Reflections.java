package org.givwenzen.reflections;

import static java.lang.String.*;
import static org.givwenzen.reflections.util.DescriptorHelper.*;
import static org.givwenzen.reflections.util.Utils.*;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;

import org.apache.commons.vfs.*;
import org.givwenzen.reflections.adapters.ParallelStrategyHelper;
import org.givwenzen.reflections.scanners.*;
import org.givwenzen.reflections.scanners.Scanner;
import org.givwenzen.reflections.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.*;

public class Reflections {

    private static final Logger log = LoggerFactory.getLogger(Reflections.class);

    private final Configuration configuration;
    private final Store store;
    private final Collection<Class<? extends Scanner>> scannerClasses;

    public Reflections(final Configuration configuration) {
        this.configuration = configuration;
        store = new Store();

        //inject to scanners
        scannerClasses = Lists.newArrayList();
        for (Scanner scanner : configuration.getScanners()) {
            scanner.setConfiguration(configuration);
            scanner.setStore(store.getStore(scanner.getIndexName()));
            scannerClasses.add(scanner.getClass());
        }

        scan();
    }

    protected void scan() {
        long time = System.currentTimeMillis();

        if (configuration.getUrls() == null || configuration.getUrls().isEmpty()) {
            log.error("given scan urls are empty. set urls in the configuration");
            return;
        }

        for (URL url : configuration.getUrls()) {
            FileObject[] fileObjects;
            try {
                FileObject fileObject = Utils.getVFSManager().resolveFile(url.toString());
                fileObjects = fileObject.findFiles(qualifiedClassName);
            } catch (FileSystemException e) {
                throw new ReflectionsException("could not resolve file in " + url, e);
            }
            ParallelStrategyHelper.apply(configuration.getParallelStrategy(), fileObjects, scanFileProcedure);
        }

        time = System.currentTimeMillis() - time;

        Integer keys = 0, values = 0;
        for (Multimap<String, String> multimap : store.store.values()) {
            keys += multimap.keySet().size();
            values += multimap.size();
        }

        configuration.getParallelStrategy().shutdown();
    }

    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        depends(SubTypesScanner.class);

        /*intellij's fault*///noinspection RedundantTypeArguments
        return Sets.newHashSet(Utils.<T>forNames(getAllSubTypesInHierarchy(type.getName())));
    }

    public Set<Class<?>> getSubTypesOf(final Iterable<Class<?>> types) {
        depends(SubTypesScanner.class);

        final Set<Class<?>> subTypes = Sets.newHashSet();
        for (final Class<?> type : types) {
            subTypes.addAll(getSubTypesOf(type));
        }

        return subTypes;
    }

    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
        depends(ClassAnnotationsScanner.class);

        Set<Class<?>> annotatedWith = Sets.newHashSet(forNames(store.get(ClassAnnotationsScanner.indexName, annotation.getName())));
        annotatedWith.addAll(getSubTypesOf(annotatedWith));

        return annotatedWith;
    }

    protected void depends(final Class<? extends Scanner> scannerClass) {
        if (scannerClasses != null && !scannerClasses.contains(scannerClass)) {
            log.error(format("scanner %s is not configured. add it to this Reflections configuration.", scannerClass.getSimpleName()));
        }
    }

    protected Set<String> getAllSubTypesInHierarchy(final String type) {
        Set<String> result = Sets.newHashSet();

        result.addAll(store.get(SubTypesScanner.indexName, type));
        result.addAll(store.get(ClassAnnotationsScanner.indexName, type));

        Set<String> subResult = Sets.newHashSet();
        for (String aClass : result) {
            subResult.addAll(getAllSubTypesInHierarchy(aClass));
        }
        result.addAll(subResult);

        return result;
    }

    private FileSelector qualifiedClassName = new FileSelector() {
        public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
            FileName fileName = fileInfo.getFile().getName();
            if(fileName.getExtension().equals("class") &&
                    configuration.getFilter().apply(fileName.getPath())) {
                return true;
            } else {
                return false;
            }
        }

        public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
            return true;
        }
    };

    private Function<FileObject, Object> scanFileProcedure = new Function<FileObject, Object>() {
        public Object apply(FileObject fileObject) {
            Object cls;
            try {
                cls = configuration.getMetadataAdapter().create(fileObject.getContent().getInputStream());
            } catch (IOException e) {
                throw new ReflectionsException("could not create class file from " + fileObject, e);
            }
            for (Scanner scanner : configuration.getScanners()) {
                scanner.scan(cls);
            }
            return null;
        }
    };
}
