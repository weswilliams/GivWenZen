package org.givwenzen.reflections.util;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import org.apache.commons.vfs.*;
import org.givwenzen.reflections.ReflectionsException;

import static org.givwenzen.reflections.util.DescriptorHelper.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;

/**
 * Some classpath convenient methods
 *
 */
public abstract class ClasspathHelper {

    /**
     * urls in current classpath from System property java.class.path
     * optional patterns are used to filter url names that contains either pattern
     *
     * for example: getUrlsForCurrentClasspath("client/")
     */
    public static Collection<URL> getUrlsForCurrentClasspath(String... patterns) {
        List<URL> urls = Lists.newArrayList();

        String javaClassPath = System.getProperty("java.class.path");
        FileSystemManager vfsManager = Utils.getVFSManager();

        if (javaClassPath != null) {
            for (String path : javaClassPath.split(File.pathSeparator)) {
                try {
                    URL url = vfsManager.resolveFile(path).getURL();
                    if (patterns != null) {
                        for (String pattern : patterns) {
                            if (url.toExternalForm().contains(pattern)) {
                                urls.add(url);
                                break; //only once
                            }
                        }
                    } else {
                        urls.add(url);
                    }
                } catch (FileSystemException e) {
                    throw new ReflectionsException("could not create url from " + path, e);
                }
            }
        }

        return urls;
    }

    /**
     * actually, urls in current classpath which are directories
     */
    public static Collection<URL> getUrlsForSourcesOnly() {
        List<URL> urls = Lists.newArrayList();

        for (URL url : getUrlsForCurrentClasspath()) {
            try {
                if (Utils.getVFSManager().resolveFile(url.getPath()).getType().equals(FileType.FOLDER)) {
                    urls.add(url);
                }
            } catch (FileSystemException e) {
                throw new ReflectionsException("could not resolve url " + url, e);
            }
        }
        return urls;
    }

    /**
     * the url that contains the given class.
     */
    public static URL getUrlForClass(Class<?> aClass) {
        return Utils.getEffectiveClassLoader().getResource(
                qNameToResourceName(aClass.getPackage().getName()));
    }

    /**
     * returns a set of urls that contain resources with prefix as the given parameter, that is exist in
     * the equivalent directory within the urls of current classpath
     */
    public static Collection<URL> getUrlsForPackagePrefix(String packagePrefix) {
        try {
            return Lists.newArrayList(Iterators.forEnumeration(
                    Utils.getEffectiveClassLoader().getResources(
                            qNameToResourceName(packagePrefix))));
        } catch (IOException e) {
            throw new ReflectionsException("Can't resolve URL for package prefix " + packagePrefix, e);
        }
    }

    /** finds all FileObjects in current class path starting with given packagePrefix and matching resourceNameFilter */
    public static List<FileObject> findFileObjects(final String packagePrefix, final Predicate<String> resourceNameFilter) {
        FileSelector fileSelector = new FileSelector() {
            public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
                String path = fileInfo.getFile().getURL().toExternalForm();
                String filename = path.substring(path.indexOf(packagePrefix) + packagePrefix.length());

                return !Utils.isEmpty(filename) && resourceNameFilter.apply(filename.substring(1));
            }

            public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
                return true;
            }
        };

        List<FileObject> fileObjects;
        try {
            fileObjects = Lists.newArrayList();

            for (URL url : getUrlsForPackagePrefix(packagePrefix)) {
                FileObject[] files = Utils.getVFSManager().resolveFile(url.getPath()).findFiles(fileSelector);
                fileObjects.addAll(Lists.newArrayList(files));
            }
        } catch (FileSystemException e) {
            throw new ReflectionsException("could not collect", e);
        }

        return fileObjects;
    }
}

