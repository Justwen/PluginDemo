package com.justwen.plugin;

import android.app.ActivityManager;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Justwen on 2018/11/18.
 */
public class AMSHookHelper {

    public static void hook() {
        hookAMS();
    }

    public static void hookAMS() {
        final Object obj = ReflectUtils.getStaticFieldObject(ActivityManager.class, "IActivityManagerSingleton");
        final Object service = ReflectUtils.invokeMethod("android.util.Singleton", obj, "get");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getClass().getClassLoader(), service.getClass().getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.d("mahang", method.getName());
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
                    newIntent.setClass(PluginApplication.sContext, StubActivity.class);
                  //  args[i] = newIntent;
                }
                return method.invoke(service, args);
            }
        });
        ReflectUtils.setValue("android.util.Singleton", obj, "mInstance", proxy);
    }

    public static void hookHandler() {

    }

}
