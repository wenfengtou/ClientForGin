package cn.com.ava.whiteboard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.com.ava.whiteboard.R;

public class BaseDialog extends Dialog {

    private TextView mSureTextView;
    private TextView mCancelTextView;
    private OnSureClickListener mOnSureClickListener;
    private OnCancelClickListener mOnCancelClickListener;
    private int mLayoutId;

    public interface OnSureClickListener {
        void onClick();
    }

    public interface OnCancelClickListener {
        void onClick();
    }

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setLayoutId(int layoutId) {
        mLayoutId = layoutId;
    }

    public void setOnSureClickListener(OnSureClickListener onSureClickListener) {
        mOnSureClickListener = onSureClickListener;
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        mOnCancelClickListener = onCancelClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setCancelable(false);
        setContentView(mLayoutId);
        mSureTextView = findViewById(R.id.sure_tv);
        if (mSureTextView != null) {
            mSureTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnSureClickListener != null) {
                        mOnSureClickListener.onClick();
                    }
                }
            });
        }
        mCancelTextView = findViewById(R.id.cancel_tv);
        if (mCancelTextView != null) {
            mCancelTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnCancelClickListener != null) {
                        mOnCancelClickListener.onClick();
                    }
                }
            });
        }
    }
}
