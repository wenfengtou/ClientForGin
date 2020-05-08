package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.wenfengtou.whiteboard.R;

import java.util.ArrayList;
import java.util.Iterator;

public class SketchView extends SurfaceView implements View.OnTouchListener {

    private SurfaceHolder mSurfaceHolder;
    private Paint mPaint;
    private Path mPath;
    private Bitmap mBackGroupBitmap;
    private Surface mSurface;

    private ArrayList<Pair<Path, Paint>> mShowingList = new ArrayList();
    private ArrayList<Pair<Path, Paint>> mResumeList = new ArrayList();

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

    public void setSurface(Surface surface) {
        mSurface = surface;
    }

    public void init() {
        mSurfaceHolder = getHolder();
        mBackGroupBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.four)).getBitmap();
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        //初始化画笔的大小
        mPaint.setTextSize(40);
        //给画笔清理锯齿
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(50);
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public void initSketch() {
        //要绘制的话肯定要有一个画布,要通过getHolder()锁定画布,
        Canvas canvas = getHolder().lockCanvas();
        //初始化画布的颜色
        //canvas.drawColor(Color.WHITE);
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(mBackGroupBitmap, 0, 0, null);
        getHolder().unlockCanvasAndPost(canvas);
    }
    //创建绘制方法
    public void updateSketch(){
        //要绘制的话肯定要有一个画布,要通过getHolder()锁定画布,
        Canvas canvas = getHolder().lockCanvas();
        //初始化画布的颜色
        //canvas.drawColor(Color.WHITE);
        drawCanvas(canvas);
        //绘制结束后要解锁画布
        getHolder().unlockCanvasAndPost(canvas);
    }

    public void startDecoreThread() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //流绘制
                while (true) {
                    Canvas h264Canvas = mSurface.lockCanvas(null);
                    drawCanvas(h264Canvas);
                    mSurface.unlockCanvasAndPost(h264Canvas);
                }
            }
        });
        thread.start();
    }

    private void drawCanvas(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        canvas.drawBitmap(mBackGroupBitmap, 0, 0, null);
        //用drawPath进行绘制
        Iterator it = mShowingList.iterator();
        while (it != null && it.hasNext()) {
            Pair pair = (Pair) it.next();
            canvas.drawPath((Path) pair.first, (Paint) pair.second);
        }
    }
    //撤销
    public void revoke() {
        Toast.makeText(getContext(), R.string.cancel_write, Toast.LENGTH_LONG).show();
        if (mShowingList.size() > 0) {
            Pair removePair = mShowingList.remove(mShowingList.size() -1);
            mResumeList.add(removePair);
            updateSketch();
        }
    }

    //恢复
    public void resume() {
        if (mResumeList.size() > 0) {
            Pair resumePair = mResumeList.remove(mResumeList.size() -1);
            mShowingList.add(resumePair);
            updateSketch();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            //处理按下事件
            case MotionEvent.ACTION_DOWN:
                //按下的时候通过moveTo()绘制按下的这个点,获取按下点的X和Y坐标
                //mPath.moveTo(motionEvent.getX(), motionEvent.getY());
                mPath = new Path();
                mResumeList.clear();
                mPath.moveTo(motionEvent.getX(), motionEvent.getY());
                mShowingList.add(new Pair(mPath, mPaint));
                //获取之后调用draw()方法进行绘制
                //draw();
                break;

            //在移动的时候进行绘制
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(motionEvent.getX(),motionEvent.getY());
                updateSketch();
                break;
            //在移动的时候进行绘制
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }
}
