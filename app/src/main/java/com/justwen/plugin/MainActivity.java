package com.justwen.plugin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.simple_list_item_2);

        TextView tv = findViewById(android.R.id.text1);
        tv.setText("启动插件Activity");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("justwen.plugin", "justwen.plugin.PluginActivity");
                startActivity(intent);
            }
        });
    }

}
