package com.dazo66.betterclient.config;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.CategoryConfigEntry;
import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
@SuppressWarnings("unchecked")
public class ConfigReader {

    private final FileReader reader;

    public ConfigReader(FileReader readerIn) {
        reader = readerIn;
    }

    public ConfigReader(String fileName) throws FileNotFoundException {
        this(new File(fileName));
    }

    public ConfigReader(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }

    public List<AbstractConfigEntry> load() throws IOException {
        return assenbleConfigEntries(IOUtils.readLines(reader));
    }

    static final BiMap<String, Class<? extends AbstractConfigEntry>> TYPE_MAP_INVERSE;
    static final BiMap<String, String> CHAR_MAP = ConfigWriter.CHAR_MAP;
    static final BiMap<String, String> CHAR_MAP_INVERSE = CHAR_MAP.inverse();
    private final static String SEPARATOR = "=";
    private final static Map<Class, Function<String, Object>> FUNCTION_MAP = new HashMap<>();
    private final static Pattern pattern = Pattern.compile("");
    //EXAMPLE: "C:category1 {"
    private final static Pattern CATEGORY_START_P = Pattern.compile("[C]+:[a-zA-Z0-9_\\-]+[ ]*\\{[ ]*$");
    private final static Pattern CATEGORY_END_P = Pattern.compile("^}$");
    //EXAMPLE: "SA:array1:["
    private final static Pattern ARRAY_START_P = Pattern.compile("[A-Z]A:[a-zA-Z0-9_\\-]+=\\[[ ]*$");
    private final static Pattern ARRAY_END_P = Pattern.compile("^]$");
    //EXAMPLE: "B:key=true"
    private final static Pattern OTHER_ENTRY_P = Pattern.compile("[A-Z]:[a-zA-Z0-9_\\-]+=[ \\S]*");
    //EXAMPLE: "array1:["
    private final static Pattern COMMENT_ARRAY = Pattern.compile("[a-zA-Z0-9_\\-]+:[ ]*\\[$");
    private final static Pattern COMMENT_ONE = Pattern.compile("[a-zA-Z0-9_\\-]+=[ \\S]*");
    //EXAMPLE: "key=vaule"
    public static Map<String, Pair<Function, BiConsumer>> ATTRIBUTES_GET_SET_FUNC = new HashMap<>();

    static {
        FUNCTION_MAP.put(Boolean.class, Boolean::valueOf);
        FUNCTION_MAP.put(Integer.class, Integer::valueOf);
        FUNCTION_MAP.put(Double.class, Double::valueOf);
        FUNCTION_MAP.put(Float.class, Float::valueOf);
        FUNCTION_MAP.put(String.class, String::toString);

        TYPE_MAP_INVERSE = ConfigWriter.TYPE_MAP.inverse();

        Function<AbstractConfigEntry, Object> getLangKey = AbstractConfigEntry::getLangKey;
        BiConsumer<AbstractConfigEntry, String> setLangKey = AbstractConfigEntry::setLangKey;
        ATTRIBUTES_GET_SET_FUNC.put("langkey", new ImmutablePair(getLangKey, setLangKey));

//        Function<AbstractConfigEntry, Object> getValue = AbstractConfigEntry::getValue;
//        BiConsumer<AbstractConfigEntry, Object> setValue = AbstractConfigEntry::setValue;
//        ATTRIBUTES_GET_SET_FUNC.put("vaule", new ImmutablePair(getValue, setValue));

        Function<AbstractConfigEntry, Object> getComment = AbstractConfigEntry::getComment;
        BiConsumer<AbstractConfigEntry, String[]> setComment = AbstractConfigEntry::setComment;
        ATTRIBUTES_GET_SET_FUNC.put("comment", new ImmutablePair(getComment, getComment));

        Function<AbstractConfigEntry, Object> getDefaultValue = AbstractConfigEntry::getDefaultValue;
        BiConsumer<AbstractConfigEntry, Object> setDefaultValue = AbstractConfigEntry::setDefaultValue;
        ATTRIBUTES_GET_SET_FUNC.put("default_value", new ImmutablePair(getDefaultValue, setDefaultValue));

        Function<AbstractConfigEntry, Object> getIsShowInGui = AbstractConfigEntry::getIsShowInGui;
        BiConsumer<AbstractConfigEntry, Boolean> setIsShowInGui = AbstractConfigEntry::setIsShowInGui;
        ATTRIBUTES_GET_SET_FUNC.put("isshow", new ImmutablePair(getIsShowInGui, setIsShowInGui));

        Function<AbstractConfigEntry, Object> getPath = AbstractConfigEntry::getPath;
        BiConsumer<AbstractConfigEntry, CategoryConfigEntry> setPath = AbstractConfigEntry::setPath;
        ATTRIBUTES_GET_SET_FUNC.put("isshow", new ImmutablePair(getIsShowInGui, setIsShowInGui));
    }

