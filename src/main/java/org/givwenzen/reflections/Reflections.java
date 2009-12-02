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
import org.dom4j.*;
import org.dom4j.io.*;
import org.givwenzen.reflections.adapters.ParallelStrategyHelper;
import org.givwenzen.reflections.scanners.*;
import org.givwenzen.reflections.scanners.Scanner;
import org.givwenzen.reflections.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.*;

/**
 * Reflections one-stop-shop object
 * <p>Reflections scans your classpath, indexes the metadata, allows you to query it on runtime
 * and may save and collect that information for many modules within your project.
 * <p>Using Reflections you can query your metadata such as:
 * <ul>
 * <li>get all subtypes of some type
 * <li>get all types/methods/fields annotated with some annotation, w/o annotation parameters matching
 * </ul>
 * <p>a typical use of Reflections would be:
 * <pre>
 *      Reflections reflections = new Reflections("my.package.prefix"); //replace my.package.prefix with your package prefix, of course
 *
 *      Set<Class<? extends SomeClassOrInterface>> subTypes = reflections.getSubTypesOf(SomeClassOrInterface.class);
 *      Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(SomeAnnotation.class);
 *      Set<Class<?>> annotated1 = reflections.getTypesAnnotatedWith(
 *              new SomeAnnotation() {public String value() {return "1";}
 *                                    public Class<? extends Annotation> annotationType() {return SomeAnnotation.class;}});
 * </pre>
 * basically, to use Reflections for scanning and querying, instantiate it with a {@link org.givwenzen.reflections.Configuration}, for example
 * <pre>
 *         new Reflections(
 *               new AbstractConfiguration() {
 *                   {
 *                      setFilter(new FilterBuilder().include("your project's common package prefix here..."));
 *                      setUrls(ClasspathHelper.getUrlsForCurrentClasspath());
 *                      setScanners(new SubTypesScanner(),
 *                                  new ClassAnnotationsScanner().filterBy(myClassAnnotationsFilter));
 *                      ));
 *                  }
 *         });
 * </pre>
 * and than use the convenient methods to query the metadata, such as {@link #getSubTypesOf}, {@link #getTypesAnnotatedWith}, {@link #getMethodsAnnotatedWith}
 * <p>another usage of Reflections is to collect pre saved metadata xml, using {@link #collect()} and {@link #collect(String, com.google.common.base.Predicate)}.
 * in order to save a Reflections instance metadata use {@link #save(String)}  
 */
public class Reflections {

    //todo
    //implement webbeans resolveByType
    //reflections object should be a delegating facade to scanners

    private static final Logger log = LoggerFactory.getLogger(Reflections.class);

    private final Configuration configuration;
    private final Store store;
    private final Collection<Class<? extends Scanner>> scannerClasses;

    /**
     * constructs a Reflections instance and scan according to given {@link Configuration}
     * <p>it is prefered to use {@link org.givwenzen.reflections.util.AbstractConfiguration}
     */
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

    /**
     * a convenient constructor for scanning within a package prefix
     * <p>if no scanners supplied, ClassAnnotationsScanner and SubTypesScanner are used by default
     */
    public Reflections(final String prefix, final Scanner... scanners) {
        this(new AbstractConfiguration() {
            final Predicate<String> filter = new FilterBuilder.Include(prefix);

            {
                Collection<URL> forPackagePrefix = ClasspathHelper.getUrlsForPackagePrefix(prefix);
                setUrls(forPackagePrefix);
                if (scanners == null || scanners.length == 0) {
                    setScanners(
                            new ClassAnnotationsScanner().filterBy(filter),
                            new SubTypesScanner().filterBy(filter));
                } else {
                    setScanners(scanners);
                }
            }
        });
    }

    protected Reflections() {
        configuration = null;
        store = new Store();
        scannerClasses = null;
    }

