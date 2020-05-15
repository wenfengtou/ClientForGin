package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.wenfengtou.whiteboard.R;
import com.wenfengtou.whiteboard.painttool.Eraser;
import com.wenfengtou.whiteboard.painttool.PaintTool;
import com.wenfengtou.whiteboard.painttool.Pen;
import com.wenfengtou.whiteboard.setting.EraserSetting;
import com.wenfengtou.whiteboard.setting.PenSetting;
import com.wenfengtou.whiteboard.shape.CurveShape;
import com.wenfengtou.whiteboard.shape.Shape;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class NormalSketchView extends View implements View.OnTouchListener {
    private int mWidth;
    private int mHeight;
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

    private Bitmap mH264Bitmap;
    private Bitmap mBackgroupBitmap;
    private volatile CopyOnWriteArrayList<DrawInfoItem> mShowingList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<DrawInfoItem> mResumeList = new CopyOnWriteArrayList();

    private PaintTool mPaintTool;
    private int mShapeType = Shape.SHAPE_TYPE_CURVE;
    private Shape mShape;

    private boolean mIsReDrawShowList = false;

    class DrawInfoItem {
        public PaintTool mPaintTool;
        public Shape mShape;
        public DrawInfoItem(PaintTool paintTool, Shape shape) {
            mPaintTool = paintTool;
            mShape = shape;
        }
    }

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
        //mBackgroupBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.back3)).getBitmap();
        mBackgroupBitmap = drawableToBitmap(getResources().getDrawable(R.drawable.back3));
        setOnTouchListener(this);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        //取drawable的宽高
        int width = 1080;
        int height = 1920;
        //取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE
                ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        //创建对应的bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        //创建对应的bitmap的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        //把drawable内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    private void initBuffer(){
        mBufferBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        //mBufferBitmap.eraseColor(Color.BLUE);
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
    }

    //撤销
    public void undo() {
        Toast.makeText(getContext(), R.string.cancel_write, Toast.LENGTH_LONG).show();
        if (mShowingList.size() > 0) {
            DrawInfoItem removeDrawInfoItem = mShowingList.remove(mShowingList.size() -1);
            mResumeList.add(removeDrawInfoItem);
            redrawShowList();
            invalidate();
        }
    }

    //重做
    public void redo() {
        Toast.makeText(getContext(), R.string.resume_write, Toast.LENGTH_LONG).show();
        if (mResumeList.size() > 0) {
            DrawInfoItem resumeDrawInfoItem = mResumeList.remove(mResumeList.size() -1);
            mShowingList.add(resumeDrawInfoItem);
            redrawShowList();
            invalidate();
        }
    }

    private void redrawShowList() {
        if (mShowingList != null) {
            mBufferBitmap.eraseColor(Color.TRANSPARENT);
            Iterator it = mShowingList.iterator();
            while (it != null && it.hasNext()) {
                DrawInfoItem drawInfoItem = (DrawInfoItem) it.next();
                PaintTool paintTool = drawInfoItem.mPaintTool;
                Shape shape = drawInfoItem.mShape;
                paintTool.drawShape(mBufferCanvas, shape);
            }
        }
        mH264Bitmap = mBufferBitmap.copy(Bitmap.Config.ARGB_8888, false);
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
            canvas.drawBitmap(mBackgroupBitmap, 0 ,0, null);
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
                    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    paint.setColor(Color.WHITE);

                    //drawCanvas(h264Canvas);
                    /*
                    h264Canvas.drawColor(Color.WHITE);
                    Iterator it = mShowingList.iterator();
                    while (it != null && it.hasNext()) {
                        Pair pair = (Pair) it.next();
                        h264Canvas.drawPath((Path) pair.first, (Paint) pair.second);
                    }
                    */




                    //for循环方式绘制，橡皮擦会黑底
                    /*
                    Canvas h264Canvas = mSurface.lockCanvas(null);
                    h264Canvas.drawColor(Color.GREEN);
                    Iterator it = mShowingList.iterator();

                    while (it != null && it.hasNext()) {
                        DrawInfoItem drawInfoItem = (DrawInfoItem) it.next();
                        PaintTool paintTool = drawInfoItem.mPaintTool;
                        Shape shape = drawInfoItem.mShape;
                        paintTool.drawShape(h264Canvas, shape);
                    }
                    mSurface.unlockCanvasAndPost(h264Canvas);
                     */



                    Canvas h264Canvas = mSurface.lockCanvas(null);
                    h264Canvas.drawColor(Color.BLUE);
                    h264Canvas.drawRect(0, 0 , mWidth / 2, mHeight , paint);
                    //使用一个复制的mH264Bitmap来传输H264流，如果直接使用mBufferBitmap，屏幕会闪，可能时被改变了？
                    if (mH264Bitmap != null && !mH264Bitmap.isRecycled()) {
                        h264Canvas.drawBitmap(mBackgroupBitmap, 0 ,0, null);
                        h264Canvas.drawBitmap(mH264Bitmap, 0, 0, null);
                    }
                    mSurface.unlockCanvasAndPost(h264Canvas);

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
        }
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()){
            //处理按下事件
            case MotionEvent.ACTION_DOWN:
                if (mBufferBitmap == null) {
                    initBuffer();
                }
                if (mPaintToolType == PAINT_TOOL_PEN) {
                    mPaintTool = new Pen(PenSetting.getInstance());
                } else {
                    mPaintTool = new Eraser(EraserSetting.getInstance());
                }

                mShape = Shape.createShapeByType(mShapeType);
                if (mShapeType == Shape.SHAPE_TYPE_CURVE) {
                    mPath = new Path();
                    ((CurveShape)mShape).setPath(mPath);
                }

                //mResumeList.clear();
                mShape.touchDown(motionEvent.getX(), motionEvent.getY());
                mShowingList.add(new DrawInfoItem(mPaintTool, mShape));
                break;

            //在移动的时候进行绘制
            case MotionEvent.ACTION_MOVE:
                //mPath.lineTo(motionEvent.getX(),motionEvent.getY());
                mShape.touchMove(motionEvent.getX(), motionEvent.getY());

                //使用画笔将形状绘制到画布上面
                //mPaintTool.drawShape(mBufferCanvas, mShape);
                redrawShowList();
                invalidate();
                break;
            //在移动的时候进行绘制
            case MotionEvent.ACTION_UP:
                mShape.touchUp(motionEvent.getX(), motionEvent.getY());
                //mPaintTool.drawShape(mBufferCanvas, mShape);
                invalidate();
                break;
        }
        return true;
    }

}
