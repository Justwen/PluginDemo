package com.justwen.plugin.loader;

import android.content.Context;
import android.os.Build;

import com.justwen.plugin.PluginApplication;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import dalvik.system.DexClassLoader;

/**
 * Created by Justwen on 2018/11/19.
 */
public class PluginLoader {

    public static ClassLoader extractPlugin(String plugin) {
        return extractPlugin(plugin, PluginApplication.sContext);
    }

    public static ClassLoader extractPlugin(String plugin, Context context) {
        File extractFile = null;
        try {
            extractFile = context.getFileStreamPath(plugin);
            try (InputStream is = context.getAssets().open(plugin);
                 FileOutputStream fos = new FileOutputStream(extractFile)) {
                byte[] buffer = new byte[1024];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String dexPath = extractFile.getPath();
        String libPath = unzipLibraryFile(dexPath, extractFile.getParent());
        File fileRelease = context.getDir("dex", Context.MODE_PRIVATE);
        return new DexClassLoader(dexPath, fileRelease.getAbsolutePath(), libPath, context.getClassLoader());
    }


    private static String unzipLibraryFile(String zipFile, String targetDir) {
        StringBuilder builder = new StringBuilder();
        String cpu = '/' + Build.SUPPORTED_ABIS[0] + '/';

        int buffer = 4096; // 这里缓冲区我们使用4KB，
        String strEntry; // 保存每个zip的条目名称
        try (FileInputStream fis = new FileInputStream(zipFile);
             ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis))) {
            ZipEntry entry; // 每个zip条目的实例
            while ((entry = zis.getNextEntry()) != null) {
                int count;
                byte[] data = new byte[buffer];
                strEntry = entry.getName();

                if (!strEntry.endsWith(".so") || !strEntry.contains(cpu)) {
                    continue;
                }

                File entryFile = new File(targetDir + strEntry);
                File entryDir = new File(entryFile.getParent());

                if (!entryDir.exists()) {
                    entryDir.mkdirs();
                }

                try (FileOutputStream fos = new FileOutputStream(entryFile);
                     BufferedOutputStream dest = new BufferedOutputStream(fos, buffer)) {
                    while ((count = zis.read(data, 0, buffer)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();

                    builder.append(entryDir.getAbsolutePath());
                    builder.append(",");
                }

            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            return builder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
