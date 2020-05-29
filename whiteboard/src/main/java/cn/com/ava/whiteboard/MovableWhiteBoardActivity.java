package cn.com.ava.whiteboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import cn.com.ava.whiteboard.view.SketchMenuView;
import cn.com.ava.whiteboard.view.SketchView;

public class MovableWhiteBoardActivity extends AppCompatActivity {

    private SketchView mSketchView;
    private SketchMenuView mSketchMenuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.whiteboard_activity_movable_white_board);
        //mSketchView = findViewById(R.id.sketch);
        //mSketchMenuView = findViewById(R.id.sketch_menu);
        //mSketchMenuView.setSketchView(mSketchView);
    }
}
