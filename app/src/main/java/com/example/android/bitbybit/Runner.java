package com.example.android.bitbybit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.widget.GridLayout;

/**
 * Created by franklin on 4/7/18.
 */

public class Runner {
    private int xPosition;
    private int yPosition;

    private int xAcceleration = 0;
    private int yAcceleration = 0;

    private int jumpCounterLimiter = 0;

    private int yVelocity = 0;
    private int xVelocity = 0;

    private Bitmap icon;
    private Paint paint;

    private int bottomY;

    Runner(int x, int y, Bitmap iconBitmap){
        xPosition = x;
        yPosition = y;

        bottomY = y;


        icon = Bitmap.createBitmap(iconBitmap);
        paint = new Paint();

        paint.setStrokeWidth(2f);
        paint.setColor(Color.BLACK);
    }

    public void setSize(int size){
        icon = Bitmap.createScaledBitmap(icon, size, size,false);
    }

    public void setxPosition(int xCoord){
        xPosition = xCoord;
    }

    public void setyPosition(int yCoord){
        yPosition = yCoord;
    }

    public void jump(){
        if(yPosition == bottomY) {
            jumpCounterLimiter = 0;
            yVelocity = -50;
            yAcceleration = 3;
        }
        else if(jumpCounterLimiter < 1) {
            yVelocity = -50;
            yAcceleration = 3;
            jumpCounterLimiter++;
            }
        }


    public void setPosition(int x, int y) {
        xPosition = x;
        yPosition = y;
    }

    public void setBottomY(int newBottomY){
        bottomY = newBottomY;

    }

    public void update(){
        yPosition+=yVelocity;
        xPosition+=xVelocity;
        yVelocity+=yAcceleration;
        xVelocity+=xAcceleration;
        if(yPosition > bottomY){
            yPosition = bottomY;
            yAcceleration = 0;
            yVelocity = 0;
        }

    }


    public void drawOnCanvas(Canvas canvas){
        canvas.drawBitmap(icon,xPosition,yPosition,paint);
    }


}
