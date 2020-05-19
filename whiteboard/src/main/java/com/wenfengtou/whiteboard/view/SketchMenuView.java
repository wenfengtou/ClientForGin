package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
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
    private LinearLayout mPenll;
    private TextView mPenTv;
    private LinearLayout mEraserll;
    private TextView mEraserTv;
    private LinearLayout mClearSketchll;
    private TextView mClearSketchTv;
    private LinearLayout mSaveSketchll;
    private TextView mSaveSketchTv;
    private SketchView mSketchView;
    private Context mContext;

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
        LayoutInflater.from(context).inflate(R.layout.layout_sketch_menu, this, true);
        mPenll = findViewById(R.id.ll_pen);
        mPenTv = findViewById(R.id.tv_pen);
        mEraserll = findViewById(R.id.ll_eraser);
        mEraserTv = findViewById(R.id.tv_eraser);
        mClearSketchll = findViewById(R.id.ll_clear_sketch);
        mSaveSketchll = findViewById(R.id.ll_save_sketch);
        mPenll.setOnClickListener(this);
        mEraserll.setOnClickListener(this);
        mClearSketchll.setOnClickListener(this);
        mSaveSketchll.setOnClickListener(this);
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
        mPenll.getLocationOnScreen(location);
        colorPopUpWindow.showAtLocation(mPenll, Gravity.NO_GRAVITY, location[0], location[1]-colorPopUpWindow.getHeight());
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
        mPenll.getLocationOnScreen(location);
        erasePopUpWindow.showAtLocation(mEraserll, Gravity.NO_GRAVITY, location[0], location[1] - erasePopUpWindow.getHeight());
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
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_pen) {
            if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_PEN) {
                showPenPopupWindow();
            }
            mPenTv.setTextColor(Color.BLUE);
            mEraserTv.setTextColor(Color.GRAY);
            mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_PEN);
        } else if (id == R.id.ll_eraser) {
            if (mSketchView.getPaintToolType() == PaintTool.PAINT_TOOL_ERASER) {
                showEraserPopupWindow();
            }
            mEraserTv.setTextColor(Color.BLUE);
            mPenTv.setTextColor(Color.GRAY);
            mSketchView.choosePaintTool(PaintTool.PAINT_TOOL_ERASER);
        } else if (id == R.id.ll_clear_sketch) {
            mSketchView.clear();
        } else if (id == R.id.ll_save_sketch) {
            Bitmap bitmap = mSketchView.getBitmap();
            FileUtil.saveBitmap("sdcard/sket.png", bitmap);
            Toast.makeText(mContext, R.string.save_sketch_success, Toast.LENGTH_LONG).show();
        }

    }
}
