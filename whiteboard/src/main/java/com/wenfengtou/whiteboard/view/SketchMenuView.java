package com.wenfengtou.whiteboard.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.wenfengtou.commonutil.FileUtil;
import com.wenfengtou.whiteboard.R;
import com.wenfengtou.whiteboard.painttool.PaintTool;

public class SketchMenuView extends LinearLayout implements View.OnClickListener {

    private LinearLayout mPenll;
    private TextView mPenTv;
    private LinearLayout mEraserll;
    private TextView mEraserTv;
    private LinearLayout mClearSketchll;
    private TextView mClearSketchTv;
    private LinearLayout mSaveSketchll;
    private TextView mSaveSketchTv;
    private NormalSketchView mSketchView;
    private PopupWindow mColorPopUpWindow;
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

    public void setSketchView(NormalSketchView sketchView) {
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

    private void showPenPopupWindow() {
        TextView textView = new TextView(mContext);
        textView.setText("颜色选择");
        textView.setBackgroundColor(Color.GREEN);
        mColorPopUpWindow = new PopupWindow(textView, 100, 100);
        //mColorPopUpWindow.setOutsideTouchable(true);//设置点击外部区域可以取消popupWindow
        mColorPopUpWindow.setFocusable(true);
        int[] location = new int[2];
        mPenll.getLocationOnScreen(location);
        mColorPopUpWindow.showAtLocation(mPenll, Gravity.NO_GRAVITY, location[0], location[1]-mColorPopUpWindow.getHeight());
        mColorPopUpWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

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
