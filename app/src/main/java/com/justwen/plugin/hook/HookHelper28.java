package com.justwen.plugin.hook;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.justwen.plugin.PluginApplication;
import com.justwen.plugin.ReflectUtils;
import com.justwen.plugin.StubActivity;
import com.justwen.plugin.loader.PluginLoader;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

import dalvik.system.BaseDexClassLoader;

/**
 * Created by Justwen on 2018/11/24.
 */
public class HookHelper28 {

    public static void startHook(Context context) {
        hookAMS();
        hookHandler();
        hookClassLoader(context);
    }

    private static void hookAMS() {
        final Object obj = ReflectUtils.getStaticFieldObject(ActivityManager.class, "IActivityManagerSingleton");
        final Object service = ReflectUtils.invokeMethod("android.util.Singleton", obj, "get");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getClass().getClassLoader(), service.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 代理startActivity方法
                if (method.getName().equals("startActivity")) {
                    int i;
                    for (i = 0; i < args.length; i++) {
                        if (args[i] instanceof Intent) {
                            break;
                        }
                    }
                    Intent intent = (Intent) args[i];
                    Intent newIntent = new Intent(intent);
                    newIntent.putExtra("new_intent", intent);
                    //将插件Activity替换为StubActivity,并缓存起来
                    newIntent.setClass(PluginApplication.sContext, StubActivity.class);
                    args[i] = newIntent;
                }
                return method.invoke(service, args);
            }
        });
        ReflectUtils.setValue("android.util.Singleton", obj, "mInstance", proxy);
    }

    public static void hookHandler() {

        Object obj = ReflectUtils.invokeStaticMethod("android.app.ActivityThread", "currentActivityThread");
        final Handler handler = (Handler) ReflectUtils.invokeMethod("android.app.ActivityThread", obj, "getHandler");

        ReflectUtils.setValue(Handler.class, handler, "mCallback", new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 159) {
                    Object obj = msg.obj;
                    List<Object> mActivityCallbacks = (List<Object>) ReflectUtils.getValue(obj.getClass(), obj, "mActivityCallbacks");
                    if (!mActivityCallbacks.isEmpty()) {
                        String className = "android.app.servertransaction.LaunchActivityItem";
                        if (mActivityCallbacks.get(0).getClass().getCanonicalName().equals(className)) {
                            Object object = mActivityCallbacks.get(0);
                            Intent intent = (Intent) ReflectUtils.getValue(object.getClass(), object, "mIntent");
                            if (intent != null) {
                                Intent target = intent.getParcelableExtra("new_intent");
                                if (target != null) {
                                    intent.setComponent(target.getComponent());
                                }
                            }

                        }
                    }

                }
                handler.handleMessage(msg);
                return true;
            }
        });


    }

    public static void hookClassLoader(Context context) {
        BaseDexClassLoader cl = (BaseDexClassLoader) context.getClassLoader();

        Object dexPathList = ReflectUtils.getValue(cl.getClass().getSuperclass(), cl, "pathList");
        Object elements = ReflectUtils.getValue(dexPathList.getClass(), dexPathList, "dexElements");

        ClassLoader pluginClassLoader = PluginLoader.extractPlugin("plugin-debug.apk", context);
        Object pluginDexPathList = ReflectUtils.getValue(pluginClassLoader.getClass().getSuperclass(), pluginClassLoader, "pathList");
        Object pluginElements = ReflectUtils.getValue(pluginDexPathList.getClass(), pluginDexPathList, "dexElements");

        //合并elements
        Object newElements = Array.newInstance(Array.get(elements, 0).getClass(), Array.getLength(elements) + Array.getLength(pluginElements));

        int i;
        for (i = 0; i < Array.getLength(pluginElements); i++) {
            Array.set(newElements, i, Array.get(pluginElements, i));
        }

        for (int j = 0; j < Array.getLength(elements); j++) {
            Array.set(newElements, i, Array.get(elements, j));
            i++;
        }

        ReflectUtils.setValue(dexPathList.getClass(), dexPathList, "dexElements", newElements);
    }
}
