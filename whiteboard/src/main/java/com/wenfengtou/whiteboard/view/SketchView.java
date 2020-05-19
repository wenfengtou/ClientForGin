package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wenfengtou.whiteboard.R;
import com.wenfengtou.whiteboard.painttool.Eraser;
import com.wenfengtou.whiteboard.painttool.PaintTool;
import com.wenfengtou.whiteboard.painttool.Pen;
import com.wenfengtou.whiteboard.setting.EraserSetting;
import com.wenfengtou.whiteboard.setting.PenSetting;
import com.wenfengtou.whiteboard.shape.CurveShape;
import com.wenfengtou.whiteboard.shape.Shape;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SketchView extends View implements View.OnTouchListener {

    private static final String TAG = "NormalSketchView";
    private int mWidth;
    private int mHeight;
    private Path mPath = new Path();
    private Paint mPaint;
    private Surface mSurface;
    private Paint mErasePaint;
    private Paint mPenPaint;
    private int mPaintToolType = PaintTool.PAINT_TOOL_PEN;
    private Bitmap mBufferBitmap;
    private Canvas mBufferCanvas;

    private Bitmap mH264Bitmap;
    private Bitmap mBackgroupBitmap;
    private volatile CopyOnWriteArrayList<DrawInfoItem> mShowingList = new CopyOnWriteArrayList();
    private CopyOnWriteArrayList<DrawInfoItem> mResumeList = new CopyOnWriteArrayList();

    private PaintTool mPaintTool;
    private int mShapeType = Shape.SHAPE_TYPE_CURVE;
    private Shape mShape;
    private Thread mH264Thread;

    private boolean mIsReDrawShowList = false;
    private boolean mIsSendH264 = true;
    private byte[] mPixels;

    class DrawInfoItem {
        public PaintTool mPaintTool;
        public Shape mShape;
        public DrawInfoItem(PaintTool paintTool, Shape shape) {
            mPaintTool = paintTool;
            mShape = shape;
        }
    }

    public SketchView(Context context) {
        this(context, null);
    }

    public SketchView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public void setSurface(Surface surface) {
        mSurface = surface;
    }

    public void setBackgroupBitmap(Bitmap bitmap) {
        mBackgroupBitmap = bitmap;
    }

    public SketchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setBackgroundColor(Color.BLUE);
        mPaint = mPenPaint;
        //mBackgroupBitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.back3)).getBitmap();
        setOnTouchListener(this);
        RecyclerView recyclerView;
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
        Log.i(TAG, "initBuffer width=" + mWidth + " height=" + mHeight);
        mPixels = new byte[mWidth * mHeight * 4];
        mBufferBitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mH264Bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        mBufferCanvas = new Canvas(mBufferBitmap);
    }

    public void choosePaintTool(int paintToolType) {
        mPaintToolType = paintToolType;
    }

    public int getPaintToolType() {
        return mPaintToolType;
    }

    public int getPaintToolColor(int paintToolType) {
        int paintToolColor = 0;
        if (paintToolType == PaintTool.PAINT_TOOL_PEN) {
            paintToolColor = PenSetting.getInstance().getColor();
        } else if (paintToolType == PaintTool.PAINT_TOOL_ERASER) {
            paintToolColor = EraserSetting.getInstance().getColor();
        }
        return paintToolColor;
    }

    public void setPaintToolColor(int paintToolType, int color) {
        if (paintToolType == PaintTool.PAINT_TOOL_PEN) {
            PenSetting.getInstance().setColor(color);
        }
    }

    public void setPaintToolStrokeWidth(int paintToolType, int size) {
        if (paintToolType == PaintTool.PAINT_TOOL_PEN) {
            PenSetting.getInstance().setStrokeWidth(size);
        } else if (paintToolType == PaintTool.PAINT_TOOL_ERASER) {
            EraserSetting.getInstance().setStrokeWidth(size);
        }
    }

    public int getPaintToolStrokeWidth(int paintToolType) {
        int paintToolStrokeWidth = 0;
        if (paintToolType == PaintTool.PAINT_TOOL_PEN) {
            paintToolStrokeWidth = PenSetting.getInstance().getStrokeWidth();
        } else if (paintToolType == PaintTool.PAINT_TOOL_ERASER) {
            paintToolStrokeWidth = EraserSetting.getInstance().getStrokeWidth();
        }
        return paintToolStrokeWidth;
    }

    public int getPaintToolMaxStrokeWidth(int paintToolType) {
        int paintToolMaxStrokeWidth = 0;
        if (paintToolType == PaintTool.PAINT_TOOL_PEN) {
            paintToolMaxStrokeWidth = PenSetting.getInstance().getMaxStrokeWidth();
        } else if (paintToolType == PaintTool.PAINT_TOOL_ERASER) {
            paintToolMaxStrokeWidth = EraserSetting.getInstance().getMaxStrokeWidth();
        }
        return paintToolMaxStrokeWidth;
    }

    //撤销
    public void undo() {
        Toast.makeText(getContext(), R.string.cancel_write, Toast.LENGTH_LONG).show();
        if (mShowingList.size() > 0) {
            DrawInfoItem removeDrawInfoItem = mShowingList.remove(mShowingList.size() -1);
            mResumeList.add(removeDrawInfoItem);
            drawShowList(mShowingList, mBufferCanvas, true);
            invalidate();
        }
    }

    //重做
    public void redo() {
        Toast.makeText(getContext(), R.string.resume_write, Toast.LENGTH_LONG).show();
        if (mResumeList.size() > 0) {
            DrawInfoItem resumeDrawInfoItem = mResumeList.remove(mResumeList.size() -1);
            mShowingList.add(resumeDrawInfoItem);
            drawShowList(mShowingList, mBufferCanvas, true);
            invalidate();
        }
    }

    public void clear() {
        mResumeList.clear();
        mShowingList.clear();
        mBufferBitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(mWidth, mHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mBackgroupBitmap, 0, 0, null);
        canvas.drawBitmap(mBufferBitmap, 0, 0, null);
        return bitmap;
    }

    private void drawShowList(CopyOnWriteArrayList<DrawInfoItem> list, Canvas canvas, boolean clear) {
        if (mShowingList != null) {
            if (clear) {
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            }
            Iterator it = mShowingList.iterator();
            while (it != null && it.hasNext()) {
                DrawInfoItem drawInfoItem = (DrawInfoItem) it.next();
                PaintTool paintTool = drawInfoItem.mPaintTool;
                Shape shape = drawInfoItem.mShape;
                paintTool.drawShape(canvas, shape);
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


        //绘制背景
        if (mBackgroupBitmap != null) {
            canvas.drawBitmap(mBackgroupBitmap, 0 ,0, null);
        }
        //绘制图形
        if (mBufferBitmap != null) {
            canvas.drawBitmap(mBufferBitmap, 0, 0, null);
            long start = SystemClock.uptimeMillis();
            ByteBuffer buffer = ByteBuffer.wrap(mPixels);
            buffer.rewind();
            mBufferBitmap.copyPixelsToBuffer(buffer);
            buffer.flip();
            mH264Bitmap.copyPixelsFromBuffer(buffer);
            long end = SystemClock.uptimeMillis();
            Log.i(TAG, "copy time=" + (end - start));
            //mH264Bitmap = mBufferBitmap.copy(Bitmap.Config.ARGB_8888, false);
        }
        /*
        Canvas h264Canvas = mSurface.lockCanvas(null);
        drawCanvas(h264Canvas);
        mSurface.unlockCanvasAndPost(h264Canvas);
         */
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsSendH264 = false;
    }

    public void startDecoreThread() {

        mH264Thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //流绘制
                int i = 0;
                while (mIsSendH264) {
                    i++;
                    //Log.i("wenfengwenfeng","i=" + i);
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
                    //使用一个复制的mH264Bitmap来传输H264流，如果直接使用mBufferBitmap，屏幕会闪，可能时被改变了？
                    if (mH264Bitmap != null && !mH264Bitmap.isRecycled()) {
                        h264Canvas.drawBitmap(mBackgroupBitmap, 0 ,0, null);
                        h264Canvas.drawBitmap(mH264Bitmap, 0, 0, null);
                    }
                    mSurface.unlockCanvasAndPost(h264Canvas);
                }
                mH264Bitmap.recycle();
                mBufferBitmap.recycle();
                mBackgroupBitmap.recycle();
            }
        });
        mH264Thread.start();
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
                if (mPaintToolType == PaintTool.PAINT_TOOL_PEN) {
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
                mPaintTool.drawShape(mBufferCanvas, mShape);
                //redrawShowList();
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