    public static List<AbstractConfigEntry> assenbleConfigEntries(Collection<String> strings) {
        List<AbstractConfigEntry> list = Lists.newArrayList();
        List<Pair<Collection<String>, Collection<String>>> rawEntryList = spiltEntry(strings);
        for (Pair<Collection<String>, Collection<String>> pair : rawEntryList) {
            list.add(assenbleEntry(pair.getLeft(), pair.getRight()));
        }
        return list;
    }

    private static Class<? extends AbstractConfigEntry> getType(String s) {
        s = s.substring(0, s.indexOf(":"));
        return TYPE_MAP_INVERSE.get(s);
    }

    //记得返回entry
    private static AbstractConfigEntry setComment(Collection<String> comment, AbstractConfigEntry emptyEntry) {
        Iterator<String> iterator = comment.iterator();
        String s;
        Class type = getGenericInterface(emptyEntry.getClass());
        layer1:
        while (iterator.hasNext()) {
            s = iterator.next().trim();
            if (COMMENT_ARRAY.matcher(s).find()) {
                String key = getMid("", "[", s).trim();
                Function function = FUNCTION_MAP.get(type);
                List<String> list = new ArrayList<>();
                while (iterator.hasNext()) {
                    s = iterator.next().trim();
                    if (!ARRAY_END_P.matcher(s).find()) {
                        list.add(s);
                    } else {
                        break layer1;
                    }
                }
                List list1 = new ArrayList();
                for (String s1 : list) {
                    list1.add(function.apply(s1));
                }
                ATTRIBUTES_GET_SET_FUNC.get(key).getRight().accept(emptyEntry, list1);

            } else {
                Pair pair = assenbleToOne(s, type);
                ATTRIBUTES_GET_SET_FUNC.get(pair.getLeft()).getRight().accept(emptyEntry, pair.getRight());
            }
        }
        return emptyEntry;
    }

