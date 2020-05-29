package cn.com.ava.whiteboard.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Process;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.wenfengtou.commonutil.FileUtil;


import java.util.ArrayList;

import cn.com.ava.whiteboard.R;
import cn.com.ava.whiteboard.adapter.PenColorBean;
import cn.com.ava.whiteboard.adapter.PenColorSelectAdapter;
import cn.com.ava.whiteboard.adapter.StrokeWidthBean;
import cn.com.ava.whiteboard.adapter.StrokeWidthSelectAdapter;
import cn.com.ava.whiteboard.painttool.PaintTool;

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

    private ViewGroup mViewRoot;

    private static int MENU_STATUS_INIT = 0;
    private static int MENU_STATUS_UNEXPANDED = 1;
    private static int MENU_STATUS_EXPANDED = 2;

    private int mDefaultPaintTool = PaintTool.PAINT_TOOL_PEN;
    private int mDefaultMenuStatus = MENU_STATUS_UNEXPANDED;
    private int mMenuStatus = MENU_STATUS_UNEXPANDED; //第一个状态为收缩状态
    private int mLastMenuStatus = MENU_STATUS_UNEXPANDED;

    private int mRotation = -1;
    private WindowManager mWindowManager;
    private AlertDialog mClearAlertDialog;
    private PopupWindow mPenPopupWindow;
    private int mPenPopupWindowHeight;
    private PopupWindow mEraserPopupWindow;
    private int mEraserPopupWindowHeight;

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
        onSketchViewBind(mSketchView, mDefaultPaintTool, mDefaultMenuStatus);
    }

    private void onSketchViewBind(SketchView sketchView, int defaultPaintTool, int defaultMenuStatus) {
        sketchView.choosePaintTool(defaultPaintTool);
        setMenuStatus(defaultMenuStatus);
    }

    private void initView(Context context) {
        mViewRoot = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.layout_sketch_menu, this, true);
        mExitSketchIv = findViewById(R.id.iv_exit_sketch);
        mPenIv = findViewById(R.id.iv_pen);
        mEraserIv = findViewById(R.id.iv_eraser);
        mSaveSketchIv = findViewById(R.id.iv_save_sketch);
        mClearSketchIv = findViewById(R.id.iv_clear_sketch);
        mUndoIv = findViewById(R.id.iv_undo);
        mRedoIv = findViewById(R.id.iv_redo);
        mExpandIv = findViewById(R.id.iv_expand);
        mExpandll = findViewById(R.id.ll_expand);

        mExitSketchIv.setOnClickListener(this);
        mPenIv.setOnClickListener(this);
        mEraserIv.setOnClickListener(this);
        mClearSketchIv.setOnClickListener(this);
        mSaveSketchIv.setOnClickListener(this);
        mUndoIv.setOnClickListener(this);
        mRedoIv.setOnClickListener(this);
        mExpandIv.setOnClickListener(this);
        initDialog(context);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }


    private void initPenPopupWindow() {
        View penToolView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_pen, null, true);
        final int paintToolType = PaintTool.PAINT_TOOL_PEN;
        //颜色选择
        final RecyclerView penColorRv = penToolView.findViewById(R.id.rv_pen_color);
        penColorRv.setLayoutManager(new GridLayoutManager(mContext, 3));
        final PenColorSelectAdapter colorSelectAdapter = new PenColorSelectAdapter();
        final ArrayList<PenColorBean> penColorBeans = new ArrayList<>();
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.pen_red), R.drawable.paintcolor_red));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.pen_green), R.drawable.paintcolor_green));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.pen_blue), R.drawable.paintcolor_blue));
        colorSelectAdapter.setCurrentColor(mSketchView.getPaintToolColor(paintToolType));
        colorSelectAdapter.setPenColorBeanList(penColorBeans);
        colorSelectAdapter.setOnItemClickListener(new PenColorSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                PenColorBean penColorBean = penColorBeans.get(position);
                colorSelectAdapter.setCurrentColor(penColorBean.color);
            }
        });
        penColorRv.setAdapter(colorSelectAdapter);

        //大小选择
        final RecyclerView penSizeRv = penToolView.findViewById(R.id.rv_pen_size);
        penSizeRv.setLayoutManager(new GridLayoutManager(mContext, 3));
        final StrokeWidthSelectAdapter sizeSelectAdapter = new StrokeWidthSelectAdapter();
        final ArrayList<StrokeWidthBean> strokeWidthBeans = new ArrayList<>();
        strokeWidthBeans.add(new StrokeWidthBean(R.drawable.paintsize_level1_pressed, R.drawable.paintsize_level1_unpressed, 10));
        strokeWidthBeans.add(new StrokeWidthBean(R.drawable.paintsize_level2_pressed, R.drawable.paintsize_level2_unpressed, 30));
        sizeSelectAdapter.setCurrentStrokeWidth(mSketchView.getPaintToolStrokeWidth(paintToolType));
        sizeSelectAdapter.setPenStrokeWidthBeanList(strokeWidthBeans);
        sizeSelectAdapter.setOnItemClickListener(new StrokeWidthSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StrokeWidthBean strokeWidthBean = strokeWidthBeans.get(position);
                sizeSelectAdapter.setCurrentStrokeWidth(strokeWidthBean.strokeWidth);
            }
        });
        penSizeRv.setAdapter(sizeSelectAdapter);


        mPenPopupWindow = new PopupWindow(penToolView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        penToolView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mPenPopupWindowHeight = penToolView.getMeasuredHeight();
        mPenPopupWindow.setFocusable(true);
        mPenPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //更新颜色
                int color = colorSelectAdapter.getCurrentColor();
                mSketchView.setPaintToolColor(paintToolType, color);
                //更新大小
                int size = sizeSelectAdapter.getCurrentStrokeWidth();
                mSketchView.setPaintToolStrokeWidth(paintToolType,  size);
            }
        });
    }

    private void initEraserPopupWindow() {
        View eraseToolView = LayoutInflater.from(mContext).inflate(R.layout.layout_popupwindow_eraser, null, true);
        final int paintToolType = PaintTool.PAINT_TOOL_ERASER;
        //大小选择
        final RecyclerView eraserSizeRv = eraseToolView.findViewById(R.id.rv_eraser_size);
        eraserSizeRv.setLayoutManager(new GridLayoutManager(mContext, 3));
        final StrokeWidthSelectAdapter sizeSelectAdapter = new StrokeWidthSelectAdapter();
        final ArrayList<StrokeWidthBean> strokeWidthBeans = new ArrayList<>();
        strokeWidthBeans.add(new StrokeWidthBean(R.drawable.paintsize_level1_pressed, R.drawable.paintsize_level1_unpressed, 10));
        strokeWidthBeans.add(new StrokeWidthBean(R.drawable.paintsize_level2_pressed, R.drawable.paintsize_level2_unpressed, 30));
        sizeSelectAdapter.setCurrentStrokeWidth(mSketchView.getPaintToolStrokeWidth(paintToolType));
        sizeSelectAdapter.setPenStrokeWidthBeanList(strokeWidthBeans);
        sizeSelectAdapter.setOnItemClickListener(new StrokeWidthSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StrokeWidthBean strokeWidthBean = strokeWidthBeans.get(position);
                sizeSelectAdapter.setCurrentStrokeWidth(strokeWidthBean.strokeWidth);
            }
        });
        eraserSizeRv.setAdapter(sizeSelectAdapter);

        mEraserPopupWindow = new PopupWindow(eraseToolView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        eraseToolView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mEraserPopupWindowHeight = eraseToolView.getMeasuredHeight();
        mEraserPopupWindow.setFocusable(true);

        mEraserPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //更新大小
                int size = sizeSelectAdapter.getCurrentStrokeWidth();
                mSketchView.setPaintToolStrokeWidth(paintToolType,  size);
            }
        });
    }

    private void initDialog(Context context) {
        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setMessage("清空画板？");
        builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSketchView.clear();
            }
        });
        builder.setNegativeButton(R.string.cancle, null);
        mClearAlertDialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mClearAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            mClearAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mClearAlertDialog.setCanceledOnTouchOutside(false);//点击外面区域不会让dialog消失
    }

    /**
     * 显示画笔设置框
     */
    private void showPenPopupWindow() {
        if (mPenPopupWindow == null) {
            initPenPopupWindow();
        }
        int[] location = new int[2];
        mPenIv.getLocationInWindow(location);
        if (location[1] < mPenPopupWindowHeight) {
            mPenPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] ,location[1] + mPenIv.getHeight());
        } else {
            mPenPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] ,location[1] - mPenPopupWindowHeight);
        }
    }


    /**
     * 显示橡皮擦设置框
     */
    private void showEraserPopupWindow() {
        if (mEraserPopupWindow == null) {
            initEraserPopupWindow();
        }
        int[] location = new int[2];
        mEraserIv.getLocationInWindow(location);
        if (location[1] < mEraserPopupWindowHeight) {
            mEraserPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] ,location[1] + mEraserIv.getHeight());
        } else {
            mEraserPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] ,location[1] - mEraserPopupWindowHeight);
        }
    }

    private void dismissAllPopupWindow() {
        if (mEraserPopupWindow != null && mEraserPopupWindow.isShowing()) {
            mEraserPopupWindow.dismiss();
        }
        if (mEraserPopupWindow != null && mPenPopupWindow.isShowing()) {
            mPenPopupWindow.dismiss();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "menu onConfigurationChanged");
    }

    public void setMenuStatus(int status) {
        mLastMenuStatus = mMenuStatus;
        mMenuStatus = status;
        if (status == MENU_STATUS_INIT) {
            mPenIv.setBackground(getResources().getDrawable(R.drawable.ic_pen_unpressed));
            mExitSketchIv.setVisibility(GONE);
            mExpandIv.setVisibility(GONE);
            mExpandll.setVisibility(GONE);
        } else if (status == MENU_STATUS_EXPANDED) {
            mExpandll.setVisibility(VISIBLE);
            mExpandIv.setBackground(getResources().getDrawable(R.drawable.ic_unexpand));
        } else if (status == MENU_STATUS_UNEXPANDED) {
            mExitSketchIv.setVisibility(VISIBLE);
            mExpandll.setVisibility(GONE);
            mExpandIv.setVisibility(VISIBLE);
            mExpandIv.setBackground(getResources().getDrawable(R.drawable.ic_expand));
        }

        if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_PEN) {
            mPenIv.setBackground(getResources().getDrawable(R.drawable.ic_pen_pressed));
            mEraserIv.setBackground(getResources().getDrawable(R.drawable.ic_eraser_unpressed));
        } else if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_ERASER) {
            mEraserIv.setBackground(getResources().getDrawable(R.drawable.ic_eraser_pressed));
            mPenIv.setBackground(getResources().getDrawable(R.drawable.ic_pen_unpressed));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_exit_sketch) {
            mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_NONE);
            setMenuStatus(MENU_STATUS_INIT);
            Process.killProcess(Process.myPid());
        } else if (id == R.id.iv_pen) {
            if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_PEN) {
                showPenPopupWindow();
                return;
            }
            if (mMenuStatus == MENU_STATUS_INIT) {
                setMenuStatus(MENU_STATUS_UNEXPANDED);
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
            mClearAlertDialog.show();
        } else if (id == R.id.iv_save_sketch) {
            Bitmap bitmap = mSketchView.getBitmap();
            //FileUtil.saveBitmap("sdcard/sket.png", bitmap);
            Toast.makeText(mContext, R.string.save_sketch_success, Toast.LENGTH_LONG).show();
        } else if (id == R.id.iv_undo) {
            mSketchView.undo();
        } else if (id == R.id.iv_redo) {
            mSketchView.redo();
        } else if (id == R.id.iv_expand) {
            if (mMenuStatus == MENU_STATUS_EXPANDED) {
                setMenuStatus(MENU_STATUS_UNEXPANDED);
            } else {
                setMenuStatus(MENU_STATUS_EXPANDED);
            }
        }
    }

    private float mDownX; //点击时的x坐标
    private float mDownY;  // 点击时的y坐标
    private int mWidth;
    private int mHeight;
    private boolean mIsDrag = false;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        int rotation = mWindowManager.getDefaultDisplay().getRotation();
        Log.i(TAG, "mWidth = " + mWidth + " mHeight = " + mHeight + " rotation=" + rotation);
        if (mRotation != rotation) { //横竖屏变化后，将菜单位置居中
            mRotation = rotation;
            if (mClearAlertDialog.isShowing()) {
                mClearAlertDialog.dismiss();
            }
            dismissAllPopupWindow();
            mCurrentRect.setEmpty();
        }
        //setVisibility会重新跑onMeasure，不同状态切换时，位置适配，确保不超过边界
        if (mMenuStatus == MENU_STATUS_INIT) {

        } else if (mMenuStatus == MENU_STATUS_UNEXPANDED) {
            if (mLastMenuStatus == MENU_STATUS_INIT) { //由INIT状态转换为未展开状态
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
            } else if (mLastMenuStatus == MENU_STATUS_EXPANDED) { //由展开状态变成未展开状态
                mCurrentRect.right = mCurrentRect.left + mWidth;
            }
        } else if (mMenuStatus == MENU_STATUS_EXPANDED) {
            int right = mCurrentRect.left + mWidth;
            if (right > mMaxRight) {  //向右边展开按钮时，小于边界
                mCurrentRect.left = mMaxRight - mWidth;
                mCurrentRect.right = mMaxRight;
            } else {
                mCurrentRect.right = right;
            }
        }
        //修改状态
        //mMenuStatus = mNextMenuStatus;
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        updateView(ev);
        return (mIsDrag || super.onInterceptTouchEvent(ev));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return updateView(event);
    }

    private int mMinLeft = 0;
    private int mMinTop = 0;
    private int mMaxRight = -1;
    private int mMaxBottom = -1;
    //private int mMaxBottom = 1132;

    private Rect mCurrentRect = new Rect(0, 0 ,0 , 0);

    public Rect getCurrentRect() {
        return mCurrentRect;
    }

    private void updateLimit() {
        View parent = ((View)getParent());
        int maxRight = parent.getRight();
        int maxBottom = parent.getBottom();
        if (mMaxRight != maxRight || mMaxBottom != maxBottom) {
            mMaxRight = maxRight;
            mMaxBottom = maxBottom;
            Log.i(TAG, "updateLimit mMaxRight = " + mMaxRight + " mMaxBottom = " + mMaxBottom);
        }
    }
    public boolean updateView(MotionEvent event) {
        super.onTouchEvent(event);
        updateLimit();
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
