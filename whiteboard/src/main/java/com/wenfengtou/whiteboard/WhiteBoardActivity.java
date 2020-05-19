package com.wenfengtou.whiteboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.wenfengtou.commonutil.FileUtil;
import com.wenfengtou.whiteboard.view.SketchView;
import com.wenfengtou.whiteboard.view.SketchMenuView;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

@RequiresApi(api = Build.VERSION_CODES.M)
public class WhiteBoardActivity extends AppCompatActivity {

    private static final String TAG = "WhiteBoardActivity";
    private ImageButton mUndoBt;
    private ImageButton mRedoBt;

    private SketchMenuView mSketchMenuView;
    private SketchView mSketchView;
    private MediaCodec mMediaCodec;
    private MediaFormat mMediaFormat;

    private int mVideoTrackIndex = -1;

    private MediaMuxer mMediaMuxer;

    long startEncodeTime = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_board);
        Intent intent = getIntent();
        mUndoBt = findViewById(R.id.bt_undo_write);
        mRedoBt = findViewById(R.id.bt_redo_write);
        mSketchView = findViewById(R.id.board_view);
        mSketchMenuView = findViewById(R.id.sketch_menu);
        mSketchMenuView.setSketchView(mSketchView);
        if (intent != null) {
            String bgPath = intent.getStringExtra("bg-path");
            if (bgPath != null && new File(bgPath).exists()) {
                Bitmap bgBitmap = BitmapFactory.decodeFile(bgPath);
                mSketchView.setBackgroupBitmap(bgBitmap);
            }
        }


        mSketchView.post(new Runnable() {
            @Override
            public void run() {
                int width = mSketchView.getWidth();
                int height = mSketchView.getHeight();
                startEncode(width, height);
            }
        });


        mUndoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSketchView.undo();
            }
        });
        mRedoBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSketchView.redo();
            }
        });

    }

    private void createMediaCodec(Surface surface, int width, int height) {
        mMediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
        try {
            mMediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            mMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1024*1024);
            mMediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            mMediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
            mMediaCodec = MediaCodec.createEncoderByType("video/avc");
            mMediaCodec.configure(mMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.setInputSurface(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startEncode(int width, int height) {
        final String h264Path = Environment.getExternalStorageDirectory() + File.separator + "record.h264";
        String mp4Path = Environment.getExternalStorageDirectory() + File.separator + "record.mp4";
        Surface surface = MediaCodec.createPersistentInputSurface();
        createMediaCodec(surface, width, height);
        mSketchView.setSurface(surface);

        if (new File(h264Path).exists()) {
            new File(h264Path).delete();
        }
        if (new File(mp4Path).exists()) {
            new File(mp4Path).delete();
        }

        try {
            mMediaMuxer = new MediaMuxer(mp4Path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        mMediaCodec.setCallback(new MediaCodec.Callback() {
            @Override
            public void onInputBufferAvailable(@NonNull MediaCodec mediaCodec, int i) {

            }

            @Override
            public void onOutputBufferAvailable(@NonNull MediaCodec mediaCodec, int i, @NonNull MediaCodec.BufferInfo bufferInfo) {
                ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(i);
                //这里将编码后的流存入byte[]队列，也可以在这里将画面输出到文件或者发送到远端
                if (outputBuffer != null && bufferInfo.size > 0) {
                    byte[] buffer = new byte[outputBuffer.remaining()];
                    outputBuffer.get(buffer);
                    int flag = buffer[4] & 0x1F;
                    FileUtil.writeH264(buffer, h264Path);
                    //Log.i(TAG, "onOutputBufferAvailable = remain=" + buffer.length + " flag=" + flag);
                }

                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                    // The codec config data was pulled out and fed to the muxer when we got
                    // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
                    Log.d(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
                    bufferInfo.size = 0;
                }

                if (outputBuffer != null && bufferInfo.size > 0) {
                    if (startEncodeTime < 0) {
                        startEncodeTime = SystemClock.uptimeMillis();
                    }
                    mMediaMuxer.writeSampleData(mVideoTrackIndex, outputBuffer, bufferInfo);
                }

                mediaCodec.releaseOutputBuffer(i, false);
                if ((bufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    mediaCodec.release();
                }
            }

            @Override
            public void onError(@NonNull MediaCodec mediaCodec, @NonNull MediaCodec.CodecException e) {

            }


            @Override
            public void onOutputFormatChanged(@NonNull MediaCodec mediaCodec, @NonNull MediaFormat mediaFormat) {
                Log.i(TAG, "onOutputFormatChanged " + mediaFormat);
                mVideoTrackIndex = mMediaMuxer.addTrack(mediaFormat);
                mMediaMuxer.start();
            }
        });
        mMediaCodec.start();
        mSketchView.startDecoreThread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaCodec.stop();
        mMediaCodec.release();
        mMediaMuxer.stop();
        mMediaMuxer.release();
        Log.i(TAG, "encodetime = " + (SystemClock.uptimeMillis() - startEncodeTime));
    }
}