    private static Class getGenericInterface(Class clazz) {
        return (Class) ((ParameterizedType) clazz.getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }

    public static AbstractConfigEntry assenbleEntry(Collection<String> comment, Collection<String> body) {
        Iterator<String> botyI = body.iterator();
        String firstLine = botyI.next().trim();
        Matcher oneEntryM = OTHER_ENTRY_P.matcher(firstLine);
        AbstractConfigEntry emptyEntry = null;

        if (oneEntryM.find()) {
            Class<? extends AbstractConfigEntry> clazz = getType(oneEntryM.group());
            if (clazz != null) {
                Class type = getGenericInterface(clazz);
                try {
                    Constructor<? extends AbstractConfigEntry> constructor = clazz.getConstructor(String.class, String.class, type, String[].class);
                    String key = getMid(":", "=", firstLine).trim();
                    emptyEntry = constructor.newInstance(key, null, null, null);
                    emptyEntry.setValue(assenbleToOne(firstLine.substring(firstLine.indexOf(":") + 1), type).getRight());
                    return setComment(comment, emptyEntry);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ConfigLoadException(String.format("Unsupported config type:%s", firstLine));
            }
        }

        Matcher arrayEntryM = ARRAY_START_P.matcher(firstLine);
        if (arrayEntryM.find()) {
            Class<? extends AbstractConfigEntry> clazz = getType(oneEntryM.group());
            if (clazz != null) {
                Class type = getGenericInterface(clazz);
                Pair pair = assenbleToArray(body, type);
                try {
                    Constructor<? extends AbstractConfigEntry> constructor = clazz.getConstructor(String.class, pair.getRight().getClass(), type, String[].class);
                    emptyEntry = constructor.newInstance((String) pair.getLeft(), null, null, null);
                    emptyEntry.setValue(pair.getRight());
                    return setComment(comment, emptyEntry);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    e.printStackTrace();
                }
            } else {
                throw new ConfigLoadException(String.format("Unsupported config type:%s", firstLine));
            }

        }

        Matcher categotyEntryM = CATEGORY_START_P.matcher(firstLine);
        if (categotyEntryM.find()) {
            String key = getMid(":", "{", firstLine).trim();
            ArrayList<String> list = new ArrayList<>(body);
            list.remove(0);
            list.remove(list.size() - 1);
            CategoryConfigEntry category = new CategoryConfigEntry(key, null, null);
            category.setValue(assenbleConfigEntries(list));
            return setComment(comment, category);
        }

        throw new ConfigLoadException(String.format("Unsupported config type:%s", firstLine));

    }

    private static List<Pair<Collection<String>, Collection<String>>> spiltEntry(Collection<String> list) {
        List<Pair<Collection<String>, Collection<String>>> rawPairList = new ArrayList<>();
        ArrayList<String> commentBuffer = Lists.newArrayList();
        ArrayList<String> bodyBuffer = Lists.newArrayList();
        Stack<String> stack = new Stack<>();
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()) {
            String s = iterator.next().trim();
            if (CATEGORY_START_P.matcher(s).find()) {
                bodyBuffer.add(s);
                stack.push("{");
            } else if (CATEGORY_END_P.matcher(s).find()) {
                if (stack.size() == 0) {
                    throw new ConfigLoadException(s);
                }
                if (CHAR_MAP_INVERSE.get(stack.pop()).equals(s)) {
                    bodyBuffer.add(s);
                    if (stack.size() == 0) {
                        rawPairList.add(new ImmutablePair<>((Collection<String>) commentBuffer.clone(), (Collection<String>) bodyBuffer.clone()));
                        commentBuffer.clear();
                        bodyBuffer.clear();
                    }
                } else {
                    //符号验证失败
                    throw new ConfigLoadException(s);
                }
            } else if (s.startsWith("#")) {
                String s1 = s.substring(1).trim();
                commentBuffer.add(s.substring(1).trim());
                if (COMMENT_ARRAY.matcher(s1).find()) {
                    s = iterator.next();
                    while (!ARRAY_END_P.matcher(s).find()) {
                        commentBuffer.add(s);
                        s = iterator.next();
                    }
                    commentBuffer.add(s);
                }
            } else if (OTHER_ENTRY_P.matcher(s).find()) {
                bodyBuffer.add(s);
                if (stack.size() == 0) {
                    rawPairList.add(new ImmutablePair<>((Collection<String>) commentBuffer.clone(), (Collection<String>) bodyBuffer.clone()));
                    commentBuffer.clear();
                    bodyBuffer.clear();
                }
            } else if (ARRAY_START_P.matcher(s).find()) {
                while (!ARRAY_END_P.matcher(s).find()) {
                    bodyBuffer.add(s);
                    s = iterator.next();
                }
                bodyBuffer.add(s);
            }
        }
        return rawPairList;
    }

    private static String getMid(String split1, String split2, String s) {
        int index = s.indexOf(split1) + split1.length();
        return s.substring(index, s.indexOf(split2, index));
    }

    private static <T> Pair<String, T[]> assenbleToArray(Collection<String> rawArrayEntry, Class<T> clazz) {
        Iterator lineIterator = rawArrayEntry.iterator();
        String firstLine = (String) lineIterator.next();
        String key = getMid(":", "=", firstLine).trim();
        List<T> list = new ArrayList<>();
        String s;
        while (lineIterator.hasNext()) {
            s = (String) lineIterator.next();
            if (ARRAY_END_P.matcher(s).find()) {
                return new ImmutablePair<>(key, (T[]) list.toArray());
            }
            list.add(stringToObj(s, clazz));
        }
        return new ImmutablePair<>(key, (T[]) new Object[]{});
    }

    private static <V> Pair<String, V> assenbleToOne(String s, Class<? extends V> clazz) {
        String raw = s.trim();
        int index = raw.lastIndexOf(SEPARATOR);
        String left = raw.substring(0, index);
        String right = raw.substring(index + 1);
        return new ImmutablePair<>(left, stringToObj(right, clazz));
    }

    private static <T> T stringToObj(String s, Class<? extends T> clazz) {
        try {
            return (T) FUNCTION_MAP.get(clazz).apply(s);
        } catch (Exception e) {
            if (e instanceof NullPointerException) {
                throw new ConfigLoadException(String.format("The Type Of String:%s Are Not In [Boolean, Integer, Double, Float, String]", s));
            } else {
                throw new ConfigLoadException(String.format("Can't Conversion String:%s to [%s]", s, clazz.getSimpleName()));
            }
        }
    }

    private static class ConfigLoadException extends IllegalArgumentException {
        ConfigLoadException(String s) {
            super(s);
        }
    }

}
