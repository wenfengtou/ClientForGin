package cn.com.ava.whiteboard.adapter;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import cn.com.ava.whiteboard.R;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace; //item间隔
    private int mLeftRightMargin; //左右两边的margin
    private boolean mNeedLine; //是否需要中间绘制直线

    public SpacesItemDecoration(int space, int leftRightMargin, boolean needLine) {
        this.mSpace = space;
        this.mLeftRightMargin = leftRightMargin;
        this.mNeedLine = needLine;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = mLeftRightMargin;
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() -1) {
            outRect.left = mSpace;
            outRect.right = mLeftRightMargin;
        } else {
            outRect.left = mSpace;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mNeedLine) {
            View firstChildren = parent.getLayoutManager().getChildAt(0);
            View lastChildren = parent.getLayoutManager().getChildAt(parent.getAdapter().getItemCount() -1);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(parent.getContext().getResources().getColor(R.color.whiteboard_size_line_color));
            paint.setStrokeWidth(parent.getContext().getResources().getDimension(R.dimen.whiteboard_1dp));
            float startX = firstChildren.getX() + firstChildren.getMeasuredWidth()/2;
            float endX = lastChildren.getX() + lastChildren.getMeasuredWidth()/2;
            float Y = firstChildren.getY() + firstChildren.getMeasuredHeight()/2;
            c.drawLine(startX,Y, endX, Y, paint);
        }
    }
}