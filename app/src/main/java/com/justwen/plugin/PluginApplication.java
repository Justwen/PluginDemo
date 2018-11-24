package com.justwen.plugin;

import android.app.Application;
import android.content.Context;

import com.justwen.plugin.hook.HookHelper;

/**
 * Created by Justwen on 2018/11/18.
 */
public class PluginApplication extends Application {

    public static Context sContext;

    @Override
    public void onCreate() {
        sContext = this;
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        HookHelper.startHook(base);
    }

}