    //
    protected void scan() {
//        System.out.println("scanning");
        long time = System.currentTimeMillis();

        if (configuration.getUrls() == null || configuration.getUrls().isEmpty()) {
            log.error("given scan urls are empty. set urls in the configuration");
            return;
        }

        for (URL url : configuration.getUrls()) {
            FileObject[] fileObjects;
            try {
                FileObject fileObject = Utils.getVFSManager().resolveFile(url.toString());
//                System.out.println(fileObject.getURL());
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
//
//        log.info(format("Reflections took %d ms to scan %d urls, producing %d keys and %d values%s%s",
//                time, configuration.getUrls().size(), keys, values,
//                parallelStrategy != null ? format(" [using %d cores]", parallelStrategy.getParallelismLevel()) : "",
//                values != 0 ? format(" [%d ms per value]", time / values) : ""));
        
        configuration.getParallelStrategy().shutdown();
    }

    /**
     * saves this Reflections store in a given directory and filename
     * <p>
     * it is prefered to specify a designated directory (for example META-INF/reflections),
     * so that it could be found later much faster using the load method
     */
    public void save(final String filename) {
        final Map<String, Multimap<String, String>> store = this.store.store;

        Document document = DocumentFactory.getInstance().createDocument();
        Element reflections = document.addElement("Reflections");
        for (String indexName : store.keySet()) {
            Element indexElement = reflections.addElement(indexName);
            for (String key : store.get(indexName).keySet()) {
                Element entryElement = indexElement.addElement("entry");
                entryElement.addElement("key").setText(key);
                Element valuesElement = entryElement.addElement("values");
                for (String value : store.get(indexName).get(key)) {
                    valuesElement.addElement("value").setText(value);
                }
            }
        }

        try {
            File file = new File(filename);
            File parent = file.getParentFile();
            if (!parent.exists()) {
                parent.mkdirs();
            }

            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(file), OutputFormat.createPrettyPrint());
            xmlWriter.write(document);
            xmlWriter.close();
//            log.info("Reflections metadata saved in " + filename);
        } catch (IOException e) {
            throw new ReflectionsException("could not save to file " + filename, e);
        }
    }

    private static Reflections read(final InputStream inputStream) {
        Reflections reflections = new Reflections();

        Document document;
        try {
            document = new SAXReader().read(inputStream);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        for (Object e1 : document.getRootElement().elements()) {
            Element index = (Element) e1;
            for (Object e2 : index.elements()) {
                Element entry = (Element) e2;
                Element key = entry.element("key");
                Element values = entry.element("values");
                for (Object o3 : values.elements()) {
                    Element value = (Element) o3;
                    reflections.store.getStore(index.getName()).put(key.getText(), value.getText());
                }
            }
        }

        return reflections;
    }

    /** collect saved Reflection xml from all urls that contains the package META-INF/reflections and includes files matching the pattern .*-reflections.xml*/
    public static Reflections collect() {
        return collect("META-INF/reflections", new FilterBuilder().include(".*-reflections.xml"));
    }

    /**
     * collect saved Reflections xml from all urls that contains the given packagePrefix and matches the given resourceNameFilter
     * <p>
     * it is prefered to use a designated resource prefix (for example META-INF/reflections but not just META-INF),
     * so that relevant urls could be found much faster
     */
    public static Reflections collect(final String packagePrefix, final Predicate<String> resourceNameFilter) {
        final Reflections reflections = new Reflections();

        final List<FileObject> fileObjects = ClasspathHelper.findFileObjects(packagePrefix, resourceNameFilter);

        try {
            for (final FileObject fileObject : fileObjects) {
                reflections.merge(read(fileObject.getContent().getInputStream()));
//                log.info("Reflections collected metadata from " + fileObject);
            }
        } catch (FileSystemException e) {
            throw new ReflectionsException("could not collect", e);
        }

        return reflections;
    }

    /**
     * merges a Reflections instance metadata into this instance
     */
    public Reflections merge(final Reflections reflections) {
        store.merge(reflections.store);
        return this;
    }

    //query

    /**
     * gets all sub types in hierarchy of a given type
     * <p/>depends on SubTypesScanner configured, otherwise an empty set is returned
     */
    public <T> Set<Class<? extends T>> getSubTypesOf(final Class<T> type) {
        depends(SubTypesScanner.class);

        /*intellij's fault*///noinspection RedundantTypeArguments
        return Sets.newHashSet(Utils.<T>forNames(getAllSubTypesInHierarchy(type.getName())));
    }

    /**
     * gets all sub types in hierarchy of given types
     * <p/>depends on SubTypesScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getSubTypesOf(final Iterable<Class<?>> types) {
        depends(SubTypesScanner.class);

        final Set<Class<?>> subTypes = Sets.newHashSet();
        for (final Class<?> type : types) {
            subTypes.addAll(getSubTypesOf(type));
        }

        return subTypes;
    }

    /**
     * gets all sub types in hierarchy of given types
     * <p/>depends on SubTypesScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getSubTypesOf(final Class<?>... types) {
        return getSubTypesOf(Arrays.asList(types));
    }

    /**
     * get types annotated with a given annotation, both types and annotations
     * <p><b>@Inherited is honored</b>, that is all subtypes of <u>classes</u> annotated with Inherited meta-annotation are returned as well
     * <p>Note that this meta-annotation type has no effect if the annotated type is used for anything other than a class.
     * Also, this meta-annotation causes annotations to be inherited only from superclasses; annotations on implemented interfaces have no effect.
     * <p/>depends on ClassAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWithInherited(final Class<? extends Annotation> annotation) {
        depends(ClassAnnotationsScanner.class);

        final Set<Class<?>> result = Sets.newHashSet();

        if (annotation.isAnnotationPresent(Inherited.class)) {
            List<Class<?>> directlyAnnotatedWith = forNames(store.get(ClassAnnotationsScanner.indexName, annotation.getName()));
            for (Class<?> annotatedWith : directlyAnnotatedWith) {
                if (!annotatedWith.isInterface()) {
                    result.add(annotatedWith);
                    result.addAll(getSubTypesOf(annotatedWith));
                }
            }
        }

        return result;
    }

    /**
     * get types annotated with a given annotation, both classes and annotations
     * <p><b>@Inherited is not honored</b>, all annotations are considerd inherited from interfaces and types to their subtypes
     * <p/>depends on ClassAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Class<? extends Annotation> annotation) {
        depends(ClassAnnotationsScanner.class);

        Set<Class<?>> annotatedWith = Sets.newHashSet(forNames(store.get(ClassAnnotationsScanner.indexName, annotation.getName())));
        annotatedWith.addAll(getSubTypesOf(annotatedWith));

        return annotatedWith;
    }

    /**
     * get types annotated with a given annotation, both classes and annotations, including annotation member values matching
     * <p><b>@Inherited is honored</b>, that is all subtypes of types annotated with Inherited meta-annotation are returned as well
     * <p>Note that this meta-annotation type has no effect if the annotated type is used for anything other than a class.
     * Also, this meta-annotation causes annotations to be inherited only from superclasses; annotations on implemented interfaces have no effect.
     * <p/>depends on ClassAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWithInherited(final Annotation annotation) {
        return getMatchingAnnotations(
                getTypesAnnotatedWithInherited(annotation.annotationType()), annotation);
    }

    /**
     * get types annotated with a given annotation, both classes and annotations, including annotation member values matching
     * <p><b>@Inherited is not honored</b>, all annotations are considerd inherited from interfaces and types to their subtypes
     * <p/>depends on ClassAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Class<?>> getTypesAnnotatedWith(final Annotation annotation) {
        return getMatchingAnnotations(
                getTypesAnnotatedWith(annotation.annotationType()), annotation);
    }

    /**
     * get all methods annotated with a given annotation
     * <p/>depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Method> getMethodsAnnotatedWith(final Class<? extends Annotation> annotation) {
        depends(MethodAnnotationsScanner.class);

        Set<Method> result = Sets.newHashSet();
        Collection<String> annotatedWith = store.get(MethodAnnotationsScanner.indexName, annotation.getName());
        for (String annotated : annotatedWith) {
            result.add(getMethodFromDescriptor(annotated));
        }

        return result;
    }

    /**
     * get all methods annotated with a given annotation, including annotation member values matching
     * <p/>depends on MethodAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Method> getMethodsAnnotatedWith(final Annotation annotation) {
        return getMatchingAnnotations(
                getMethodsAnnotatedWith(annotation.annotationType()), annotation);
    }

    /**
     * get all fields annotated with a given annotation
     * <p/>depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Field> getFieldsAnnotatedWith(final Class<? extends Annotation> annotation) {
        depends(FieldAnnotationsScanner.class);

        final Set<Field> result = Sets.newHashSet();

        Collection<String> annotatedWith = store.get(FieldAnnotationsScanner.indexName, annotation.getName());
        for (String annotated : annotatedWith) {
            result.add(getFieldFromString(annotated));
        }

        return result;
    }

    /**
     * get all methods annotated with a given annotation, including annotation member values matching
     * <p/>depends on FieldAnnotationsScanner configured, otherwise an empty set is returned
     */
    public Set<Field> getFieldsAnnotatedWith(final Annotation annotation) {
        return getMatchingAnnotations(
                getFieldsAnnotatedWith(annotation.annotationType()), annotation);
    }

    /**
     * get 'converter' methods that could effectively convert from type 'from' to type 'to'
     *
     * @param from - the one and only parameter indicating the type to convert from
     * @param to   - the required return type
     *             <p/>
     *             depends on ConvertersScanner configured, otherwise an empty set is returned
     */
    public Set<Method> getConverters(final Class<?> from, final Class<?> to) {
        depends(ConvertersScanner.class);

        Set<Method> result = Sets.newHashSet();

        for (String converter : store.get(ConvertersScanner.indexName, ConvertersScanner.getConverterKey(from, to))) {
            result.add(getMethodFromDescriptor(converter));
        }

        return result;
    }

    /** return all super types of a given annotated element annotated with a given annotation up in hierarchy, including the given type */
    public static List<AnnotatedElement> getAllSuperTypesAnnotatedWith(final AnnotatedElement annotatedElement, final Annotation annotation) {
        final List<AnnotatedElement> annotated = Lists.newArrayList();

        if (annotatedElement != null) {
            if (annotatedElement.isAnnotationPresent(annotation.annotationType())) {
                annotated.add(annotatedElement);
            }

            if (annotatedElement instanceof Class<?>) {
                List<AnnotatedElement> subResult = Lists.newArrayList();
                Class<?> aClass = (Class<?>) annotatedElement;
                subResult.addAll(getAllSuperTypesAnnotatedWith(aClass.getSuperclass(), annotation));
                for (AnnotatedElement anInterface : aClass.getInterfaces()) {
                    subResult.addAll(getAllSuperTypesAnnotatedWith(anInterface, annotation));
                }
                annotated.addAll(subResult);
            }
        }

        return annotated;
    }

    //
    protected void depends(final Class<? extends Scanner> scannerClass) {
        if (scannerClasses != null && !scannerClasses.contains(scannerClass)) {
            log.error(format("scanner %s is not configured. add it to this Reflections configuration.", scannerClass.getSimpleName()));
        }
    }

    /**
     * recursivelly get all sub types of a given type fqn, including annotations 
     */
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

    /**
     * returns a subset of given annotatedWith, where annotation member values matches the given annotation
     */
    @SuppressWarnings("unchecked")
    protected <T extends AnnotatedElement> Set<T> getMatchingAnnotations(final Set<T> annotatedElements, final Annotation annotation) {
        Set<T> result = Sets.newHashSet();

        for (AnnotatedElement annotatedElement : annotatedElements) {
            if (isAnnotationMembersMatcing(annotation, annotatedElement)) {
                //noinspection unchecked
                result.add((T) annotatedElement);
            }
        }

        return result;
    }

    /**
     * checks for annotation member values matching on an annotated element or it's first annotated super type, based on equlaity of members
     * <p>override this to adopt a different annotation member values matching strategy
     */
    protected boolean isAnnotationMembersMatcing(final Annotation annotation1, final AnnotatedElement annotatedElement) {
        List<AnnotatedElement> elementList = getAllSuperTypesAnnotatedWith(annotatedElement, annotation1);

        if (!elementList.isEmpty()) {
            AnnotatedElement element = elementList.get(0);
            Annotation annotation2 = element.getAnnotation(annotation1.annotationType());

            if (annotation2 != null && annotation1.annotationType() == annotation2.annotationType()) {
                for (Method method : annotation1.annotationType().getDeclaredMethods()) {
                    try {
                        if (!method.invoke(annotation1).equals(method.invoke(annotation2))) {
                            return false;
                        }
                    } catch (Exception e) {
                        throw new ReflectionsException(format("could not invoke method %s on annotation %s", method.getName(), annotation1.annotationType()), e);
                    }
                }
                return true;
            }
        }

        return false;
    }

    public Method getMethodFromDescriptor(String descriptor) throws ReflectionsException {
        int p0 = descriptor.indexOf('(');
        String methodKey = descriptor.substring(0, p0);
        String methodParameters = descriptor.substring(p0 + 1, descriptor.length() - 1);

        int p1 = methodKey.lastIndexOf('.');
        String className = methodKey.substring(methodKey.lastIndexOf(' ') + 1, p1);
        String methodName = methodKey.substring(p1 + 1);

        Class<?>[] parameterTypes = null;
        if (!Utils.isEmpty(methodParameters)) {
            String[] parameterNames = methodParameters.split(", ");
            List<Class<?>> types = Utils.forNames(parameterNames);
            parameterTypes = types.toArray(new Class<?>[types.size()]);
        }

        try {
            return resolveType(className).getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new ReflectionsException("Can't resolve method named " + methodName, e);
        }
    }

    protected Field getFieldFromString(String field) {
        String className = field.substring(0, field.lastIndexOf('.'));
        String fieldName = field.substring(field.lastIndexOf('.') + 1);

        try {
            return resolveType(className).getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new ReflectionsException("Can't resolve field named " + fieldName, e);
        }
    }

    //
    private FileSelector qualifiedClassName = new FileSelector() {
        public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
            FileName fileName = fileInfo.getFile().getName();
//            System.out.print("qual check: "+fileInfo.getFile().getName()+" ");
//            if(fileName.getExtension().equals("class")) {
//                System.out.print("extension passed ");
//            }
//            if(configuration.getFilter().apply(fileName.getPath())) {
//                System.out.print("filter passed ");
//            }
            if(fileName.getExtension().equals("class") &&
                    configuration.getFilter().apply(fileName.getPath())) {
//                System.out.println(" passed");
                return true;
            } else {
//                System.out.println(" failed");
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
