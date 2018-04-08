package com.example.android.bitbybit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    GridLayout grid;
    int columnCount;
    int rowCount;
    RunnerView mRunView;
    RelativeLayout rootView;
    int GCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View topLevelView = getWindow().getDecorView();
        topLevelView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        Display deviceDisplay = getWindowManager().getDefaultDisplay();
        DisplayMetrics deviceDisplayMetrics = new DisplayMetrics();
        deviceDisplay.getRealMetrics(deviceDisplayMetrics);
        BigInteger screenW = new BigInteger(deviceDisplayMetrics.widthPixels+"");
        BigInteger screenH = new BigInteger(deviceDisplayMetrics.heightPixels+"");
        GCD = screenH.gcd(screenW).intValue();
        columnCount = screenW.intValue()/GCD;

        rowCount = screenH.intValue()/GCD;

        rootView = findViewById(R.id.root);

        mRunView = findViewById(R.id.runVIew);
        mRunView.loadBitmaps();

        class playThread extends Thread{
            public void run() {
                while(true){
                    mRunView.update();
                    try{
                        Thread.sleep(17);

                    } catch (Exception e){

                    }
                }
            }
        }

        playThread master = new playThread();
        master.start();
    }
    public int getIconSize(){
        return GCD;
    }
    public int[] getRowsAndColumns(){
        int[] toReturn = {rowCount,columnCount};

        return toReturn;

    }



}
