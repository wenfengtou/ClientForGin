package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
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
    public final static int PAINT_TOOL_PEN = 0;
    public final static int PAINT_TOOL_ERASER = 1;
    private Paint mErasePaint;
    private Paint mPenPaint;
    private int mPaintToolType = PAINT_TOOL_PEN;
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;
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
        //setBackgroundColor(Color.BLUE);
        mPenPaint = createDefaultPenPaint();
        mErasePaint = createDefaultErasePaint();
        mPaint = mPenPaint;
        setOnTouchListener(this);
    }

    private void initBuffer(){
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBufferBitmap.eraseColor(Color.BLUE);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    private Paint createDefaultPenPaint() {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        //初始化画笔的大小
        paint.setTextSize(40);
        //给画笔清理锯齿
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(50);
        return paint;
    }

    private Paint createDefaultErasePaint() {

        Paint paint = new Paint();
        //paint.setAlpha(0);
        //这个属性是设置paint为橡皮擦重中之重
        //这是重点
        //下面这句代码是橡皮擦设置的重点
        //paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        paint.setColor(Color.BLUE);
        //上面这句代码是橡皮擦设置的重点（重要的事是不是一定要说三遍）
        //paint.setAntiAlias(true);
        //paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
       // paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeWidth(30);

        return paint;
    }

    public void choosePaintTool(int paintToolType) {
        mPaintToolType = paintToolType;
        /*
        if (paintToolType == PAINT_TOOL_PEN) {
            mPaint = mPenPaint;
        } else if (paintToolType == PAINT_TOOL_ERASER) {
            mPaint = mErasePaint;
        }

         */
    }

    //撤销
    public void revoke() {
        Toast.makeText(getContext(), R.string.cancel_write, Toast.LENGTH_LONG).show();
        if (mShowingList.size() > 0) {
            Pair removePair = mShowingList.remove(mShowingList.size() -1);
           // thread.updataPathList(mShowingList);
            drawList();
            invalidate();
            //mResumeList.add(removePair);
            //updateSketch();
        }
    }

    private void drawList(){
        if (mShowingList != null) {
            //mBufferBitmap.eraseColor(Color.TRANSPARENT);
            mBufferBitmap.eraseColor(Color.BLUE);

//            mBufferCanvas.drawColor(Color.BLUE);
            Iterator it = mShowingList.iterator();
            while (it != null && it.hasNext()) {
                Pair pair = (Pair) it.next();
                mBufferCanvas.drawPath((Path) pair.first, (Paint) pair.second);
            }
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
        //drawCanvas(canvas);


        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        }

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
                int i = 0;
                while (true) {
                    i++;
                    Log.i("wenfengwenfeng","i=" + i);
                    Canvas h264Canvas = mSurface.lockCanvas(null);
                    //drawCanvas(h264Canvas);
                    /*
                    h264Canvas.drawColor(Color.WHITE);
                    Iterator it = mShowingList.iterator();
                    while (it != null && it.hasNext()) {
                        Pair pair = (Pair) it.next();
                        h264Canvas.drawPath((Path) pair.first, (Paint) pair.second);
                    }

                     */
                    h264Canvas.drawColor(Color.BLUE);
                    if (mBufferBitmap != null && !mBufferBitmap.isRecycled()) {
                        h264Canvas.drawBitmap(mBufferBitmap, 0, 0, null);
                    }
                    mSurface.unlockCanvasAndPost(h264Canvas);
                    /*
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                     */
                }
            }
        });
        thread.start();
    }

    private void drawCanvas(Canvas canvas) {
        //canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //canvas.drawColor(Color.WHITE);
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
                if (mPaintToolType == PAINT_TOOL_PEN) {
                    mPaint = createDefaultPenPaint();
                } else {
                    mPaint = createDefaultErasePaint();
                }

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
                if (mBufferBitmap == null) {
                    initBuffer();
                }
                mBufferCanvas.drawPath(mPath, mPaint);

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

}
