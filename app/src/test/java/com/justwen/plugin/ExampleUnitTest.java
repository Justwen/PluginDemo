package com.justwen.plugin;

import com.justwen.plugin.loader.PluginLoader;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        try {
            ClassLoader classLoader = PluginLoader.extractPlugin("plugin.apk");
            Object obj = classLoader.loadClass("justwen.plugin.PluginDemo");
            System.out.println(obj.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(4, 2 + 2);
    }
}