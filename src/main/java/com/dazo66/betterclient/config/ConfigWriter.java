package com.dazo66.betterclient.config;

import com.dazo66.betterclient.config.configentrys.*;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author Dazo66
 */
public class ConfigWriter {

    private final PrintStream stream;
    private Stack<String> stack = new Stack<>();
    private boolean isCommentWriting = false;

    private static final short INDENTATION_COUNT = 4;
    public static final BiMap<Class<? extends AbstractConfigEntry>, String> TYPE_MAP = HashBiMap.create();
    static final BiMap<String, String> CHAR_MAP = HashBiMap.create();
    private static final String SPACE = " ";
    public static Map<String, Pair<Function, BiConsumer>> ATTRIBUTES_GET_SET_FUNC = ConfigReader.ATTRIBUTES_GET_SET_FUNC;

    static {
        TYPE_MAP.put(BooleanConfigEntry.class, "B");
        TYPE_MAP.put(CategoryConfigEntry.class, "C");
        TYPE_MAP.put(DoubleConfigEntry.class, "D");
        TYPE_MAP.put(IntConfigEntry.class, "I");
        TYPE_MAP.put(StringConfigEntry.class, "S");

        TYPE_MAP.put(BooleanArrayConfigEntry.class, "BA");
        TYPE_MAP.put(DoubleArrayConfigEntry.class, "DA");
        TYPE_MAP.put(IntArrayConfigEntry.class, "IA");
        TYPE_MAP.put(StringArrayConfigEntry.class, "SA");

        CHAR_MAP.put("{", "}");
        CHAR_MAP.put("[", "]");
        CHAR_MAP.put("<", ">");
        CHAR_MAP.put("(", ")");
    }

    public ConfigWriter(PrintStream printStream) {
        this.stream = printStream;
    }

    public ConfigWriter(File file) throws FileNotFoundException {
        this(new PrintStream(file));
    }

    public ConfigWriter(String fileName) throws FileNotFoundException {
        this(new File(fileName));
    }

    private void push(String s) {
        stack.push(s);
    }

    private void pop() throws IOException {
        appendLine(CHAR_MAP.get(stack.pop()));
    }

    private String getIndentation() {
        StringBuilder s = new StringBuilder();
        int size = stack.size() * INDENTATION_COUNT;
        for (int i = 0; i < size; i++) {
            s.append(SPACE);
        }
        return s.toString();
    }

    private Appendable appendComment(String[] commentIn) throws IOException {
        Appendable a = append("");
        for (String s : commentIn) {
            a = appendLine("#" + s);
        }
        return a;
    }

    private Appendable appendLine(String[] strings) throws IOException {
        Appendable a = append("");
        for (String s : strings) {
            a = appendLine(s);
        }
        return a;
    }

    private Appendable appendLine(String string) throws IOException {
        if (isCommentWriting) {
            string = "#" + string;
        }
        return append(getIndentation()).append(string).append("\n");
    }

    private Appendable appendLine() throws IOException {
        return appendLine("");
    }

    private Appendable append(String string) throws IOException {
        return stream.append(string);
    }

    public Appendable appendChar(CharSequence charSequence) throws IOException {
        return stream.append(charSequence);
    }

    public boolean save(List<AbstractConfigEntry> entryList) throws IOException {
        for (AbstractConfigEntry entry : entryList) {
            String type = TYPE_MAP.get(entry.getClass());
            if ("C".equals(type)) {
                appendLine(String.format("C:%s {", entry.getKey()));
                push("{");
                save(((CategoryConfigEntry) entry).getValue());
                pop();
            } else if (type.endsWith("A")) {
                appendLine(String.format("%s:%s:[", type, entry.getKey()));
                push("[");
                appendLine(genArray((Object[]) entry.getValue()));
                pop();
            } else if (type != null) {
                commentToSave(entry);
                appendLine(String.format("%s:%s=%s", type, entry.getKey(), entry.getValue()));
            }
            appendLine();
        }
        stream.flush();
        return true;
    }

    private static String[] genArray(Object[] objects) {
        ArrayList<String> list = new ArrayList<>();
        for (Object o : objects) {
            list.add(o.toString());
        }
        return list.toArray(new String[0]);
    }

    public void commentToSave(AbstractConfigEntry configEntry) {
        startCommentWriter();
        for (Map.Entry<String, Pair<Function, BiConsumer>> entry : ConfigReader.ATTRIBUTES_GET_SET_FUNC.entrySet()) {
            try {
                Object vaule = entry.getValue().getLeft().apply(configEntry);
                if (vaule == null) {
                    continue;
                }
                if (!vaule.getClass().isArray()) {
                    appendLine(String.format("%s=%s" , configEntry.getKey(), vaule));
                } else {
                    //todo array写入
                    appendLine(String.format("%s:[", entry.getKey()));
                    push("[");
                    endCommentWriter();
                    appendLine(genArray((Object[]) vaule));
                    pop();
                    startCommentWriter();
                }
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        endCommentWriter();
    }

    private void startCommentWriter(){
        isCommentWriting = true;
    }

    private void endCommentWriter(){
        isCommentWriting = false;
    }

}
