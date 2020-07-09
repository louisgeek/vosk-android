package com.louisgeek.vosk;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.louisgeek.vosk.event.VoskStartEvent;
import com.louisgeek.vosk.event.VoskStopEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.kaldi.Assets;
import org.kaldi.Model;
import org.kaldi.RecognitionListener;
import org.kaldi.SpeechRecognizer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by louisgeek on 2020/4/17.
 */
public class VoskService extends BaseService {
    private static final String TAG = "VoskService";
    private Gson mGson = new Gson();

    static {
        System.loadLibrary("kaldi_jni");
    }

    private Context mContext;
    private Model model;
    private SpeechRecognizer recognizer;
    private ExecutorService mDiskIOThreadExecutor = Executors.newSingleThreadExecutor();
    private Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        init();
        return super.onStartCommand(intent, flags, startId);

    }

    private void init() {
        mDiskIOThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Assets assets = new Assets(mContext);
                    File assetDir = assets.syncAssets();
                    Log.d("!!!!", assetDir.toString());
                    model = new Model(assetDir.toString() + "/model-android");
                    //
                    mMainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            //
                            recognizeMicrophone();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribe(VoskStartEvent event) {
        //
        Log.e(TAG, "onSubscribe: 开始识别 ");
        recognizeMicrophone();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribe(VoskStopEvent event) {
        //
        Log.e(TAG, "onSubscribe: 停止识别 ");
        cancel();
    }

    public void recognizeMicrophone() {
        cancel();
        //
        try {
            recognizer = new SpeechRecognizer(model);
            recognizer.addListener(new RecognitionListener() {
                @Override
                public void onPartialResult(String hypothesis) {
//                        Log.e(TAG, "onPartialResult: " + hypothesis);
                    if (TextUtils.isEmpty(hypothesis)) {
                        return;
                    }
//                    Log.e(TAG, "onPartialResult: raw " + hypothesis);
                    VoskPartial voskPartial = mGson.fromJson(hypothesis, VoskPartial.class);
                    if (TextUtils.isEmpty(voskPartial.getPartial())) {
                        return;
                    }
                    EventBus.getDefault().post(voskPartial);
                }

                @Override
                public void onResult(String hypothesis) {
//                        Log.e(TAG, "onResult: " + hypothesis);
                    if (TextUtils.isEmpty(hypothesis)) {
                        return;
                    }
//                    Log.e(TAG, "onResult: " + s);
                    VoskResult voskResult = mGson.fromJson(hypothesis, VoskResult.class);
                    if (TextUtils.isEmpty(voskResult.getText())) {
                        return;
                    }
                    EventBus.getDefault().post(voskResult);
                }

                @Override
                public void onError(Exception e) {
                    Log.e(TAG, "recognizeMicrophone: " + e.getMessage());
                }

                @Override
                public void onTimeout() {
                    recognizer.cancel();
                    recognizer = null;
                }
            });
            recognizer.startListening();
        } catch (IOException e) {
            Log.e(TAG, "recognizeMicrophone: " + e.getMessage());
        }
    }

    private void cancel() {
        if (recognizer != null) {
            recognizer.cancel();
        }
    }

    private void release() {
        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
            recognizer = null;
        }
    }

    @Override
    public void onDestroy() {
        release();
        super.onDestroy();
    }

    // 服务启动
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, VoskService.class);
        context.startService(intent);
    }

    // 服务停止
    public static void actionStop(Context context) {
        Intent intent = new Intent(context, VoskService.class);
        context.stopService(intent);
    }

}
