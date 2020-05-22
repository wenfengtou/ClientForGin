package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wenfengtou.commonutil.FileUtil;
import com.wenfengtou.whiteboard.R;
import com.wenfengtou.whiteboard.adapter.PenColorBean;
import com.wenfengtou.whiteboard.adapter.PenColorSelectAdapter;
import com.wenfengtou.whiteboard.painttool.PaintTool;

import java.util.ArrayList;

public class SketchMenuView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "SketchMenuView";
    private ImageView mExitSketchIv;
    private ImageView mPenIv;
    private ImageView mEraserIv;
    private ImageView mClearSketchIv;
    private ImageView mSaveSketchIv;
    private ImageView mUndoIv;
    private ImageView mRedoIv;
    private ImageView mExpandIv;
    private LinearLayout mExpandll;
    private SketchView mSketchView;
    private Context mContext;

    private static int MENU_STATUS_INIT = 0;
    private static int MENU_STATUS_UNEXPAND = 1;
    private static int MENU_STATUS_EXPAND = 2;

    private int mMenuStatus = MENU_STATUS_INIT;
    private int mNextMenuStatus = MENU_STATUS_INIT;

    public SketchMenuView(Context context) {
        this(context, null);
    }

    public SketchMenuView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SketchMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(mContext);
    }

    public void setSketchView(SketchView sketchView) {
        mSketchView = sketchView;
    }

    private void initView(Context context) {
        ViewGroup root = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_sketch_menu, this, true);
        mExitSketchIv = findViewById(R.id.iv_exit_sketch);
        mPenIv = findViewById(R.id.iv_pen);
        mEraserIv = findViewById(R.id.iv_eraser);
        mSaveSketchIv = findViewById(R.id.iv_save_sketch);
        mClearSketchIv = findViewById(R.id.iv_clear_sketch);
        mUndoIv = findViewById(R.id.iv_undo);
        mRedoIv = findViewById(R.id.iv_redo);
        mExpandIv = findViewById(R.id.iv_expand);
        mExpandll = findViewById(R.id.ll_expand);

        mPenIv.setOnClickListener(this);
        mEraserIv.setOnClickListener(this);
        mClearSketchIv.setOnClickListener(this);
        mSaveSketchIv.setOnClickListener(this);
        mUndoIv.setOnClickListener(this);
        mRedoIv.setOnClickListener(this);
        mExpandIv.setOnClickListener(this);
    }

    /**
     * 显示画笔设置框
     */
    private void showPenPopupWindow() {
        View penToolView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_pen, null, true);
        final int paintToolType = PaintTool.PAINT_TOOL_PEN;
        //颜色选择
        final RecyclerView penColorRv = penToolView.findViewById(R.id.rv_pen_color);
        penColorRv.setLayoutManager(new GridLayoutManager(mContext, 2));
        final PenColorSelectAdapter selectadapter = new PenColorSelectAdapter();
        final ArrayList<PenColorBean> penColorBeans = new ArrayList<>();
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.pen_red), R.drawable.circle_red));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.pen_green), R.drawable.circle_green));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.pen_blue), R.drawable.circle_blue));
        selectadapter.setCurrentColor(mSketchView.getPaintToolColor(paintToolType));
        selectadapter.setPenColorBeanList(penColorBeans);
        selectadapter.setOnItemClickListener(new PenColorSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PenColorBean penColorBean = penColorBeans.get(position);
                selectadapter.setCurrentColor(penColorBean.color);
            }
        });
        penColorRv.setAdapter(selectadapter);

        //大小设置
        final SeekBar penSizeBar = penToolView.findViewById(R.id.sb_pen_size);
        int process = (int)(((float)mSketchView.getPaintToolStrokeWidth(paintToolType)/(float) mSketchView.getPaintToolMaxStrokeWidth(paintToolType)) * 100);
        penSizeBar.setProgress(process);


        PopupWindow colorPopUpWindow = new PopupWindow(penToolView, 300, 200);
        colorPopUpWindow.setFocusable(true);
        int[] location = new int[2];
        mPenIv.getLocationOnScreen(location);
        //colorPopUpWindow.showAsDropDown(mPenll);
        colorPopUpWindow.showAtLocation(mPenIv, Gravity.NO_GRAVITY, location[0], location[1]-colorPopUpWindow.getHeight());
        colorPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //更新颜色
                int color = selectadapter.getCurrentColor();
                mSketchView.setPaintToolColor(paintToolType, color);
                //更新大小
                float percent = (float)penSizeBar.getProgress()/(float)100;
                int size = (int) (percent * mSketchView.getPaintToolMaxStrokeWidth(paintToolType));
                mSketchView.setPaintToolStrokeWidth(paintToolType,  size);
            }
        });
    }


    /**
     * 显示橡皮擦设置框
     */
    private void showEraserPopupWindow() {
        View eraseToolView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_eraser, null, true);
        final int paintToolType = PaintTool.PAINT_TOOL_ERASER;
        //大小设置
        final SeekBar eraseSizeBar = eraseToolView.findViewById(R.id.sb_eraser_size);
        int process = (int)(((float)mSketchView.getPaintToolStrokeWidth(paintToolType)/(float) mSketchView.getPaintToolMaxStrokeWidth(paintToolType)) * 100);
        eraseSizeBar.setProgress(process);

        PopupWindow erasePopUpWindow = new PopupWindow(eraseToolView, 300, 80);
        erasePopUpWindow.setFocusable(true);
        int[] location = new int[2];
        mEraserIv.getLocationOnScreen(location);
        //erasePopUpWindow.showAsDropDown(mEraserll);
        erasePopUpWindow.showAtLocation(mEraserIv, Gravity.NO_GRAVITY, location[0], location[1] - erasePopUpWindow.getHeight());
        erasePopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //更新大小
                float percent = (float)eraseSizeBar.getProgress()/(float)100;
                int size = (int) (percent * mSketchView.getPaintToolMaxStrokeWidth(paintToolType));
                mSketchView.setPaintToolStrokeWidth(paintToolType,  size);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    private void setNextMenuStatus(int status) {
        mNextMenuStatus = status;
        if (status == MENU_STATUS_INIT) {
            mPenIv.setBackground(getResources().getDrawable(R.drawable.ic_pen_unpressed));
            mExitSketchIv.setVisibility(GONE);
            mExpandIv.setVisibility(GONE);
            mExpandll.setVisibility(GONE);
        } else if (status == MENU_STATUS_EXPAND) {
            mExpandll.setVisibility(VISIBLE);
            mExpandIv.setBackground(getResources().getDrawable(R.drawable.ic_unexpand));
        } else if (status == MENU_STATUS_UNEXPAND) {
            mExitSketchIv.setVisibility(VISIBLE);
            mExpandll.setVisibility(GONE);
            mExpandIv.setVisibility(VISIBLE);
            mExpandIv.setBackground(getResources().getDrawable(R.drawable.ic_expand));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_exit_sketch) {
            mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_NONE);
            setNextMenuStatus(MENU_STATUS_INIT);
        } else if (id == R.id.iv_pen) {
            if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_PEN) {
                showPenPopupWindow();
                return;
            }
            if (mMenuStatus == MENU_STATUS_INIT) {
                setNextMenuStatus(MENU_STATUS_UNEXPAND);
            }
            mPenIv.setBackground(getResources().getDrawable(R.drawable.ic_pen_pressed));
            mEraserIv.setBackground(getResources().getDrawable(R.drawable.ic_eraser_unpressed));
            mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_PEN);
        } else if (id == R.id.iv_eraser) {
            if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_ERASER) {
                showEraserPopupWindow();
            }
            mEraserIv.setBackground(getResources().getDrawable(R.drawable.ic_eraser_pressed));
            mPenIv.setBackground(getResources().getDrawable(R.drawable.ic_pen_unpressed));
            mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_ERASER);
        } else if (id == R.id.iv_clear_sketch) {
            mSketchView.clear();
        } else if (id == R.id.iv_save_sketch) {
            Bitmap bitmap = mSketchView.getBitmap();
            FileUtil.saveBitmap("sdcard/sket.png", bitmap);
            Toast.makeText(mContext, R.string.save_sketch_success, Toast.LENGTH_LONG).show();
        } else if (id == R.id.iv_undo) {
            mSketchView.undo();
        } else if (id == R.id.iv_redo) {
            mSketchView.redo();
        } else if (id == R.id.iv_expand) {
            if (mMenuStatus == MENU_STATUS_EXPAND) {
                setNextMenuStatus(MENU_STATUS_UNEXPAND);
            } else {
                setNextMenuStatus(MENU_STATUS_EXPAND);
            }
        }
    }

    private boolean mIsExpanding = false;
    private float mDownX; //点击时的x坐标
    private float mDownY;  // 点击时的y坐标
    private int mWidth; //  测量宽度 FreeView的宽度
    private int mHeight; // 测量高度 FreeView的高度
    private boolean mIsDrag = false;

    private int mExitSketchIvWidth;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取屏宽高 和 可是适用范围 （我的需求是可在屏幕内拖动 不超出范围 也不需要隐藏）
        mWidth = getMeasuredWidth();
        mHeight= getMeasuredHeight();
        Log.i(TAG, "onMeasure mWidth=" + mWidth + " mHeight=" + mHeight);
        //不同状态切换时，位置适配
        if (mNextMenuStatus == MENU_STATUS_INIT) {

        } else if (mNextMenuStatus == MENU_STATUS_UNEXPAND) {
            if (mMenuStatus == MENU_STATUS_INIT) { //由INIT状态转换为未展开状态
                int exitSketchIvWidth = mExitSketchIv.getMeasuredWidth();
                int expandIvWidth = mExpandIv.getMeasuredWidth();
                int left = mCurrentRect.left - exitSketchIvWidth;
                int right = mCurrentRect.right + expandIvWidth;
                if (left < mMinLeft) {  //向左边展开按钮时，小于边界
                    mCurrentRect.left = mMinLeft;
                    mCurrentRect.right = mMinLeft + mWidth;
                } else if (right > mMaxRight) {
                    mCurrentRect.left = mMaxRight - mWidth;
                    mCurrentRect.right = mMaxRight;
                } else {
                    mCurrentRect.left = left;
                    mCurrentRect.right = right;
                }
            } else if (mMenuStatus == MENU_STATUS_EXPAND) { //由展开状态变成未展开状态
                mCurrentRect.right = mCurrentRect.left + mWidth;
            }
        } else if (mNextMenuStatus == MENU_STATUS_EXPAND) {
            int right = mCurrentRect.left + mWidth;
            if (right > mMaxRight) {  //向右边展开按钮时，小于边界
                mCurrentRect.left = mMaxRight - mWidth;
                mCurrentRect.right = mMaxRight;
            } else {
                mCurrentRect.right = right;
            }
        }
        //修改状态
        mMenuStatus = mNextMenuStatus;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        updataView(ev);
        return (mIsDrag || super.onInterceptTouchEvent(ev));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return updataView(event);
    }

    private int mMinLeft = 0;
    private int mMaxRight = 1920;
    private int mMinTop = 0;
    private int mMaxBottom = 960;

    private Rect mCurrentRect = new Rect(0, 0 ,0 , 0);

    public Rect getCurrentRect() {
        return mCurrentRect;
    }

    public boolean updataView(MotionEvent event) {
        super.onTouchEvent(event);
        if (this.isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIsDrag = false;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE: // 滑动动作处理 记录离开屏幕时的 moveX  moveY 用于计算距离 和 判断滑动事件和点击事件 并作出响应
                    final float moveX = event.getX() - mDownX;
                    final float moveY = event.getY() - mDownY;
                    int l,r,t,b; // 上下左右四点移动后的偏移量
                    //计算偏移量 设置偏移量 = 3 时 为判断点击事件和滑动事件的峰值
                    if (Math.abs(moveX) > 3 ||Math.abs(moveY) > 3) { // 偏移量的绝对值大于 3 为 滑动时间 并根据偏移量计算四点移动后的位置
                        mIsDrag = true;
                        l = (int) (getLeft() + moveX);
                        r = l + mWidth;
                        t = (int) (getTop() + moveY);
                        b = t + mHeight;
                        Log.i(TAG, "l =" + l + " t=" + t + " r=" + r + " b=" + b);
                        if (l < mMinLeft) {
                            l = mMinLeft;
                            r = mMinLeft + mWidth;
                        }
                        if (r > mMaxRight) {
                            l = mMaxRight - mWidth;
                            r = mMaxRight;
                        }
                        if (t < mMinTop) {
                            t = mMinTop;
                            b = mMinTop + mHeight;
                        }
                        if (b > mMaxBottom) {
                            t = mMaxBottom - mHeight;
                            b = mMaxBottom;
                        }
                        mCurrentRect.set(l, t, r, b);
                        this.layout(l, t, r, b); // 重置view在layout 中位置
                    } else {

                    }
                    break;
                case MotionEvent.ACTION_UP: // 不处理
                    Log.i(TAG, "MotionEvent.ACTION_UP");
                    //setPressed(false);
                    break;
                case MotionEvent.ACTION_CANCEL: // 不处理
                    Log.i(TAG, "MotionEvent.ACTION_CANCEL");
                    //setPressed(false);
                    break;
            }
            return true;
        }
        return false;
    }

}
