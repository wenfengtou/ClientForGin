package cn.com.ava.whiteboard.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import cn.com.ava.whiteboard.R;

public class CheckBoxDialog extends BaseDialog {

    private static final String TAG = "CheckBoxDialog";

    private CheckBox mNoAskAgainCheckBox;
    private NoAskAgainCheckChangeListener mNoAskAgainCheckChangeListener;

    public interface NoAskAgainCheckChangeListener {
        void onCheckChange(boolean isChecked);
    }

    public CheckBoxDialog(@NonNull Context context) {
        super(context);
    }

    public CheckBoxDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CheckBoxDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public void setNoAskAgainCheckChangeListener(NoAskAgainCheckChangeListener noAskAgainCheckChangeListener) {
        mNoAskAgainCheckChangeListener = noAskAgainCheckChangeListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mNoAskAgainCheckBox = findViewById(R.id.no_ask_again_checkBox);
        mNoAskAgainCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mNoAskAgainCheckChangeListener != null) {
                    mNoAskAgainCheckChangeListener.onCheckChange(isChecked);
                }
            }
        });
    }

}
