package com.leebaba.android.blockedittextdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;

import com.leebaba.android.blockedittext.BlockEdittext;
import com.leebaba.android.blockedittext.OnInputListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private BlockEdittext edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edittext = (BlockEdittext) findViewById(R.id.block);
        edittext.setOnInputListener(new OnInputListener() {
            @Override
            public void OnInput(String text) {
                Log.i(TAG, "OnInput: " +text);
            }
        });
        edittext.setBlockMargin(20);
        edittext.setBlockSize(200);
        edittext.setTextSize(TypedValue.COMPLEX_UNIT_PX,100);
    }

}
