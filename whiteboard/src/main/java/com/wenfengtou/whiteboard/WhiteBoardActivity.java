package com.wenfengtou.whiteboard;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import com.wenfengtou.commonutil.FileUtil;
import com.wenfengtou.whiteboard.view.NormalSketchView;
import com.wenfengtou.whiteboard.view.SketchView;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

@RequiresApi(api = Build.VERSION_CODES.M)
public class WhiteBoardActivity extends AppCompatActivity {

    private static final String TAG = "WhiteBoardActivity";
    private String mSavePath;
    private String mMp4Path;
    private Button mCancelWriteBt;
    private Button mResumeWriteBt;
    private NormalSketchView mSketchView;
    private MediaCodec mMediaCodec;
    private Surface mSurface;
    private MediaFormat mMediaFormat = MediaFormat.createVideoFormat("video/avc", 1080, 1920);

    private int mVideoTrackIndex = -1;

    private MediaMuxer mMediaMuxer;

    long startEncodeTime = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white_board);
        mCancelWriteBt = findViewById(R.id.bt_cancel_write);
        mResumeWriteBt = findViewById(R.id.bt_resume_write);
        mSketchView = findViewById(R.id.board_view);
        mSavePath = getExternalCacheDir() + File.separator + "record.h264";
        mMp4Path = getExternalCacheDir() + File.separator + "record.mp4";
        mSurface = MediaCodec.createPersistentInputSurface();
        createMediaCodec(mSurface);
        mSketchView.setSurface(mSurface);

        if (new File(mSavePath).exists()) {
            new File(mSavePath).delete();
        }
        if (new File(mMp4Path).exists()) {
            new File(mMp4Path).delete();
        }

        try {
            mMediaMuxer = new MediaMuxer(mMp4Path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            //mMediaMuxer.
        } catch (IOException e) {
            e.printStackTrace();
        }

        mSketchView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // TODO Auto-generated method stub
                //mSketchView.initSketch(); //清空画布


            }
        });
        startEncode();
        mSketchView.startDecoreThread();

        /*
        mSketchView.setSurface(mSurface);
        mSketchView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                mSketchView.initSketch(); //清空画布
                if (new File(mSavePath).exists()) {
                    new File(mSavePath).delete();
                }
                startEncode();
                mSketchView.startDecoreThread();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

            }
        });

         */

        mCancelWriteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSketchView.revoke();
            }
        });
        mResumeWriteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mSketchView.resume();
            }
        });
    }

    private void createMediaCodec(Surface surface) {
        try {
            mMediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
            mMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 1024*1024);
            mMediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2);
            mMediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 35);
            mMediaCodec = MediaCodec.createEncoderByType("video/avc");
            mMediaCodec.configure(mMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mMediaCodec.setInputSurface(surface);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startEncode() {
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
                    int flag = buffer[4] & 0x1F;
                    outputBuffer.get(buffer);
                    FileUtil.writeH264(buffer, mSavePath);
                    Log.i(TAG, "onOutputBufferAvailable = remain=" + buffer.length + " flag=" + flag);
                }
                if (startEncodeTime < 0) {
                    startEncodeTime = SystemClock.uptimeMillis();
                }
                mMediaMuxer.writeSampleData(mVideoTrackIndex, outputBuffer, bufferInfo);
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaMuxer.stop();
        mMediaMuxer.release();
        Log.i(TAG, "encodetime = " + (SystemClock.uptimeMillis() - startEncodeTime));
    }
}
