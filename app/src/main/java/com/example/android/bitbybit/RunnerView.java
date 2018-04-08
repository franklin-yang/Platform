package com.example.android.bitbybit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * TODO: document your custom view class.
 */
public class RunnerView extends View {
    private Runner theRunner;
    private Bitmap runner;
    Vector<Bitmap[]> icons;
    private int[] rowsAndColumns;
    Random picker;
    Bitmap[] CellImages;
    private int iconSize;
    Paint iconPaint = new Paint();
    private int backgroundUpdateSkipper = 0;
    private boolean advance;
    Bitmap torch;
    Bitmap bonfire;
    Vector<Obstacle> obstacles;
    private boolean end = false;


    public RunnerView(Context context) {
        super(context);
        init(null, 0);
    }

    public RunnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }


    private void init(AttributeSet attrs, int defStyle) {


    }



    public void loadBitmaps(){
        super.onFinishInflate();
        torch = BitmapFactory.decodeResource(getResources(),R.drawable.torch);
        bonfire = BitmapFactory.decodeResource(getResources(),R.drawable.fire);
        runner = BitmapFactory.decodeResource(getResources(),R.drawable.runner);
        theRunner = new Runner((getWidth()/10),(4*getHeight()/5), runner);
        setWillNotDraw(false);
        Bitmap backgroundCellSrc = BitmapFactory.decodeResource(getResources(),R.drawable.grid);
        picker = new Random();
        iconSize = ((MainActivity)getContext()).getIconSize();

        CellImages = new Bitmap[15];
        CellImages[0] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,0,0,45,45),iconSize,iconSize,false);
        CellImages[1] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,312,0,45,45),iconSize,iconSize,false);
        CellImages[2] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,401,0,45,45),iconSize,iconSize,false);
        CellImages[3] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,223,45,45,45),iconSize,iconSize,false);
        CellImages[4] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,268,45,45,45),iconSize,iconSize,false);
        CellImages[5] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,312,45,45,45),iconSize,iconSize,false);
        CellImages[6] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,357,45,45,45),iconSize,iconSize,false);
        CellImages[7] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,45,89,45,45),iconSize,iconSize,false);
        CellImages[8] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,89,89,45,45),iconSize,iconSize,false);
        CellImages[9] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,138,89,45,45),iconSize,iconSize,false);
        CellImages[10] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,223,89,45,45),iconSize,iconSize,false);
        CellImages[11] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,401,89,45,45),iconSize,iconSize,false);
        CellImages[12] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,45,134,45,45),iconSize,iconSize,false);
        CellImages[13] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,134,89,45,45),iconSize,iconSize,false);
        CellImages[14] = Bitmap.createScaledBitmap(Bitmap.createBitmap(backgroundCellSrc,401,178,45,45),iconSize,iconSize,false);
        rowsAndColumns = ((MainActivity)(getContext())).getRowsAndColumns();
        icons = new Vector();
        Log.d("df",Arrays.toString(rowsAndColumns)+"");

        for(int i = 0; i < rowsAndColumns[1]; i++){
            Bitmap[] eachColumn = new Bitmap[rowsAndColumns[0]];

            for(int j = 0; j < eachColumn.length; j++){
                eachColumn[j] = CellImages[picker.nextInt(15)];
            }
            icons.add(eachColumn);
        }
        obstacles = new Vector<>();

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        theRunner.setSize(getHeight()/10);
        theRunner.setPosition(getWidth()/10,4*getWidth()/5);
        theRunner.setBottomY(4*getHeight()/5);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                theRunner.jump();
            }
        });
    }

    public void update(){
        for(Obstacle o:obstacles){
            if(theRunner.collisionCheck(o)){
                end = true;
                Intent endGame = new Intent(getContext(), GameOver.class);
                getContext().startActivity(endGame);
            }
        }
        if(picker.nextInt(200)==2){
            spawnObstacle();
        }

        if(backgroundUpdateSkipper != 0 && backgroundUpdateSkipper % 20 == 0) {
            advanceBackground();
            backgroundUpdateSkipper = 0;
        }
        else
            backgroundUpdateSkipper++;
        theRunner.update();
        for(Obstacle o:obstacles){
            o.update();
        }
        postInvalidate();
    }

    private void spawnObstacle() {
        int pickObstacle = picker.nextInt(2);
        Log.d("dfd",pickObstacle+"Dfd");
        Obstacle toAdd = null;
        deleteGoneObstacles();
        int size = picker.nextInt(((MainActivity)getContext()).getDimensions()[1]/4);
        int velocity = 3+picker.nextInt(15);
        switch (pickObstacle){
            case 0: toAdd = new Torch(((MainActivity)getContext()).getDimensions()[0],theRunner.getBottomY()-(size-getHeight()/10),torch);
                    break;
            case 1: toAdd = new Bonfire(((MainActivity)getContext()).getDimensions()[0],theRunner.getBottomY()-(size-getHeight()/10),bonfire);
                    break;
        }
        toAdd.setSize(size);
        toAdd.setxVelocity(-velocity);
        obstacles.add(toAdd);
    }

    private void deleteGoneObstacles(){
        if(obstacles.size() > 0 && obstacles.get(0).getxPosition() < 0){
            obstacles.remove(0);
            deleteGoneObstacles();
        }
        Log.d("DFd",obstacles.size()+"");
    }

    private void advanceBackground(){
        icons.remove(0);
        Bitmap[] newColumn = new Bitmap[rowsAndColumns[0]];
        for(int j = 0; j < newColumn.length; j++){
            newColumn[j] = CellImages[picker.nextInt(15)];
        }
        icons.add(newColumn);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i = 0; i < icons.size(); i++){
            for(int j = 0; j < icons.get(i).length; j++){
                Log.d("index",j+"");
                canvas.drawBitmap(icons.get(i)[j],i*iconSize,j*iconSize, iconPaint);
            }
        }
        for(Obstacle o: obstacles){
            o.drawOnCanvas(canvas);
        }
        theRunner.drawOnCanvas(canvas);


    }
}
