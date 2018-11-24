package com.justwen.plugin.hook;

import android.content.Context;
import android.os.Build;

/**
 * Created by Justwen on 2018/11/24.
 */
public class HookHelper {

    public static void startHook(Context context) {

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O) {

        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            HookHelper27.startHook(context);
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {
            HookHelper28.startHook(context);
        }

    }
}
