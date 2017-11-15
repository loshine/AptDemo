package me.loshine.annotation;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by longshuai on 2017/11/15.
 */

public class Knife {

    private static Map<String, LayoutBinder> BINDER_MAP = new HashMap<>();

    public static void bind(Object obj) {
        String className = obj.getClass().getName();
        try {
            LayoutBinder layoutBinder = BINDER_MAP.get(className);
            if (layoutBinder == null) {
                Class<?> finderClass = Class.forName(className + "LayoutBinder");
                layoutBinder = (LayoutBinder) finderClass.newInstance();
                BINDER_MAP.put(className, layoutBinder);
            }
            layoutBinder.bindView(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
