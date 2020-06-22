package cn.com.ava.whiteboard.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.wenfengtou.commonutil.FileUtil;


import java.util.ArrayList;

import cn.com.ava.whiteboard.R;
import cn.com.ava.whiteboard.adapter.PenColorBean;
import cn.com.ava.whiteboard.adapter.PenColorSelectAdapter;
import cn.com.ava.whiteboard.adapter.SpacesItemDecoration;
import cn.com.ava.whiteboard.adapter.StrokeWidthBean;
import cn.com.ava.whiteboard.adapter.StrokeWidthSelectAdapter;
import cn.com.ava.whiteboard.drawable.SharpDrawable;
import cn.com.ava.whiteboard.drawable.SharpView;
import cn.com.ava.whiteboard.painttool.PaintTool;

public class SketchMenuView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = "SketchMenuView";
    private ImageView mExitSketchIv;
    private LinearLayout mPenLl;
    private ImageView mPenIv;
    private ImageView mPenUpTriangleIv;
    private ImageView mPenDownTriangleIv;

    private LinearLayout mEraserLl;
    private ImageView mEraserIv;
    private ImageView mEraserUpTriangleIv;
    private ImageView mEraserDownTriangleIv;

    private ImageView mClearSketchIv;
    private ImageView mSaveSketchIv;
    private ImageView mUndoIv;
    private ImageView mRedoIv;
    private ImageView mExpandIv;
    private ConstraintLayout mExpandCl;
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
    private int mPenPopupWindowWidth;
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
        mViewRoot = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.whiteboard_sketch_menu, this, true);
        mExitSketchIv = findViewById(R.id.iv_exit_sketch);
        mPenLl = findViewById(R.id.ll_pen);
        mPenIv = findViewById(R.id.iv_pen);
        mPenUpTriangleIv = findViewById(R.id.iv_pen_up_triangle);
        mPenDownTriangleIv = findViewById(R.id.iv_pen_down_triangle);
        mEraserLl = findViewById(R.id.ll_eraser);
        mEraserIv = findViewById(R.id.iv_eraser);
        mEraserUpTriangleIv = findViewById(R.id.iv_eraser_up_triangle);
        mEraserDownTriangleIv =findViewById(R.id.iv_eraser_down_triangle);
        mSaveSketchIv = findViewById(R.id.iv_save_sketch);
        mClearSketchIv = findViewById(R.id.iv_clear_sketch);
        mUndoIv = findViewById(R.id.iv_undo);
        mRedoIv = findViewById(R.id.iv_redo);
        mExpandIv = findViewById(R.id.iv_expand);
        mExpandCl = findViewById(R.id.cl_expand);

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
        View penToolView = LayoutInflater.from(mContext).inflate(R.layout.whiteboard_popupwindow_pen, null, false);

        final int paintToolType = PaintTool.PAINT_TOOL_PEN;

        int leftRightMargin = getResources().getDimensionPixelSize(R.dimen.sw_5dp);
        //颜色选择
        final RecyclerView penColorRv = penToolView.findViewById(R.id.rv_pen_color);
        penColorRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false));
        //penColorRv.setLayoutManager(new GridLayoutManager(mContext, 8));
        final PenColorSelectAdapter colorSelectAdapter = new PenColorSelectAdapter();
        final ArrayList<PenColorBean> penColorBeans = new ArrayList<>();
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_000000), R.drawable.whiteboard_pen_color_000000));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_929699), R.drawable.whiteboard_pen_color_929699));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_e62223), R.drawable.whiteboard_pen_color_e62223));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_ff6a0c), R.drawable.whiteboard_pen_color_ff6a0c));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_f7b500), R.drawable.whiteboard_pen_color_f7b500));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_00be50), R.drawable.whiteboard_pen_color_00be50));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_43d7b5), R.drawable.whiteboard_pen_color_43d7b5));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_006fff), R.drawable.whiteboard_pen_color_006fff));
        penColorBeans.add(new PenColorBean(getResources().getColor(R.color.whiteboard_6236ff), R.drawable.whiteboard_pen_color_6236ff));
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
        //设置item之间的间隔
        penColorRv.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.sw_5dp), leftRightMargin, false));


        //大小选择
        final RecyclerView penSizeRv = penToolView.findViewById(R.id.rv_pen_size);
        penSizeRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false));
        //penSizeRv.setLayoutManager(new GridLayoutManager(mContext, 4));
        final StrokeWidthSelectAdapter sizeSelectAdapter = new StrokeWidthSelectAdapter();
        final ArrayList<StrokeWidthBean> strokeWidthBeans = new ArrayList<>();
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.pen_size1), R.drawable.whiteboard_paint_tool_size1));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.pen_size2), R.drawable.whiteboard_paint_tool_size2));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.pen_size3), R.drawable.whiteboard_paint_tool_size3));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.pen_size4), R.drawable.whiteboard_paint_tool_size4));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.pen_size5), R.drawable.whiteboard_paint_tool_size5));
       // strokeWidthBeans.add(new StrokeWidthBean(R.drawable.whiteboard_pen_size1_pressed, R.drawable.whiteboard_pen_size1_unpressed, getResources().getDimensionPixelSize(R.dimen.pen_size1)));
       // strokeWidthBeans.add(new StrokeWidthBean(R.drawable.whiteboard_pen_size2_pressed, R.drawable.whiteboard_pen_size2_unpressed, getResources().getDimensionPixelSize(R.dimen.pen_size2)));
       // strokeWidthBeans.add(new StrokeWidthBean(R.drawable.whiteboard_pen_size3_pressed, R.drawable.whiteboard_pen_size3_unpressed, getResources().getDimensionPixelSize(R.dimen.pen_size3)));
       // strokeWidthBeans.add(new StrokeWidthBean(R.drawable.whiteboard_pen_size4_pressed, R.drawable.whiteboard_pen_size4_unpressed, getResources().getDimensionPixelSize(R.dimen.pen_size4)));
       // strokeWidthBeans.add(new StrokeWidthBean(R.drawable.whiteboard_pen_size5_pressed, R.drawable.whiteboard_pen_size5_unpressed, getResources().getDimensionPixelSize(R.dimen.pen_size5)));
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

        penToolView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mPenPopupWindowHeight = penToolView.getMeasuredHeight();
        mPenPopupWindowWidth = penToolView.getMeasuredWidth();

        /*
        //测量完之后，设置画笔大小RecyclerView的间距，使其均匀分布
        int penSizeRvItemWidth = getResources().getDimensionPixelSize(R.dimen.sw_35dp);
        int penSizeRvItemWidthSum = penSizeRvItemWidth * sizeSelectAdapter.getItemCount();
        int space = (mPenPopupWindowWidth - penSizeRvItemWidthSum - 2 * leftRightMargin)/(sizeSelectAdapter.getItemCount() - 1);
        penSizeRv.addItemDecoration(new SpacesItemDecoration(space, leftRightMargin, true));
         */
        penSizeRv.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.sw_5dp), getResources().getDimensionPixelOffset(R.dimen.sw_5dp), true));
        //显示popupWindow
        mPenPopupWindow = new PopupWindow(penToolView, mPenPopupWindowWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPenPopupWindow.setFocusable(false);
        mPenPopupWindow.setOutsideTouchable(false);
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
        View eraseToolView = LayoutInflater.from(mContext).inflate(R.layout.whiteboard_popupwindow_eraser, null, true);
        final int paintToolType = PaintTool.PAINT_TOOL_ERASER;
        //大小选择
        final RecyclerView eraserSizeRv = eraseToolView.findViewById(R.id.rv_eraser_size);
        eraserSizeRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL,false));
        final StrokeWidthSelectAdapter sizeSelectAdapter = new StrokeWidthSelectAdapter();
        final ArrayList<StrokeWidthBean> strokeWidthBeans = new ArrayList<>();
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.eraser_size1), R.drawable.whiteboard_paint_tool_size1));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.eraser_size2), R.drawable.whiteboard_paint_tool_size2));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.eraser_size3), R.drawable.whiteboard_paint_tool_size3));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.eraser_size4), R.drawable.whiteboard_paint_tool_size4));
        strokeWidthBeans.add(new StrokeWidthBean(getResources().getDimensionPixelSize(R.dimen.eraser_size5), R.drawable.whiteboard_paint_tool_size5));

        sizeSelectAdapter.setCurrentStrokeWidth(mSketchView.getPaintToolStrokeWidth(paintToolType));
        sizeSelectAdapter.setPenStrokeWidthBeanList(strokeWidthBeans);
        sizeSelectAdapter.setOnItemClickListener(new StrokeWidthSelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                StrokeWidthBean strokeWidthBean = strokeWidthBeans.get(position);
                sizeSelectAdapter.setCurrentStrokeWidth(strokeWidthBean.strokeWidth);
            }
        });
        eraserSizeRv.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelOffset(R.dimen.sw_5dp), getResources().getDimensionPixelOffset(R.dimen.sw_5dp), true));
        eraserSizeRv.setAdapter(sizeSelectAdapter);

        mEraserPopupWindow = new PopupWindow(eraseToolView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        eraseToolView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mEraserPopupWindowHeight = eraseToolView.getMeasuredHeight();
        mEraserPopupWindow.setFocusable(false);
        mEraserPopupWindow.setOutsideTouchable(false);

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
        builder.setPositiveButton(R.string.whiteboard_sure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSketchView.clear();
            }
        });
        builder.setNegativeButton(R.string.whiteboard_cancel, null);
        mClearAlertDialog = builder.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mClearAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            mClearAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        }
        mClearAlertDialog.setCanceledOnTouchOutside(false);//点击外面区域不会让dialog消失
    }

    /**
     * 点击了菜单之外的处理,主要是隐藏PopupWindow
     *
     */
    public void touchDownOutside() {
        dismissPenPopupWindow();
        dismissEraserPopupWindow();
    }

    /**
     * 切换画笔设置框状态
     */
    private void togglePenPopupWindow() {
        if (mPenPopupWindow != null) {
            if (mPenPopupWindow.isShowing()) {
                dismissPenPopupWindow();
            } else {
                showPenPopupWindow();
            }
        } else {
            showPenPopupWindow();
        }
    }

    /**
     * 显示画笔设置框
     */
    private void showPenPopupWindow() {
        if (mPenPopupWindow == null) {
            initPenPopupWindow();
        }

        int[] penLocation = new int[2];
        mPenLl.getLocationInWindow(penLocation);
        SharpView.ArrowDirection direction;
        if (penLocation[1] < mPenPopupWindowHeight) {
            mPenPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, penLocation[0] ,penLocation[1] + mPenLl.getHeight());
            direction = SharpView.ArrowDirection.TOP;
            //mPenDownTriangleIv.setVisibility(VISIBLE);
        } else {
            mPenPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, penLocation[0] ,penLocation[1] - mPenPopupWindowHeight);
            direction = SharpView.ArrowDirection.BOTTOM;
            //mPenUpTriangleIv.setVisibility(VISIBLE);
        }

        int penLlHalfwidth = mPenLl.getMeasuredWidth()/2;

        SharpDrawable bd = new SharpDrawable(GradientDrawable.Orientation.LEFT_RIGHT, null);
        bd.setSharpSize(20);
        bd.setBgColor(Color.WHITE);
        bd.setArrowDirection(direction);
        bd.setCornerRadius(10);
        bd.setRelativePosition((float)penLlHalfwidth/(float)(mPenPopupWindowWidth));
        if (penLocation[0] + mPenPopupWindowWidth > mMaxRight) {
            Log.i(TAG, "max over");
            int penMarginRight = mMaxRight - (penLocation[0] +penLlHalfwidth);
            bd.setRelativePosition(1 - (float)penMarginRight/(float)(mPenPopupWindowWidth));
        }
        mPenPopupWindow.getContentView().setBackground(bd);


    }

    /**
     * 隐藏画笔设置框
     */
    private void dismissPenPopupWindow() {
        if (mPenPopupWindow != null && mPenPopupWindow.isShowing()) {
            mPenPopupWindow.dismiss();
        }
        //mPenUpTriangleIv.setVisibility(INVISIBLE);
        //mPenDownTriangleIv.setVisibility(INVISIBLE);
    }

    /**
     * 切换橡皮擦设置框状态
     */
    private void toggleEraserPopupWindow() {
        if (mEraserPopupWindow != null) {
            if (mEraserPopupWindow.isShowing()) {
                dismissEraserPopupWindow();
            } else {
                showEraserPopupWindow();
            }
        } else {
            showEraserPopupWindow();
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
        mEraserLl.getLocationInWindow(location);
        if (location[1] < mEraserPopupWindowHeight) {
            mEraserPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] ,location[1] + mEraserLl.getHeight());
            //mEraserDownTriangleIv.setVisibility(VISIBLE);
        } else {
            mEraserPopupWindow.showAtLocation(this, Gravity.NO_GRAVITY, location[0] ,location[1] - mEraserPopupWindowHeight);
           // mEraserUpTriangleIv.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏橡皮擦设置框
     */
    private void dismissEraserPopupWindow() {
        if (mEraserPopupWindow != null && mEraserPopupWindow.isShowing()) {
            mEraserPopupWindow.dismiss();
           // mEraserUpTriangleIv.setVisibility(INVISIBLE);
           // mEraserDownTriangleIv.setVisibility(INVISIBLE);
        }
    }


    /**
     * 隐藏所有PopupWindow
     */
    private void dismissAllPopupWindow() {
        dismissPenPopupWindow();
        dismissEraserPopupWindow();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i(TAG, "menu onConfigurationChanged");
        /*
        if (mClearAlertDialog.isShowing()) {
            mClearAlertDialog.dismiss();
        }
        dismissAllPopupWindow();
        mCurrentRect.setEmpty();
         */
    }


    public void setMenuStatus(int status) {
        mLastMenuStatus = mMenuStatus;
        mMenuStatus = status;
        if (status == MENU_STATUS_INIT) {
            mPenIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_pen_unpressed));
            mExitSketchIv.setVisibility(GONE);
            mExpandIv.setVisibility(GONE);
            mExpandCl.setVisibility(GONE);
        } else if (status == MENU_STATUS_EXPANDED) {
            mExpandCl.setVisibility(VISIBLE);
            mExpandIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_unexpand));
        } else if (status == MENU_STATUS_UNEXPANDED) {
            mExitSketchIv.setVisibility(VISIBLE);
            mExpandCl.setVisibility(GONE);
            mExpandIv.setVisibility(VISIBLE);
            mExpandIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_expand));
        }

        if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_PEN) {
            mPenIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_pen_pressed));
            mEraserIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_eraser_unpressed));
        } else if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_ERASER) {
            mEraserIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_eraser_pressed));
            mPenIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_pen_unpressed));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.iv_pen || id == R.id.iv_eraser) { //画笔，橡皮擦的popupwindow隐藏显示不一致
            if (id == R.id.iv_pen) {
                dismissEraserPopupWindow();
                if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_PEN) {
                    togglePenPopupWindow();
                    return;
                }
                if (mMenuStatus == MENU_STATUS_INIT) {
                    setMenuStatus(MENU_STATUS_UNEXPANDED);
                }
                mPenIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_pen_pressed));
                mEraserIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_eraser_unpressed));
                mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_PEN);
            } else if (id == R.id.iv_eraser) {
                dismissPenPopupWindow();
                if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_ERASER) {
                    toggleEraserPopupWindow();
                }
                mEraserIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_eraser_pressed));
                mPenIv.setBackground(getResources().getDrawable(R.drawable.whiteboard_ic_pen_unpressed));
                mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_ERASER);
            }
        } else {
            dismissAllPopupWindow();
            if (id == R.id.iv_exit_sketch) {
                mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_NONE);
                setMenuStatus(MENU_STATUS_INIT);
                Process.killProcess(Process.myPid());
            } else if (id == R.id.iv_clear_sketch) {
                mClearAlertDialog.show();
            } else if (id == R.id.iv_save_sketch) {
                Bitmap bitmap = mSketchView.getBitmap();
                //FileUtil.saveBitmap("sdcard/sket.png", bitmap);
                Toast.makeText(mContext, R.string.whiteboard_save_sketch_success, Toast.LENGTH_LONG).show();
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

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    private int mLargeMoveCount = 0;
    public boolean updateView(MotionEvent event) {
        super.onTouchEvent(event);
        updateLimit();
        if (this.isEnabled()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mIsDrag = false;
                    mLargeMoveCount = 0;
                    mDownX = event.getX();
                    mDownY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE: // 滑动动作处理 记录离开屏幕时的 moveX  moveY 用于计算距离 和 判断滑动事件和点击事件 并作出响应
                    final float moveX = event.getX() - mDownX;
                    final float moveY = event.getY() - mDownY;
                    int l,r,t,b; // 上下左右四点移动后的偏移量
                    //计算偏移量 设置偏移量 = 3 时 为判断点击事件和滑动事件的峰值
                    // 偏移量的绝对值大于 3 为 滑动时间 并根据偏移量计算四点移动后的位置
                    //参考：https://blog.csdn.net/yuhui77268769/article/details/103771252
                    if (Math.abs(moveX) > 5 || Math.abs(moveY) > 5) {
                        Log.i(TAG, "Math.abs large!!!!!!");
                        mLargeMoveCount++;
                    }
                    //优化：偏移量大的点大于1个才判断为滑动事件，不然太灵敏了。也不能设置太大，否则感觉第一下拖不动
                    if (mLargeMoveCount > 2) {
                        mIsDrag = true;
                    }

                    if (mIsDrag) {
                        dismissAllPopupWindow();
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
                    }
                    break;
                case MotionEvent.ACTION_UP: // 不处理
                    Log.i(TAG, "MotionEvent.ACTION_UP");
                    break;
                case MotionEvent.ACTION_CANCEL: // 不处理
                    Log.i(TAG, "MotionEvent.ACTION_CANCEL");
                    break;
            }
            return true;
        }
        return false;
    }

}
