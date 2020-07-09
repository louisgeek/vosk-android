// Copyright 2019 Alpha Cephei Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.louisgeek.voskdemo.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.louisgeek.vosk.VoskService;
import com.louisgeek.vosk.VoskPartial;
import com.louisgeek.vosk.VoskResult;
import com.louisgeek.vosk.event.VoskStopEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    ListView id_lv;
    MyBaseAdapter myBaseAdapter;
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_main);

        VoskService.actionStart(this);
        EventBus.getDefault().post(new VoskStopEvent());
        /*findViewById(R.id.recognize_mic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                EventBus.getDefault().post(new VoskStartEvent());
            }
        });*/


         myBaseAdapter = new MyBaseAdapter();
        id_lv.setAdapter(myBaseAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        VoskService.actionStop(this);
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribe(VoskResult voskResult) {
        Log.e(TAG, "onSubscribe: VoskResult " + voskResult.getText());
        MessageModel messageModel = new MessageModel();
        messageModel.title = voskResult.getText();
        myBaseAdapter.addData(messageModel);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribe(VoskPartial voskPartial) {
        Log.e(TAG, "onSubscribe: VoskPartial " + voskPartial.getPartial());
    }
}
