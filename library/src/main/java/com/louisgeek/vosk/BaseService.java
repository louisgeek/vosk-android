package com.louisgeek.vosk;

import android.app.Service;
import android.content.Intent;

import com.louisgeek.vosk.event.BaseEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by louisgeek on 2018/11/6.
 */
public abstract class BaseService extends Service {
//    private Gson mGson = new Gson();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribe(BaseEvent event) {

    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
