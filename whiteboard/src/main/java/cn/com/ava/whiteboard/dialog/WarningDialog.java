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

class WarningDialog extends BaseDialog {

    public WarningDialog(@NonNull Context context) {
        super(context);
    }

    public WarningDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected WarningDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
