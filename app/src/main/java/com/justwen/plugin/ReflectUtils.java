package com.justwen.plugin;

import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Justwen on 2018/11/18.
 */
public class ReflectUtils {

    @Nullable
    public static Object getStaticFieldObject(Class clz, String name) {
        try {
            Field field = clz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object getStaticFieldObject(String className, String name) {
        try {
            Class clz = Class.forName(className);
            Field field = clz.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(null);
        } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(Object obj, String methodName) {
        return invokeMethod(obj.getClass(), obj, methodName, null, null);
    }

    public static Object invokeMethod(String className, Object obj, String methodName) {
        try {
            return invokeMethod(Class.forName(className), obj, methodName, null, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(Class clz, Object obj, String methodName, Class[] paramTypes, Object[] paramValues) {
        try {
            Method method = clz.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return method.invoke(obj, paramValues);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static void setValue(String className, Object obj, String name, Object value) {
        try {
            Class clz = Class.forName(className);
            Field field = clz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
