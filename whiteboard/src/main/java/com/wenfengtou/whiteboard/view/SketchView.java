package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class SketchView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Path mPath;

    public SketchView(Context context) {
        super(context);
    }

    public SketchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SketchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        //初始化画笔的大小
        mPaint.setTextSize(40);
        //给画笔清理锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        setOnTouchListener(this);
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    //创建绘制方法
    public void draw(){
        //要绘制的话肯定要有一个画布,要通过getHolder()锁定画布,
        Canvas canvas = getHolder().lockCanvas();
        //初始化画布的颜色
        canvas.drawColor(Color.WHITE);
        //用drawPath进行绘制
        canvas.drawPath(mPath, mPaint);
        //绘制结束后要解锁画布
        getHolder().unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            //处理按下事件
            case MotionEvent.ACTION_DOWN:
                //按下的时候通过moveTo()绘制按下的这个点,获取按下点的X和Y坐标
                mPath.moveTo(motionEvent.getX(), motionEvent.getY());
                //获取之后调用draw()方法进行绘制
                draw();
                break;

            //在移动的时候进行绘制
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(motionEvent.getX(),motionEvent.getY());
                draw();
                break;
        }
        return true;
    }
}
