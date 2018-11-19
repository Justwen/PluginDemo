package com.justwen.plugin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Justwen on 2018/11/18.
 */
public class StubActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(this,"插件启动失败",Toast.LENGTH_SHORT).show();
    }
}
