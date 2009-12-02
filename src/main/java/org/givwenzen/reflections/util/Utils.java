package org.givwenzen.reflections.util;

import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.givwenzen.reflections.ReflectionsException;

import java.util.*;

/**
 * a garbage can of convenient methods
 */
@SuppressWarnings("unchecked")
public abstract class Utils {
    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    /** try to resolves all given string representation of types to a list of java types */
    public static <T> List<Class<? extends T>> forNames(final Iterable<String> classes) {
        List<Class<? extends T>> result = new ArrayList<Class<? extends T>>();
        for (String className : classes) {
            result.add((Class<? extends T>) DescriptorHelper.resolveType(className));
        }
        return result;
    }

    public static <T> List<Class<? extends T>> forNames(final String... classes) {
        List<Class<? extends T>> result = new ArrayList<Class<? extends T>>(classes.length);
        for (String className : classes) {
            result.add((Class<? extends T>) DescriptorHelper.resolveType(className));
        }
        return result;
    }

    public static ClassLoader getEffectiveClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }


    public static String join(Collection<String> strings, String sep) {
        if (strings.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (String parameterName : strings) {
            sb.append(parameterName).append(sep);
        }

        String names = sb.substring(0, sb.length() - sep.length());
        return names;
    }

    public static String repeat(String string ,int times) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < times; i++) {
            sb.append(string);
        }

        return sb.toString();
    }

    public static FileSystemManager getVFSManager() {
        try {
            return VFS.getManager();
        } catch (FileSystemException e) {
            throw new ReflectionsException("could not get VFS Manager", e);
        }
    }
    
	/**
	 * isEmpty compatible with Java 5
	 */
	public static boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}
}
