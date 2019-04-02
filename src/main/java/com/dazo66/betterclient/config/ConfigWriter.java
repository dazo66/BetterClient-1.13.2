package com.dazo66.betterclient.config;

import com.dazo66.betterclient.config.configentrys.*;
import org.apache.commons.io.IOUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author Dazo66
 */
public class ConfigWriter {

    private final FileWriter writer;
    private Stack<String> stack = new Stack<>();

    private static final short INDENTATION_COUNT = 4;
    private static final Map<Class<? extends AbstractConfigEntry>, String> TYPE_MAP = new HashMap<>();
    private static final Map<String, String> CHAR_MAP = new HashMap<>();
    private static final String SPACE = " ";

    static {
        TYPE_MAP.put(BooleanConfigEntry.class, "B");
        TYPE_MAP.put(CategoryConfigEntry.class, "C");
        TYPE_MAP.put(DoubleConfigEntry.class, "D");
        TYPE_MAP.put(IntConfigEntry.class, "I");
        TYPE_MAP.put(StringConfigEntry.class, "S");

        CHAR_MAP.put("{", "}");
        CHAR_MAP.put("[", "]");
        CHAR_MAP.put("<", ">");
        CHAR_MAP.put("(", ")");
    }

    public ConfigWriter(FileWriter writerIn) {
        writer = writerIn;
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
        return append(getIndentation()).append(string).append("\n");
    }

    private Appendable appendLine() throws IOException {
        return append(getIndentation()).append("\n");
    }

    private Appendable append(String string) throws IOException {
        return writer.append(string);
    }

    public Appendable appendChar(CharSequence charSequence) throws IOException {
        return writer.append(charSequence);
    }

    public boolean save(List<AbstractConfigEntry> entryList) throws IOException {
        for (AbstractConfigEntry entry : entryList) {
            String type = TYPE_MAP.get(entry.getClass());
            if ("C".equals(type)) {
                appendLine(String.format("C:%s {", entry.getKey()));
                push("{");
                save(((CategoryConfigEntry)entry).getValue());
                pop();
                appendLine();
            } else if (type != null) {
                appendComment(AbstractConfigEntry.commentToSave(entry));
                appendLine(String.format("%s:%s=%s", type, entry.getKey(),entry.getValue()));
                appendLine();
            }
        }
        return true;
    }

}
