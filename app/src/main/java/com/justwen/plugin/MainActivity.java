package com.justwen.plugin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(android.R.layout.simple_list_item_2);

        TextView tv = findViewById(android.R.id.text1);
        tv.setText("启动插件Activity");
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AMSHookHelper.hookClassLoader(MainActivity.this);
                Intent intent = new Intent();
                intent.setClassName("justwen.plugin", "justwen.plugin.PluginActivity");
                startActivity(intent);
            }
        });
    }

}
