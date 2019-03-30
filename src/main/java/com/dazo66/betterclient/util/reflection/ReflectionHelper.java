package com.dazo66.betterclient.util.reflection;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.io.Resources;
import joptsimple.internal.Strings;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.util.*;

import static com.dazo66.betterclient.BetterClient.MODID;

/**
 * @author Dazo66
 */
public class ReflectionHelper {

    /* Public Util Method Start */

    public static final HashMap<String, Class<?>> TYPE_MAP = new HashMap<>();
    private static ReflectionHelper instance = new ReflectionHelper();

    static {
        TYPE_MAP.put("V", void.class);
        TYPE_MAP.put("Z", boolean.class);
        TYPE_MAP.put("C", char.class);
        TYPE_MAP.put("B", byte.class);
        TYPE_MAP.put("S", short.class);
        TYPE_MAP.put("I", int.class);
        TYPE_MAP.put("F", float.class);
        TYPE_MAP.put("J", long.class);
        TYPE_MAP.put("D", double.class);
        //        TYPE_MAP.put("[Z", boolean[].class);
        //        TYPE_MAP.put("[C",char[].class);
        //        TYPE_MAP.put("[B",byte[].class);
        //        TYPE_MAP.put("[S",short[].class);
        //        TYPE_MAP.put("[I",int[].class);
        //        TYPE_MAP.put("[F",float[].class);
        //        TYPE_MAP.put("[J",long[].class);
        //        TYPE_MAP.put("[D",double[].class);
    }

    private List<URL> defines = Lists.newArrayList();
    private Logger logger = LogManager.getLogger(MODID);
    private Multimap<Class<?>, ReflectionInfo> reflectionInfoMap = Multimaps.newSetMultimap(new HashMap<>(), HashSet::new);

    private ReflectionHelper() {
    }

