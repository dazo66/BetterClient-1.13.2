package com.dazo66.betterclient.util.langfileutil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

/**
 * @author Dazo66
 */
public class RuleFactory {

    private static HashMap<String, Class<? extends IRule>> ruleType = new HashMap<>();

    static {
        ruleType.put("contains", ContainsRule.class);
        ruleType.put("contain", ContainsRule.class);
    }

    @SuppressWarnings("unchecked")
    public static IRule getRule(String rawRule) {
        if (rawRule.startsWith("#rule")) {
            int index = rawRule.indexOf("(");
            if (index != -1) {
                String s = rawRule.substring(6, index);
                for (String keyword : ruleType.keySet()) {
                    if (s.contains(keyword)) {
                        try {
                            return ruleType.get(keyword).getConstructor(String.class).newInstance(rawRule);
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        throw new RuntimeException(rawRule);
    }


}
