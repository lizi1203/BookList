package com.casper.testdrivendevelopment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.casper.testdrivendevelopment.R.drawable.smile_icon;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    SurfaceHolder holder;
    DrawThread drawThread;
    ArrayList<Sprite> sprites=new ArrayList<Sprite>();
    Bitmap mBitmap;
    Boolean click=false;
    float xTouch=-1,yTouch=-1;
    int count=0;
    public GameView(Context context) {
        super(context);
        holder=this.getHolder();
        holder.addCallback(this);

        sprites.add(new Sprite());
        sprites.add(new Sprite());
        sprites.add(new Sprite());
        sprites.add(new Sprite());

        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                xTouch=event.getX();
                yTouch=event.getY();
                click=true;
                return false;
            }

        });
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    drawThread=new DrawThread();
    drawThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    if(null!=drawThread)
    {
        drawThread.stopThread();
        drawThread=null;
    }
    }

    private class DrawThread extends Thread{
        private Boolean beAlive=true;


        public void stopThread() {
            beAlive=false;
        }

        public void run() {
            try{
                while(beAlive) {
                    //获得绘画的画布
                    Canvas canvas =  holder.lockCanvas();
                    canvas.drawColor(Color.WHITE);
                    Paint p = new Paint();
                    p.setColor(Color.BLACK);
                    p.setTextSize(50);
                    if(xTouch>=0) {
                        canvas.drawText("你击中了"+count+"次",20,40,p);
                    }else
                        canvas.drawText("hello world!", 20, 40, p);

                    for(Sprite sprite:sprites)
                    {
                        sprite.move();
                    }

                    for(Sprite sprite:sprites){
                        mBitmap =  BitmapFactory.decodeResource(getResources(), smile_icon);;
                        int width = mBitmap.getWidth();
                        int height = mBitmap.getHeight();
                        //设置想要的大小
                        int newWidth=120;
                        int newHeight=120;
                        //计算压缩的比率
                        float scaleWidth=((float)newWidth)/width;
                        float scaleHeight=((float)newHeight)/height;
                        //获取想要缩放的matrix
                        Matrix matrix = new Matrix();
                        matrix.postScale(scaleWidth,scaleHeight);
                        mBitmap=Bitmap.createBitmap(mBitmap,0,0,width,height,matrix,true);
                        canvas.drawBitmap(mBitmap,sprite.getX(),sprite.getY(),p);
                    }

                    //把绘画好的内容提交上去
                    holder.unlockCanvasAndPost(canvas);//解锁
                    Thread.sleep(20);
                }
            }catch (Exception e){

            }
        }
    }

    private class Sprite {
        int x;
        int y;
        double directionAgle;
        private Rect mChangeImageBackgroundRect = null;

        public Sprite() {
           x= (int) (GameView.this.getWidth()*Math.random());
           y= (int) (GameView.this.getHeight()*Math.random());
           directionAgle=Math.random()*2*Math.PI;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getDirectionAgle() {
            return directionAgle;
        }

        @SuppressLint("ResourceType")
        public void move()
        {
            x+=15*Math.cos(directionAgle);
            y+=15*Math.sin(directionAgle);

            if(x<0)
                x+=GameView.this.getWidth();
            if(x>GameView.this.getWidth())
                x-=GameView.this.getWidth();
            if(y<0)
                y+=GameView.this.getHeight();
            if(y>GameView.this.getHeight())
                y-=GameView.this.getHeight();

            if(Math.random()<0.05)
                directionAgle=Math.random()*2*Math.PI;

            if(click=true&&this.getX()<xTouch&&
                    this.getY()<yTouch&&
                    this.getX()+mBitmap.getWidth()>xTouch &&
                    this.getY()+mBitmap.getHeight()>yTouch){
                count++;
                xTouch=0;
                yTouch=0;
                click=false;
                x= (int) (GameView.this.getWidth()*Math.random());
                y= (int) (GameView.this.getHeight()*Math.random());
                directionAgle=Math.random()*2*Math.PI;
                this.move();
        }
            }
        }


    }




