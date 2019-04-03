package com.dazo66.betterclient.config;

import com.dazo66.betterclient.config.configentrys.AbstractConfigEntry;
import com.dazo66.betterclient.config.configentrys.CategoryConfigEntry;
import com.google.common.collect.BiMap;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
public class ConfigReader {

    private final static String SEPARATOR = "=";
    private final static Map<Class, Function<String, Object>> FUNCTION_MAP = new HashMap<>();

    private final static Pattern pattern = Pattern.compile("");
    private final static Pattern CATEGORY_START_P = Pattern.compile("[A-Z]+:[a-zA-Z0-9 ]+\\{");
    private final static Pattern CATEGORY_END_P = Pattern.compile("^}$");
    private final static Pattern ARRAY_START_P = Pattern.compile("[a-zA-Z0-9]+:[ \\[]+");
    private final static Pattern ARRAY_END_P = Pattern.compile("^]$");
    private final static Pattern OTHER_ENTRY_P = Pattern.compile("[a-zA-Z0-9]+=\\S*");

    static final BiMap<String, Class<? extends AbstractConfigEntry>> TYPE_MAP_INVERSE;
    static final BiMap<String, String> CHAR_MAP = ConfigWriter.CHAR_MAP;
    static final BiMap<String, String> CHAR_MAP_INVERSE = CHAR_MAP.inverse();

    private final FileReader reader;
    private LineIterator lineIterator;
    private List<String> commentBuffer = new ArrayList<>();


    private static Map<String, Pair<Function, BiConsumer>>
            ATTRIBUTES_GET_SET_FUNC
            = new HashMap();

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

        Function<AbstractConfigEntry, Object> getValue = AbstractConfigEntry::getValue;
        BiConsumer<AbstractConfigEntry, Object> setValue = AbstractConfigEntry::setValue;
        ATTRIBUTES_GET_SET_FUNC.put("vaule", new ImmutablePair(getValue, setValue));

        Function<AbstractConfigEntry, Object> getComment = AbstractConfigEntry::getComment;
        BiConsumer<AbstractConfigEntry, String[]> setComment = AbstractConfigEntry::setComment;
        ATTRIBUTES_GET_SET_FUNC.put("comment", new ImmutablePair(getComment, getComment));

        Function<AbstractConfigEntry, Object> getDefaultValue = AbstractConfigEntry::getDefaultValue;
        BiConsumer<AbstractConfigEntry, Object> setDefaultValue = AbstractConfigEntry::setDefaultValue;
        ATTRIBUTES_GET_SET_FUNC.put("comment", new ImmutablePair(getDefaultValue, setDefaultValue));

        Function<AbstractConfigEntry, Object> getIsShowInGui = AbstractConfigEntry::getIsShowInGui;
        BiConsumer<AbstractConfigEntry, Boolean> setIsShowInGui = AbstractConfigEntry::setIsShowInGui;
        ATTRIBUTES_GET_SET_FUNC.put("isshow", new ImmutablePair(getIsShowInGui, setIsShowInGui));

        Function<AbstractConfigEntry, Object> getPath = AbstractConfigEntry::getPath;
        BiConsumer<AbstractConfigEntry, CategoryConfigEntry> setPath = AbstractConfigEntry::setPath;
        ATTRIBUTES_GET_SET_FUNC.put("isshow", new ImmutablePair(getIsShowInGui, setIsShowInGui));
    }

    public ConfigReader(FileReader readerIn) {
        reader = readerIn;
        lineIterator = IOUtils.lineIterator(reader);

    }

    public ConfigReader(File file) throws FileNotFoundException {
        this(new FileReader(file));
    }

    public static List<AbstractConfigEntry> assenbleConfigEntries(Collection<String> strings){
        List<AbstractConfigEntry> list = Lists.newArrayList();
        List<Pair<Collection<String>, Collection<String>>> rawEntryList = spiltEntry(strings);
        for (Pair<Collection<String>, Collection<String>> pair : rawEntryList) {
            list.add(assenbleEntry(pair.getLeft(), pair.getRight()));
        }
        return list;
    }

    public static AbstractConfigEntry assenbleEntry(Collection<String> comment, Collection<String> body) {
        Iterator<String> botyI = body.iterator();
        String s = botyI.next();



    }

    private static List<Pair<Collection<String>, Collection<String>>> spiltEntry(Collection<String> list) {
        List<Pair<Collection<String>, Collection<String>>> rawPairList = new ArrayList<>();
        ArrayList<String> commentBuffer = Lists.newArrayList();
        ArrayList<String> bodyBuffer = Lists.newArrayList();
        Stack<String> stack = new Stack<>();

        for (String s : list) {
            s = s.trim();
            if (CATEGORY_START_P.matcher(s).find()) {
                bodyBuffer.add(s);
                stack.push("{");
            } else if (CATEGORY_END_P.matcher(s).find()) {
                if (stack.size() == 0) {
                    throw new IllegalValueException(s);
                }
                if (CHAR_MAP_INVERSE.get(stack.pop()).equals(s)) {
                    bodyBuffer.add(s);
                    if (stack.size() == 0) {
                        rawPairList.add(new ImmutablePair<>((Collection<String>) commentBuffer.clone(), (Collection<String>) bodyBuffer.clone());
                        commentBuffer.clear();
                        bodyBuffer.clear();
                    }
                } else {
                    //符号验证失败
                    throw new IllegalValueException(s);
                }
            } else if (s.startsWith("#")) {
                commentBuffer.add(s.substring(1).trim());
            }
        }
        return rawPairList;

    }

    private static <V> Pair<String, V> assenbleToOne(String s, Class<? extends V> clazz) {
        String raw = s.trim();
        int index = raw.lastIndexOf(SEPARATOR);
        String left = raw.substring(0, index);
        String right = raw.substring(index + 1);
        return new ImmutablePair<>(left, stringToObj(right, clazz));
    }

    @SuppressWarnings("unchecked")
    private static <T> T stringToObj(String s, Class<? extends T> clazz) {
        try {
            return (T)FUNCTION_MAP.get(clazz).apply(s);
        }catch (Exception e) {
            if (e instanceof NullPointerException) {
                throw new IllegalValueException(String.format("The Type Of String:%s Are Not In [Boolean, Integer, Double, Float, String]", s));
            }else {
                throw new IllegalValueException(String.format("Can't Conversion String:%s to [%s]", s, clazz.getSimpleName()));
            }
        }
    }

    private static class IllegalValueException extends IllegalArgumentException {
        IllegalValueException(String s) {
            super(s);
        }
    }








}
