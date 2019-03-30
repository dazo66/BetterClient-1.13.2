package com.dazo66.betterclient.util.langfileutil;

import org.apache.http.ParseException;

import java.util.LinkedList;

import static com.dazo66.betterclient.util.langfileutil.LangEntryList.*;

/**
 * @author Dazo66
 */
public class ContainsRule extends AbstractRule {

    private String rawRule;
    private boolean reverse = true;
    private String containString;

    public ContainsRule(String rule) {
        rawRule = rule;
        compile(rule);
    }

    @Override
    public String getKeyword() {
        return "contains";
    }

    @Override
    public boolean isReverse() {
        return reverse;
    }

    @Override
    public String getRawRule() {
        return rawRule;
    }

    @Override
    public void compile(String rule) {
        Character c;
        LinkedList<Character> queue = (LinkedList<Character>) toCharacterQueue(rule);
        if (rule.startsWith("#rule:")) {
            pollTimes(queue, 6);
            c = peekNotSpace(queue);
            if (c == '!') {
                queue.poll();
                c = peekNotSpace(queue);
            } else {
                reverse = false;
            }
            if (isAlphabet(c)) {
                String s = pollAWord(queue);
                if ("contains".equals(s) || "contain".equals(s)) {
                    c = peekNotSpace(queue);
                    if (c == '(') {
                        queue.poll();
                        c = peekNotSpace(queue);
                        if (c == '"') {
                            queue.poll();
                            int i = queue.lastIndexOf('"');
                            if (i != -1) {
                                containString = pollTimes(queue, i);
                                return;
                            } else {
                                throw new ParseException(rule);
                            }
                        } else if (c == '\'') {
                            queue.poll();
                            int i = queue.lastIndexOf('\'');
                            if (i != -1) {
                                containString = pollTimes(queue, i);
                                return;
                            }
                        }
                    }
                }
            }
        }
        throw new ParseException(rule);
    }

    @Override
    public boolean isAllow(String s) {
        if (reverse) {
            return !s.contains(containString);
        } else {
            return s.contains(containString);
        }
    }
}
