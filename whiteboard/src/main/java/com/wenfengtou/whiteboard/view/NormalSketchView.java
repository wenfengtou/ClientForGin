package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.wenfengtou.whiteboard.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class NormalSketchView extends View implements View.OnTouchListener {
    private int mWidth;
    private int mHeight;
    //MyThread thread;
    private Path mPath = new Path();
    private Paint mPaint;
    private Surface mSurface;

    private volatile CopyOnWriteArrayList<Pair<Path, Paint>> mShowingList = new CopyOnWriteArrayList();

    public NormalSketchView(Context context) {
        this(context, null);
    }

    public NormalSketchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setSurface(Surface surface) {
        mSurface = surface;
    }

    public NormalSketchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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


    //撤销
    public void revoke() {
        Toast.makeText(getContext(), R.string.cancel_write, Toast.LENGTH_LONG).show();
        if (mShowingList.size() > 0) {
            Pair removePair = mShowingList.remove(mShowingList.size() -1);
           // thread.updataPathList(mShowingList);
            invalidate();
            //mResumeList.add(removePair);
            //updateSketch();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
        Bitmap bitmap = thread == null ? null : thread.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            canvas.drawBitmap(bitmap , 0 , 0 , null);
        }
         */
        drawCanvas(canvas);

        /*
        Canvas h264Canvas = mSurface.lockCanvas(null);
        drawCanvas(h264Canvas);
        mSurface.unlockCanvasAndPost(h264Canvas);

         */
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
        canvas.drawColor(Color.WHITE);
        //用drawPath进行绘制
        Iterator it = mShowingList.iterator();
        while (it != null && it.hasNext()) {
            Pair pair = (Pair) it.next();
            canvas.drawPath((Path) pair.first, (Paint) pair.second);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        if(mWidth != width || mHeight != height) {
            mWidth = width;
            mHeight = height;
          //  thread = new MyThread(mWidth , mHeight, mPaint);
          //  thread.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        /*
        if(thread != null) {
            thread.release();
        }

         */
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            //处理按下事件
            case MotionEvent.ACTION_DOWN:
                //mPath.moveTo(motionEvent.getX(), motionEvent.getY());
                mPath = new Path();
                //mResumeList.clear();
                mPath.moveTo(motionEvent.getX(), motionEvent.getY());
                mShowingList.add(new Pair(mPath, mPaint));
                //thread.pause(false);
                //按下的时候通过moveTo()绘制按下的这个点,获取按下点的X和Y坐标
                //mPath.moveTo(motionEvent.getX(), motionEvent.getY());
                /*
                mPath = new Path();
                mResumeList.clear();
                mPath.moveTo(motionEvent.getX(), motionEvent.getY());
                mShowingList.add(new Pair(mPath, mPaint));
                 */
                //获取之后调用draw()方法进行绘制
                //draw();
                break;

            //在移动的时候进行绘制
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(motionEvent.getX(),motionEvent.getY());
                //thread.updataPath(mPath);
                //thread.updataPathList(mShowingList);
                //updateSketch();
                invalidate();
                break;
            //在移动的时候进行绘制
            case MotionEvent.ACTION_UP:
                //thread.pause(true);
                break;
        }
        return true;
    }

    /*
    private class MyThread extends Thread {

        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Paint mPaint;
        private volatile Path mPath = null;
        private volatile boolean isRunning = true;
        private volatile boolean isPaused = false;
        private volatile boolean isHangup = true;
        private  ArrayList<Pair<Path, Paint>> mShowingList;
        public MyThread(int w, int h , Paint paint) {
            mBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
            mPaint = paint;
            isRunning = true;
        }

        public Bitmap getBitmap() {
            return mBitmap;
        }

        public void updataPathList(ArrayList<Pair<Path, Paint>> showingList) {
            mShowingList = showingList;
            isHangup = false;
        }

        public void updataPath(Path path) {
            mPath = path;
        }

        public void pause(boolean flag) {
            isPaused = flag;
        }

        public void release() {
            isRunning = false;
            try {
                this.join(100);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            super.run();
            while (isRunning) {
                if(isPaused || mCanvas == null) {
                    continue;
                }
                if(mShowingList == null || mShowingList.isEmpty()) {
                    mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    continue;
                }
                //mCanvas.drawPath(mPath , mPaint);
                Iterator it = mShowingList.iterator();
                while (it != null && it.hasNext()) {
                    Pair pair = (Pair) it.next();
                    mCanvas.drawPath((Path) pair.first, (Paint) pair.second);
                }
                postInvalidate();
            }
        }
    }

     */
}
