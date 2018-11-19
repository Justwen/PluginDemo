package com.justwen.plugin;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.justwen.plugin.loader.PluginLoader;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.justwen.plugin", appContext.getPackageName());
    }

    @Test
    public void testPlugin() {
        try {
            ClassLoader classLoader = PluginLoader.extractPlugin("plugin-debug.apk");
            Class clz = classLoader.loadClass("justwen.plugin.PluginDemo");
            Log.d("plugin", clz.newInstance().toString());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
    }

}
