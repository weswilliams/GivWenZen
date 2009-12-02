package org.givwenzen.reflections.util;

import java.util.*;

import org.givwenzen.reflections.ReflectionsException;

/** convenient methods for translating to/from type descriptors */
public class DescriptorHelper {

    /** I[Ljava.lang.String; -> int, java.lang.String[] */
    public static List<String> splitDescriptor(final String descriptor) {
        List<String> result = new ArrayList<String>();

        int cursor = 0;
        while (cursor < descriptor.length()) {
            int start = cursor;
            while (descriptor.charAt(cursor) == '[') {cursor ++;}
            char rawType = descriptor.charAt(cursor);
            if (rawType == 'L') {
                cursor = descriptor.indexOf(";", cursor);
            }
            cursor++;

            result.add(descriptorToTypeName(descriptor.substring(start, cursor)));
        }

        return result;
    }

    /** I -> int; [Ljava.lang.String; -> java.lang.String[] */
    public static String descriptorToTypeName(final String element) {
        int start = element.lastIndexOf("[") + 1;
        char rawType = element.charAt(start);

        String name;
        switch (rawType) {
            case 'L':
                name = element.substring(start + 1, element.indexOf(';')).replace("/", ".");
                break;
            default:
                name = primitiveToTypeName(rawType);
                break;
        }

        return name + Utils.repeat("[]", start);
    }
    /** int -> I; java.lang.Object -> java.lang.Object; java.lang.String[] -> [Ljava.lang.String; */
    public static String typeNameToDescriptor(String typeName) {
        if (!typeName.contains("[")) {
            return typeName;
        } else {
        String s = typeName.replaceAll("]", "");
            int i = typeName.indexOf('[');
            String arr = s.substring(s.indexOf('['));
            String s1 = typeName.substring(0, i);
            return arr + 'L' + s1 + ';';
        }
    }

    /**
     * method (I[Ljava.lang.String;)Ljava.lang.Object; -> int, java.lang.String[]
     */
    public static List<String> methodDescriptorToParameterNameList(final String descriptor) {
        return splitDescriptor(
                descriptor.substring(descriptor.indexOf("(") + 1, descriptor.lastIndexOf(")")));
    }

    /**
     * method (I[Ljava.lang.String;)Ljava.lang.Object; -> java.lang.Object
     */
    public static String methodDescriptorToReturnTypeName(final String descriptor) {
        return splitDescriptor(
                descriptor.substring(descriptor.lastIndexOf(")") + 1))
                .get(0);
    }

    /**
     * I -> java.lang.Integer; V -> java.lang.Void
     */
    public static String primitiveToTypeName(final char rawType) {
        return primitiveToType(rawType).getName();
    }

    private static Class<?> primitiveToType(char rawType) {
        return  'Z' == rawType ? Boolean.TYPE :
                'C' == rawType ? Character.TYPE :
                'B' == rawType ? Byte.TYPE :
                'S' == rawType ? Short.TYPE :
                'I' == rawType ? Integer.TYPE :
                'J' == rawType ? Long.TYPE :
                'F' == rawType ? Float.TYPE :
                'D' == rawType ? Double.TYPE :
                'V' == rawType ? Void.TYPE :
                /*error*/      null;
    }

    /**
     * java.lang.String -> java/lang/String.class
     */
    public static String classNameToResourceName(final String className) {
        return qNameToResourceName(className) + ".class";
    }

    /**
     * java.lang.String -> java/lang/String
     */
    public static String qNameToResourceName(final String qName) {
        return qName.replace(".", "/");
    }

    /**
     * tries to resolve the given type name to a java type
     * accepted types are except for ordinary java object (java.lang.String) are primitives (int, boolean, ...) and array types (java.lang.String[][])
     */
    public static Class<?> resolveType(String typeName) {
        if (primitives.containsKey(typeName)) {
            return primitives.get(typeName);
        }

        try {
            String descriptor = typeNameToDescriptor(typeName);
            return Class.forName(descriptor);
        } catch (ClassNotFoundException e) {
            throw new ReflectionsException("could not resolve type " + typeName, e);
        }
    }

    //
    static Map<String, Class<?>> primitives; static {
        primitives = new HashMap<String, Class<?>>();
        primitives.put("boolean", Boolean.TYPE);
        primitives.put("char", Character.TYPE);
        primitives.put("byte", Byte.TYPE);
        primitives.put("short", Short.TYPE);
        primitives.put("int", Integer.TYPE);
        primitives.put("long", Long.TYPE);
        primitives.put("float", Float.TYPE);
        primitives.put("double", Double.TYPE);
        primitives.put("void", Void.TYPE);
    }
}
