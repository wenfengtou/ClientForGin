package com.wenfengtou.whiteboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

import com.wenfengtou.whiteboard.view.SketchMenuView;
import com.wenfengtou.whiteboard.view.SketchView;

public class MovableWhiteBoardActivity extends AppCompatActivity {

    private SketchView mSketchView;
    private SketchMenuView mSketchMenuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movable_white_board);
        //mSketchView = findViewById(R.id.sketch);
        //mSketchMenuView = findViewById(R.id.sketch_menu);
        //mSketchMenuView.setSketchView(mSketchView);
    }
}
