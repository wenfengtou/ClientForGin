package com.wenfengtou.whiteboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wenfengtou.whiteboard.view.SketchView;

public class WhiteBoardActivity extends AppCompatActivity {

    Button mCancelWriteBt;
    SketchView mSketchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_board);
        mCancelWriteBt = findViewById(R.id.bt_cancel_write);
        mSketchView = findViewById(R.id.board_view);
        mCancelWriteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSketchView.revoke();
            }
        });
    }
}
