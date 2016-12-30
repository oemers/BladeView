package com.example.oemers.bladeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.util.ArrayList;

/**
 * Created by oemers on 2016/12/30.
 */
public class MyBladeView extends MySurfaceView{
    private Context mContext;
    private ArrayList<PointF> mTrack;
    private final static int POINT_LIMIT = 20;
    private Paint mPaint;
    private Canvas canvas;
    private float evX,evY;
    private int scroe=0;
    //此变量用于修改刀光的颜色
    private int mBladeColor = 0xFFFF0000;

    public MyBladeView(Context context) {
        super(context);
        init(context);
    }
    public MyBladeView(Context context, AttributeSet set) {
        super(context,set);
        init(context);
    }

    private void init(Context context){
        mContext = context;
        mPaint = new Paint();
        mTrack = new ArrayList<PointF>();

    }

    //专用于绘制屏幕
    @Override
    protected void myDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.myDraw(canvas);
        this.canvas = canvas;
        //用户画背景
        drawBackground(canvas);
        //画刀光
        drawBlade(canvas);
    }

    //请记得修改此画背景的方法，使背景更漂亮
    private void drawBackground(Canvas canvas){
        if (canvas == null){
            return;
        }
        //设置背景透明
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    //画刀光到画布上,有能力的小组可自行改进刀光
    private void drawBlade1(Canvas canvas){
        if (canvas == null){
            return;
        }
        mPaint.setColor(0xFFFFFFFF);
        synchronized(mTrack){
            Path path = new Path();
            Float startX, startY;
            Float controlX,controlY;
            Float endX, endY;

            int strokeWidth = 10;
            mPaint.setStyle(Paint.Style.STROKE);

            if(mTrack.size()>1){
                endX =  mTrack.get(0).x;
                endY =  mTrack.get(0).y;

                for(int i=0;i<mTrack.size()-1;i++){
                    startX = endX;
                    startY = endY;
                    controlX = mTrack.get(i).x;
                    controlY = mTrack.get(i).y;
                    endX = (controlX + mTrack.get(i+1).x)/2;
                    endY = (controlY + mTrack.get(i+1).y)/2;
                    path.moveTo(startX, startY);
                    path.quadTo(controlX, controlY, endX, endY);
                    mPaint.setColor(mBladeColor);
                    System.out.println("color:"+mBladeColor);
                    mPaint.setStrokeWidth(strokeWidth++);
                    canvas.drawPath(path, mPaint);

                    path.reset();

                }

                startX = endX;
                startY = endY;
                endX = mTrack.get(mTrack.size()-1).x;
                endY = mTrack.get(mTrack.size()-1).y;
                path.moveTo(startX, startY);
                path.lineTo(endX, endY);
                mPaint.setStrokeWidth(strokeWidth++);
                mPaint.setColor(mBladeColor);
                canvas.drawPath(path, mPaint);


                mTrack.remove(0);

            }
        }
    }

    //画刀光到画布上,有能力的小组可自行改进刀光
    private void drawBlade(Canvas canvas){
        if (canvas == null){
            return;
        }
        mPaint.setColor(0xFFFF0000);
        synchronized(mTrack){
            Path path = new Path();
            Float startX, startY;
            Float controlX,controlY;
            Float endX, endY;

            int strokeWidth = 1;
            mPaint.setStyle(Paint.Style.STROKE);

            if(mTrack.size()>1){
                endX =  mTrack.get(0).x;
                endY =  mTrack.get(0).y;

                for(int i=0;i < mTrack.size()-1;i++){

                    startX = endX;
                    startY = endY;

                    controlX = mTrack.get(i).x;
                    controlY = mTrack.get(i).y;
                    endX = (controlX + mTrack.get(i+1).x)/2;
                    endY = (controlY + mTrack.get(i+1).y)/2;
                    path.moveTo(startX, startY);
                    path.quadTo(controlX, controlY, endX, endY);
                    mPaint.setColor(mBladeColor);
                    if (i == mTrack.size()-2){
                        mPaint.setStrokeCap(Paint.Cap.ROUND);
                    }else {
                        mPaint.setStrokeCap(Paint.Cap.BUTT);
                    }
                    mPaint.setStrokeWidth(20);
                    mPaint.setAlpha(255/mTrack.size() * (i+1));
                    canvas.drawPath(path, mPaint);
                    path.reset();
                }

                startX = endX;
                startY = endY;
                endX = mTrack.get(mTrack.size()-1).x;
                endY = mTrack.get(mTrack.size()-1).y;
                path.moveTo(startX, startY);
                path.lineTo(endX, endY);
                mPaint.setStrokeWidth(20);
                mPaint.setColor(0x1000000*(strokeWidth++*20)+0xFF0000);
                canvas.drawPath(path, mPaint);
                if(mTrack.size()>=POINT_LIMIT){
                    evX = mTrack.get(mTrack.size()-1).x;
                    evY = mTrack.get(mTrack.size()-1).y;
                    mTrack.remove(0);
                }
            }else {
                mPaint.setColor(mBladeColor);
                mPaint.setStyle(Paint.Style.FILL);
                if (evX != 0 && evY != 0){
                    canvas.drawCircle(evX, evY,10,mPaint);
                }
            }
        }
    }


    //屏幕点击事件的响应方法
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        if(event.getAction()== MotionEvent.ACTION_DOWN){
            handleActionDown(event);
        }else if(event.getAction()== MotionEvent.ACTION_MOVE){
            handleActionMove(event);
        }else if(event.getAction()== MotionEvent.ACTION_UP){
            handleActionUp();
        }

        return true;
    }

    //手指按下的响应方法
    private void handleActionDown(MotionEvent event){
        PointF point = new PointF(event.getX(),event.getY());
        synchronized(mTrack){
            mTrack.add(point);
        }
    }

    //手指拖动的响应方法
    private void handleActionMove(MotionEvent event){
        PointF point = new PointF(event.getX(),event.getY());

        synchronized(mTrack){
            if(mTrack.size()>=POINT_LIMIT){
                mTrack.remove(0);
            }
            mTrack.add(point);
//            Log.d("summer",point.toString());
        }
    }

    //手指抬起的响应方法
    private void handleActionUp(){
        synchronized(mTrack){
            mTrack.clear();
        }
    }

    public final void addTrack(PointF point){
        synchronized(mTrack){
            if(mTrack.size()>=POINT_LIMIT){
                mTrack.remove(0);
            }
            mTrack.add(point);
        }
    }

}
