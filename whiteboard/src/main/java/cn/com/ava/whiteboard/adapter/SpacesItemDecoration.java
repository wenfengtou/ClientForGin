package cn.com.ava.whiteboard.adapter;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace; //item间隔
    private int mLeftRightMargin; //左右两边的margin

    public SpacesItemDecoration(int space, int leftRightMargin) {
        this.mSpace = space;
        this.mLeftRightMargin = leftRightMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.left = mLeftRightMargin;
        } else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() -1 ) {
            outRect.left = mSpace;
            outRect.right = mLeftRightMargin;
        } else {
            outRect.left = mSpace;
        }

    }
}