    /**
     * tryInvoke the specified method with instance and paras
     *
     * @param method   specified method
     * @param instance instance to tryInvoke method
     *                 if static method the instance should be null
     * @param paras    the paras
     * @param <T>      return type
     * @param <E>      instance type
     * @return the method return or null if have any Exception
     */
    @SuppressWarnings("unchecked")
    public static <T, E> T invoke(Method method, E instance, Object... paras) {
        try {
            return (T) method.invoke(instance, paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get Constructor form fromClass with parameterTypes
     *
     * @param fromClass      from class
     * @param parameterTypes the Constructor's paras
     * @param <T>            class type
     * @return return the Constructor or null if have any Exception
     */
    @Nonnull
    public static <T> Constructor<T> getConstructorStatic(@Nonnull final Class<T> fromClass, @Nonnull final Class<?>... parameterTypes) {

        try {
            return tryGetConstructorStatic(fromClass, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * new instance with specified class
     * parameters could not be null
     * if has parameters must have null
     * please use method :
     * {@link ReflectionHelper#newInstanceStatic(Class, Class[], Object...)} ()}
     *
     * @param fromClass from class
     * @param paras     paras
     * @param <T>       Class type
     * @return new Class instance or null if have any Exception
     */
    public static <T> T newInstanceFromClassStatic(Class<T> fromClass, Object... paras) {

        try {
            return tryNewInstanceFromClassStatic(fromClass, paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * new instance with specified class
     * and specified paras classes
     *
     * @param fromClass  from class
     * @param parasClass the paras classes
     * @param paras      paras
     * @param <T>        Class type
     * @return new Class instance or null if have any Exception
     */
    public static <T> T newInstanceStatic(Class<T> fromClass, Class<?>[] parasClass, Object... paras) {

        try {
            return tryNewInstanceStatic(fromClass, parasClass, paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * transform the String to Class Instance;
     * <p>
     * "I" -> int.class
     * "[I" -> int[].class
     * "Ljava.lang.String" -> String.class
     * "Ljava/lang/String;" ->String.class
     * <p>
     * ETC...
     *
     * @param raw the raw type name;
     * @return Class instance or null if have any Exception
     */
    public static Class<?> getType(String raw) throws ClassNotFoundException {
        if (raw.startsWith("L")) {
            raw = raw.substring(1);
            if (raw.endsWith(";")) {
                raw = raw.substring(0, raw.length() - 1);
            }
            if (raw.contains("/")) {
                raw = raw.replace('/', '.');
            }
            return Launch.classLoader.findClass(raw);
        }
        if (raw.startsWith("[")) {
            if (!raw.endsWith(";")) {
                raw += ';';
            }
            return Launch.classLoader.findClass(raw);
        }
        Class<?> res = TYPE_MAP.get(raw);
        if (res == null) {
            throw new ClassNotFoundException();
        }
        return res;
    }

    public static ReflectionHelper getInstance() {
        return instance;
    }

    /**
     * tryInvoke the specified method with instance and paras
     *
     * @param method   specified method
     * @param instance instance to tryInvoke method
     *                 if static method the instance should be null
     * @param paras    the paras
     * @param <T>      return type
     * @param <E>      instance type
     * @return the method return
     * @throws InvocationTargetException instance can't tryInvoke this method
     * @throws IllegalAccessException    access will always legal
     */
    @SuppressWarnings("unchecked")
    private static <T, E> T tryInvoke(Method method, E instance, Object... paras) throws InvocationTargetException, IllegalAccessException {
        return (T) method.invoke(instance, paras);
    }

    private static Field findField(Class<?> clazz, String... fieldNames) throws NoSuchFieldException {
        Field f;
        for (String fieldName : fieldNames) {
            try {
                f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Exception ignore) {
            }
        }
        throw new NoSuchFieldException();
    }

    /**
     * get Constructor form fromClass with parameterTypes
     *
     * @param fromClass      from class
     * @param parameterTypes the Constructor's paras
     * @param <T>            class type
     * @return return the
     * @throws NoSuchMethodException can't find Constructor
     */
    @Nonnull
    private static <T> Constructor<T> tryGetConstructorStatic(@Nonnull final Class<T> fromClass, @Nonnull final Class<?>... parameterTypes) throws NoSuchMethodException {

        Preconditions.checkNotNull(fromClass, "class");
        Preconditions.checkNotNull(parameterTypes, "parameter types");
        final Constructor<T> constructor;
        try {
            constructor = fromClass.getDeclaredConstructor(parameterTypes);
            constructor.setAccessible(true);
        } catch (final NoSuchMethodException e) {
            final StringBuilder desc = new StringBuilder();
            desc.append(fromClass.getSimpleName()).append('(');
            for (int i = 0, length = parameterTypes.length; i < length; i++) {
                desc.append(parameterTypes[i].getName());
                if (i != length - 1) {
                    desc.append(',').append(' ');
                }
            }
            desc.append(')');
            throw new NoSuchMethodException("Could not find constructor '" + desc.toString() + "' in " + fromClass);
        }
        return constructor;
    }

    /* Public Method End */

    /**
     * new instance with specified class
     * parameters could not be null
     * if has parameters must have null
     * please use method :
     * {@link ReflectionHelper#newInstanceStatic(Class, Class[], Object...)} ()}
     *
     * @param fromClass from class
     * @param paras     paras
     * @param <T>       Class type
     * @return new Class instance
     * @throws IllegalAccessException    never throw
     * @throws InvocationTargetException if the underlying
     *                                   constructor throws an exception.
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class.
     * @throws NoSuchMethodException     can't find this ConstructorStatic
     */
    private static <T> T tryNewInstanceFromClassStatic(Class<T> fromClass, Object... paras) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        Objects.requireNonNull(fromClass);
        for (Object o : paras) {
            Objects.requireNonNull(o);
        }
        return newInstanceFromConstructorStatic(tryGetConstructorStatic(fromClass, instanceArrayToClassInstance(paras)), paras);
    }

    /**
     * new instance with specified class
     * and specified paras classes
     *
     * @param fromClass  from class
     * @param parasClass the paras classes
     * @param paras      paras
     * @param <T>        Class type
     * @return new Class instance
     * @throws IllegalAccessException    never throw
     * @throws InvocationTargetException if the underlying
     *                                   constructor throws an exception.
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class.
     * @throws NoSuchMethodException     can't find this ConstructorStatic
     */
    private static <T> T tryNewInstanceStatic(Class<T> fromClass, Class<?>[] parasClass, Object... paras) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        Objects.requireNonNull(fromClass);
        return newInstanceFromConstructorStatic(tryGetConstructorStatic(fromClass, parasClass), paras);
    }

    @Nonnull
    private static Method findMethod(@Nonnull Class<?> clazz, @Nonnull String methodName, @Nullable String srgName, String notchName, Class<?>... parameterTypes) throws NoSuchMethodException {
        Preconditions.checkNotNull(clazz);
        Preconditions.checkArgument(StringUtils.isNotEmpty(methodName), "Method name cannot be empty");
        Method m = null;
        for (String nameToFind : Arrays.asList(methodName, srgName, notchName)) {
            try {
                m = clazz.getDeclaredMethod(nameToFind, parameterTypes);
                m.setAccessible(true);
                return m;
            } catch (Exception ignore) {
            }
        }
        if (m == null) {
            throw new NoSuchMethodException("MethodName: " + methodName + " " + srgName + " " + notchName);
        }
        return m;
    }

    private static Class[] instanceArrayToClassInstance(Object... paras) {
        Class[] classes = new Class[paras.length];
        for (int i = 0; i < paras.length; i++) {
            classes[i] = paras[i].getClass();
        }
        return classes;
    }

    private static <T> T newInstanceFromConstructorStatic(Constructor<T> constructor, Object... paras) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        return constructor.newInstance(paras);
    }

    private static String getNotchName(String srgName) {
        try {
            return srgName.substring(srgName.lastIndexOf('_') + 1);
        } catch (Exception ignore) {
        }
        return srgName;
    }

    /**
     * get Method whit define key
     * method name and obfName will get from define file.
     * if Launch.blackboard.get("fml.deobfuscatedEnvironment") == true
     * then use key to find method either use obfName
     * <p>
     * usage: ReflectionHelper.getInstance().getMethod(...);
     *
     * @param fromClass find method in this class
     * @param key       method define key
     * @return the Method instance or null if have any Exception
     */
    public Method getMethod(Class<?> fromClass, String key) {

        try {
            return tryGetMethod(fromClass, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get Method with Method key and Method paras
     * method obf name will find in define file
     * if Launch.blackboard.get("fml.deobfuscatedEnvironment") == true
     * then use key to find method either use obfName
     * <p>
     * usage: ReflectionHelper.getInstance().tryGetMethod(...);
     *
     * @param fromClass find method in this class
     * @param key       method key
     * @param paras     method paras
     * @return method instance or null if have any Exception
     */
    @Deprecated
    public Method getMethod(Class<?> fromClass, String key, Class<?>... paras) {

        try {
            return tryGetMethod(fromClass, key, paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * tryInvoke the method with method key
     * method name and obfName will get in define file
     * <p>
     * usage: ReflectionHelper.getInstance().tryInvoke(...);
     *
     * @param fromClass find method in this class
     * @param methodKey the key to get method name and obfName
     * @param instance  specified method
     * @param paras     the paras
     * @param <T>       return type
     * @param <E>       instance type
     * @return method return type or null if have any Exception
     */
    @SuppressWarnings("unchecked")
    public <T, E> T invoke(Class<? extends E> fromClass, String methodKey, E instance, Object... paras) {
        Method method = getMethod(fromClass, methodKey);
        if (method == null) {
            return null;
        }
        return (T) invoke(method, instance, paras);
    }

    /**
     * get Constructor form fromClass with parameterTypes
     *
     * @param fromClass      from class
     * @param parameterTypes the Constructor's paras
     * @param <T>            class type
     * @return return the Constructor or null if have any Exception
     */
    @Nonnull
    public <T> Constructor<T> getConstructor(@Nonnull final Class<T> fromClass, @Nonnull final Class<?>... parameterTypes) {

        try {
            return tryGetConstructorStatic(fromClass, parameterTypes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * new instance with specified class
     * parameters could not be null
     * if has parameters must have null
     * please use method :
     * {@link ReflectionHelper#newInstanceStatic(Class, Class[], Object...)} ()}
     *
     * @param fromClass from class
     * @param paras     paras
     * @param <T>       Class type
     * @return new Class instance or null if have any Exception
     */
    public <T> T newInstanceFromClass(Class<T> fromClass, Object... paras) {

        try {
            return tryNewInstanceFromClassStatic(fromClass, paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * new instance with specified class
     * and specified paras classes
     *
     * @param fromClass  from class
     * @param parasClass the paras classes
     * @param paras      paras
     * @param <T>        Class type
     * @return new Class instance or null if have any Exception
     */
    public <T> T newInstance(Class<T> fromClass, Class<?>[] parasClass, Object... paras) {

        try {
            return tryNewInstanceStatic(fromClass, parasClass, paras);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //    private List<ReflectionInfo> findInfoFormList(Collection<ReflectionInfo> list, String key){
    //        List<ReflectionInfo> res = new ArrayList<>();
    //        if (list != null) {
    //            for (ReflectionInfo info : list) {
    //                if (info.key.equals(key)) {
    //                    res.add(info);
    //                }
    //            }
    //        }
    //        return res;
    //    }
    //
    //    private List<ReflectionInfo> findInfoFormListWithName(Collection<ReflectionInfo> list, String key){
    //        List<ReflectionInfo> res = new ArrayList<>();
    //        if (list != null) {
    //            for (ReflectionInfo info : list) {
    //                if (info.key.equals(key)) {
    //                    res.add(info);
    //                }
    //            }
    //        }
    //        return res;
    //    }

    /**
     * get field with specified key
     * field name and obfName will find
     * in define file
     *
     * @param fromClass fromClass
     * @param key       the define key
     * @return the field instance
     */
    public Field getField(Class<?> fromClass, String key) {

        try {
            return tryGetField(fromClass, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * get field value with specified key
     * field name and obfName will find
     * in define file
     *
     * @param fromClass from class
     * @param fieldKey  field key
     * @param instance  instance by from class
     * @param <T>       class type
     * @param <E>       value type
     * @return value of field or null if have any Exception
     */
    @SuppressWarnings("unchecked")
    public <T, E> E getValue(Class<? super T> fromClass, String fieldKey, T instance) {

        try {
            return (E) Objects.requireNonNull(tryGetField(fromClass, fieldKey)).get(instance);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * set field value with specified key
     * field name and obfName will find
     * in define file
     *
     * @param fromClass from class
     * @param instance  instance by from class
     * @param value     the value of will set
     * @param <T>       class type
     * @param <V>       new value type
     */
    public <T, V> void setValue(Class<? super T> fromClass, String name, T instance, V value) {

        try {
            trySetValue(fromClass, name, instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T, V> void setFinalValue(Class<? super T> fromClass, String name, T instance, V value) {

        try {
            trySetFinalValue(fromClass, name, instance, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * add define file to instance
     * file format:
     * ClassName(class name) key(unique) name(field name or method name) obfName [desc](only method need)
     * ClassName : xx.xx.xx.Xx
     * key : xxx..
     * name : xxx..
     * obfName : func_xxxx or field_xxxx
     * desc : (xxxx)xxxx
     * <p>
     * can use '#' to mark comment :
     * example : #Commant
     * java.lang.String string hash hash #String.hash field
     * <p>
     * Class a is the sub class of Class A
     * if you define : "A key name obfName"
     * you can use this code get instance a's field from A class:
     * ReflectionHelper.getInstance().getField(a.class, key);
     * <p>
     * usage ReflectionHelper.getInstance().addDefineFile("xxx.xxx");
     * <p>
     * file can put in jar file or runDir
     *
     * @param file file name
     * @return return read info from file success or fail
     */
    public boolean addDefineFile(String file) {

        try {
            URL resource;
            File file1 = new File(file);
            if (file1.exists()) {
                resource = file1.toURI().toURL();
            } else {
                resource = Resources.getResource(file);
            }
            if (defines.contains(resource)) {
                logger.warn("define field: {} was loaded!", file);
                return false;
            }
            defines.add(resource);
            List<String> lineList = IOUtils.readLines(resource.openStream(), "UTF-8");
            for (String line : lineList) {
                if (Strings.isNullOrEmpty(line)) {
                    continue;
                }
                boolean b = false;
                try {
                    b = !parseLine(line);
                } catch (Exception ignore) {
                }
                if (b) {
                    logger.warn("Parse Reflection Info Exception at File: {} LineNum: {} Line: {}", file, lineList.indexOf(line), line);
                }
            }
            return true;
        } catch (Exception e) {
            logger.warn("Load Reflection Info File: {} Exception", file);
            return false;
        }
    }

    protected boolean parseLine(String defineLine) {
        defineLine = defineLine.trim();
        if (defineLine.startsWith("#")) {
            return true;
        }
        String string = Iterables.getFirst(Splitter.on('#').limit(2).split(defineLine), "").trim();
        List<String> list = Lists.newArrayList(Splitter.on(" ").trimResults().split(string));
        if (list.size() > 5 || list.size() < 4) {
            return false;
        }
        Class<?> fromClass = null;
        try {
            if (list.get(0).contains("/")) {
                list.set(0, list.get(0).replace("/", "."));
            }
            if ("*".equals(list.get(0))) {
                list.set(0, "java.lang.Object");
            }
            fromClass = Launch.classLoader.findClass(list.get(0));
        } catch (ClassNotFoundException e) {
            logger.warn("Wrong Class Name: {}", list.get(0));
            return false;
        }
        String name;
        String obfName = null;
        String desc = null;
        ReflectionInfo info;
        name = list.get(2);
        obfName = list.get(3);
        //class key name obfName null
        if (list.size() == 5) {
            //class key name obfName desc
            desc = list.get(4);
        }
        try {
            info = new ReflectionInfo(fromClass, list.get(1), name, obfName, desc);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        Collection<ReflectionInfo> l = reflectionInfoMap.get(info.fromClass);
        if (l != null && !l.isEmpty()) {
            for (ReflectionInfo info1 : l) {
                if (info1.key.equals(info.key)) {
                    logger.warn("Contains key: {} at class: {}", list.get(1), list.get(0));
                    return false;
                }
            }
        }
        reflectionInfoMap.put(info.fromClass, info);
        return true;
    }

    private List<ReflectionInfo> findInfoFromMap(Class<?> clazz, String key) {

        List<ReflectionInfo> list = new ArrayList<>();
        for (Class clazz1 : reflectionInfoMap.keySet()) {
            Class clazz2 = null;
            if (ClassUtils.isAssignable(clazz, clazz1)) {
                list.addAll(reflectionInfoMap.get(clazz1));
            }
        }
        list.removeIf(reflectionInfo -> !reflectionInfo.key.equals(key));
        return list;
    }

    private ReflectionInfo findMethodWithKey(Class<?> fromClass, String key) {
        List<ReflectionInfo> list = findInfoFromMap(fromClass, key);
        for (ReflectionInfo info : list) {
            if (info.isMethod() && info.key.equals(key)) {
                return info;
            }
        }
        return null;
    }

    private ReflectionInfo findMethodFromInfos(Class<?> fromClass, String key, Class<?>... paras) {
        List<ReflectionInfo> list = findInfoFromMap(fromClass, key);
        for (ReflectionInfo info : list) {
            if (info.isMethod() && Arrays.equals(paras, info.paramers)) {
                return info;
            }
        }
        return null;
    }

    private ReflectionInfo findFieldFromInfos(Class<?> fromClass, String key) {
        List<ReflectionInfo> list = findInfoFromMap(fromClass, key);
        for (ReflectionInfo info : list) {
            if (info.isField()) {
                return info;
            }
        }
        return null;
    }

    /**
     * get Method whit define key
     * method name and obfName will get from define file.
     * if Launch.blackboard.get("fml.deobfuscatedEnvironment") == true
     * then use key to find method either use obfName
     * <p>
     * usage: ReflectionHelper.getInstance().tryGetMethod(...);
     *
     * @param fromClass find method in this class
     * @param key       method define key
     * @return the Method instance
     * @throws NoSuchMethodException        The fromClass can't find method
     * @throws MethodNameNotDefineException the key not define in define file
     */
    private Method tryGetMethod(Class<?> fromClass, String key) throws NoSuchMethodException {
        ReflectionInfo info = findMethodWithKey(fromClass, key);
        if (info == null) {
            logger.warn("Method :{}.{} was not define", fromClass.getName(), key);
            throw new MethodNameNotDefineException(String.format("Method :%s.%s was not define", fromClass.getName(), key));
        }
        Method method = findMethod(fromClass, info.name, info.obfName, getNotchName(info.obfName), info.paramers);
        if (method == null) {
            logger.warn("Can't find method: {} in Class: {}", fromClass.getName(), key);
        }
        return method;
    }

    /**
     * get Method with Method key and Method paras
     * method obf name will find in define file
     * if Launch.blackboard.get("fml.deobfuscatedEnvironment") == true
     * then use key to find method either use obfName
     * <p>
     * usage: ReflectionHelper.getInstance().tryGetMethod(...);
     *
     * @param fromClass find method in this class
     * @param key       method key
     * @param paras     method paras
     * @return method instance
     * @throws NoSuchMethodException if method can't find in fromClass throw this Exception
     */
    @Deprecated
    private Method tryGetMethod(Class<?> fromClass, String key, Class<?>... paras) throws NoSuchMethodException {
        ReflectionInfo info = findMethodFromInfos(fromClass, key, paras);
        if (info == null) {
            logger.warn("Method :{}.{} was not define", fromClass.getName(), key);
            throw new MethodNameNotDefineException(String.format("Method :%s.%s not define", fromClass.getName(), key));
        }
        Method method = findMethod(fromClass, info.name, info.obfName, getNotchName(info.obfName), info.paramers);
        if (method == null) {
            logger.warn("Can't find method: {} in Class: {}", fromClass.getName(), key);
        }
        return method;
    }

    /**
     * tryInvoke the method with method key
     * method name and obfName will get in define file
     * <p>
     * usage: ReflectionHelper.getInstance().tryInvoke(...);
     *
     * @param fromClass find method in this class
     * @param methodKey the key to get method name and obfName
     * @param instance  specified method
     * @param paras     the paras
     * @param <T>       return type
     * @param <E>       instance type
     * @return method return type
     * @throws InvocationTargetException instance can't tryInvoke this method
     * @throws IllegalAccessException    never throw
     * @throws NoSuchMethodException     can't find method
     */
    @SuppressWarnings("unchecked")
    private <T, E> T tryInvoke(Class<? super E> fromClass, String methodKey, E instance, Object... paras) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = tryGetMethod(fromClass, methodKey);
        return (T) tryInvoke(method, instance, paras);
    }

    /**
     * get Constructor form fromClass with parameterTypes
     *
     * @param fromClass      from class
     * @param parameterTypes the Constructor's paras
     * @param <T>            class type
     * @return return the
     * @throws NoSuchMethodException can't find Constructor
     */
    @Nonnull
    private <T> Constructor<T> tryGetConstructor(@Nonnull final Class<T> fromClass, @Nonnull final Class<?>... parameterTypes) throws NoSuchMethodException {

        return tryGetConstructorStatic(fromClass, parameterTypes);
    }

    /**
     * new instance with specified class
     * parameters could not be null
     * if has parameters must have null
     * please use method :
     * {@link ReflectionHelper#newInstanceStatic(Class, Class[], Object...)} ()}
     *
     * @param fromClass from class
     * @param paras     paras
     * @param <T>       Class type
     * @return new Class instance
     * @throws IllegalAccessException    never throw
     * @throws InvocationTargetException if the underlying
     *                                   constructor throws an exception.
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class.
     * @throws NoSuchMethodException     can't find this ConstructorStatic
     */
    private <T> T tryNewInstanceFromClass(Class<T> fromClass, Object... paras) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        return tryNewInstanceFromClassStatic(fromClass, paras);
    }

    /**
     * new instance with specified class
     * and specified paras classes
     *
     * @param fromClass  from class
     * @param parasClass the paras classes
     * @param paras      paras
     * @param <T>        Class type
     * @return new Class instance
     * @throws IllegalAccessException    never throw
     * @throws InvocationTargetException if the underlying
     *                                   constructor throws an exception.
     * @throws InstantiationException    if the class that declares the
     *                                   underlying constructor represents an abstract class.
     * @throws NoSuchMethodException     can't find this ConstructorStatic
     */
    private <T> T tryNewInstance(Class<T> fromClass, Class<?>[] parasClass, Object... paras) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {

        return tryNewInstanceStatic(fromClass, parasClass, paras);
    }

    /**
     * get field with specified key
     * field name and obfName will find
     * in define file
     *
     * @param fromClass fromClass
     * @param key       the define key
     * @return the field instance
     * @throws NoSuchFieldException        can't find field at specified class
     * @throws FieldNameNotDefineException field key not exist in define file
     */
    private Field tryGetField(Class<?> fromClass, String key) throws NoSuchFieldException {
        ReflectionInfo info = findFieldFromInfos(fromClass, key);
        if (info == null) {
            logger.warn("Field :{}.{} was not define", fromClass.getName(), key);
            throw new FieldNameNotDefineException(String.format("Field :%s.%s not define", fromClass.getName(), key));
        }
        Field field;
        try {
            field = findField(fromClass, info.name, info.obfName, getNotchName(info.obfName));
        } catch (Exception e) {
            logger.warn("Field :{} {} {} {} can't find in class {} ", key, info.name, info.obfName, getNotchName(info.obfName), fromClass.getName());
            throw e;
        }
        ;
        return field;
    }

    /**
     * get field value with specified key
     * field name and obfName will find
     * in define file
     *
     * @param fromClass from class
     * @param fieldKey  field key
     * @param instance  instance by from class
     * @param <T>       class type
     * @param <E>       value type
     * @return value of field
     * @throws IllegalAccessException never throw
     * @throws NoSuchFieldException   can't find field in from class
     */
    @SuppressWarnings("unchecked")
    private <T, E> E tryGetValue(Class<? super T> fromClass, String fieldKey, T instance) throws IllegalAccessException, NoSuchFieldException {

        return (E) Objects.requireNonNull(tryGetField(fromClass, fieldKey)).get(instance);
    }

    /**
     * set field value with specified key
     * field name and obfName will find
     * in define file
     *
     * @param fromClass from class
     * @param instance  instance by from class
     * @param value     the value of will set
     * @param <T>       class type
     * @param <V>       new value type
     * @throws IllegalAccessException never throw
     * @throws NoSuchFieldException   can't find field in from class
     */
    private <T, V> void trySetValue(Class<? super T> fromClass, String name, T instance, V value) throws IllegalAccessException, NoSuchFieldException {

        Objects.requireNonNull(tryGetField(fromClass, name)).set(instance, value);
    }

    private <T, V> void trySetFinalValue(Class<? super T> fromClass, String name, T instance, V value) throws IllegalAccessException, NoSuchFieldException {

        Field field = Objects.requireNonNull(tryGetField(fromClass, name));
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        field.set(instance, value);
    }

    public static class FieldNameNotDefineException extends NoSuchFieldException {
        FieldNameNotDefineException(String s) {
            super(s);
        }
    }

    public static class MethodNameNotDefineException extends NoSuchMethodException {
        MethodNameNotDefineException(String s) {
            super(s);
        }
    }

    protected class ReflectionInfo {

        private Class<?> fromClass;
        private String key;
        private String name;
        private String obfName;
        private Class<?>[] paramers;
        private Class<?> returnType;
        private boolean isField;

        protected ReflectionInfo(Class<?> fromClass, String key, String name, String obfName, String desc) throws ClassNotFoundException {

            this.key = key;
            this.fromClass = fromClass;
            this.name = name;
            this.obfName = obfName;
            isField = Strings.isNullOrEmpty(desc);
            descInit(desc);
        }

        protected void descInit(String desc) throws ClassNotFoundException {
            if (!isField) {
                int index = desc.indexOf(')');
                String paras = desc.substring(1, index);
                ArrayList<Class<?>> paraList = new ArrayList<>();
                while (!paras.isEmpty()) {
                    if (paras.startsWith("L") || paras.startsWith("[L")) {
                        int next = paras.indexOf(';');
                        paraList.add(getType(paras.substring(0, next + 1)));
                        paras = paras.substring(next + 1);
                        continue;
                    }
                    for (String s : TYPE_MAP.keySet()) {
                        if (paras.startsWith(s)) {
                            paraList.add(TYPE_MAP.get(s));
                            paras = paras.substring(s.length());
                        }
                    }
                }
                paramers = new Class<?>[paraList.size()];
                paraList.toArray(paramers);
                returnType = getType(desc.substring(index + 1));
            } else {
                paramers = new Class[0];
            }
        }

        boolean isField() {
            return isField;
        }

        boolean isMethod() {
            return !isField;
        }

    }

}
