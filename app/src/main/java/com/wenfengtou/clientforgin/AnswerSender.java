package com.wenfengtou.clientforgin;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.File;

public class AnswerSender {

    private SendHandler mSendHandler;
    public static class SendHandler extends Handler {
        private AnswerSender mAnswerSender;
        public SendHandler(AnswerSender answerSender) {
            mAnswerSender = answerSender;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (true) {
                        mAnswerSender.postfile(new File(""));
                    } else {
                        sendEmptyMessage(2);
                    }
                case 2:
                    mAnswerSender.postAnswer(new File(""));
                    break;
            }
        }
    }


    private void start() {

    }

    private void postfile(File file) {
       new AsyncTask<File, Integer, Integer>() {
           @Override
           protected Integer doInBackground(File... files) {
               return null;
           }

           @Override
           protected void onPostExecute(Integer integer) {
               super.onPostExecute(integer);
           }
       }.execute();
    }

    private void postAnswer(File file) {

    }

    public void close() {
        mSendHandler.removeCallbacksAndMessages(null);
    }

}
