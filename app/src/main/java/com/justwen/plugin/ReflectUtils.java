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
    public static Object getValue(String className, Object obj, String fieldName) {
        try {
            Class clz = Class.forName(className);
            getValue(clz, obj, fieldName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public static Object getValue(Class clz, Object obj, String fieldName) {
        try {
            Field field = clz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static Object invokeStaticMethod(Class clz, String methodName) {
        return invokeMethod(clz, null, methodName, null, null);
    }

    public static Object invokeStaticMethod(String className, String methodName) {
        try {
            return invokeStaticMethod(Class.forName(className), methodName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(String className, Object obj, String methodName) {
        try {
            return invokeMethod(Class.forName(className), obj, methodName, null, null);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object invokeMethod(Class clz, Object obj, String methodName) {
        return invokeMethod(clz, obj, methodName, null, null);
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
            setValue(clz, obj, name, value);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void setValue(Class clz, Object obj, String name, Object value) {
        try {
            Field field = clz.getDeclaredField(name);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
}
