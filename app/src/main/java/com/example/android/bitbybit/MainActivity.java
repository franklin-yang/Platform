package com.example.android.bitbybit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View topLevelView = getWindow().getDecorView();
        topLevelView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


}
