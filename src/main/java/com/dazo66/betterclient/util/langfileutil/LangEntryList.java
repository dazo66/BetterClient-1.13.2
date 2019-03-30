package com.dazo66.betterclient.util.langfileutil;

import org.apache.commons.io.IOUtils;
import org.apache.http.ParseException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Dazo66
 */
public class LangEntryList {

    private static final Pattern PATTERN0 = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Pattern PATTERN1 = Pattern.compile("=");
    private File file;
    private TreeMap<String, String> map;
    private TreeMap<String, String> oldMap;
    private List<IRule> rules = new ArrayList<>();

    public LangEntryList(File fileIn) {
        file = fileIn;
        map = serialization();

    }

    static Queue<Character> toCharacterQueue(String string) {
        Queue<Character> queue = new LinkedList<>();
        for (Character char1 : string.toCharArray()) {
            queue.offer(char1);
        }
        return queue;
    }


    @SuppressWarnings("unchecked")
    static String pollAWord(Queue<Character> queue) {
        Character character;
        LinkedList<Character> linkedList = (LinkedList) queue;
        StringBuilder builder = new StringBuilder();
        while (!linkedList.isEmpty()) {
            character = linkedList.poll();
            if (isAlphabet(character)) {
                builder.append(character);
            } else {
                linkedList.addFirst(character);
                break;
            }
        }
        return builder.toString();
    }

    static boolean isAlphabet(char character) {
        return character >= 'A' && character <= 'z';
    }

    static Character peekNotSpace(Queue<Character> queue) {
        char c;
        while (!queue.isEmpty()) {
            c = queue.peek();
            if (c == ' ') {
                queue.poll();
            } else {
                return c;
            }
        }
        return null;
    }

    static String pollTimes(Queue<Character> queue, int times) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < times; i++) {
            builder.append(queue.poll());
        }
        return builder.toString();
    }

    private TreeMap<String, String> serialization() {
        List<String> list = new ArrayList<>();
        TreeMap<String, String> map = new TreeMap<>();
        try {
            list = IOUtils.readLines(new FileInputStream(file), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] strings;
        for (String s : list) {
            if (null != s && !s.isEmpty()) {
                if (!s.startsWith("#")) {
                    strings = PATTERN1.split(s, 2);
                    if (strings.length == 2) {
                        if (strings[1].isEmpty()) {
                            strings[1] = strings[0];
                        }
                        map.put(strings[0], strings[1]);
                    }
                } else {
                    try {
                        addRule(s);
                    } catch (ParseException e) {
                        System.err.println(e.getMessage());
                    }

                }
            }
        }
        return map;
    }

    protected void addRule(String s) {
        for (IRule r : rules) {
            if (r.equals(s)) {
                return;
            }
        }
        rules.add(RuleFactory.getRule(s));

    }

    protected List<String> splitFile() {
        ArrayList<String> list = new ArrayList<>();
        if (file.isFile()) {
            try {
                LineNumberReader reader = new LineNumberReader(new FileReader(file));
                String s;
                while ((s = reader.readLine()) != null) {
                    list.add(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    boolean save() {
        TreeMap<String, String> reference = serialization();
        try {
            PrintWriter writer = new PrintWriter(file, "utf-8");
            String s;
            for (IRule rule : rules) {
                s = rule.toString();
                writer.println(s);
            }
            for (Map.Entry<String, String> entrySet : map.entrySet()) {
                String value = reference.get(entrySet.getKey());
                if (value == null || value.isEmpty()) {
                    value = entrySet.getKey();
                }
                if (!entrySet.getValue().equals(value)) {
                    entrySet.setValue(value);
                }
                s = entrySet.getKey() + "=" + entrySet.getValue();
                writer.println(s);
            }
            IOUtils.closeQuietly(writer);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    void put(String key, String value) {
        for (IRule rule : rules) {
            if (!rule.isAllow(key)) {
                return;
            }
        }
        map.put(key, value);
    }
}