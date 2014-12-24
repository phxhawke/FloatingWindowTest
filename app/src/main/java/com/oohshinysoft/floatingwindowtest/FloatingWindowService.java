/*
 * Copyright (c) 2014 Carlos F. Urrutia.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.oohshinysoft.floatingwindowtest;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

public class FloatingWindowService extends Service {
    private WindowManager windowManager;
    private WindowManager.LayoutParams params;
    private View floatingView;
    private static final String packageName = FloatingWindowService.class.getPackage().getName();
    private static final String TAG = FloatingWindowService.class.getSimpleName();
    public static final String START = packageName + "start";
    public static final String UPDATE = packageName + "update";
    public static final String UPDATE_EXTRA = "gravity";
    public static final String STOP = packageName + "stop";

    public FloatingWindowService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        /*
            Not really needed for the example, but a good habit to get into if we start inflating
            multiple layouts. Though if that happens it may be necessary to move it outside of the method.
         */
        LayoutInflater layoutInflater;

        super.onCreate();

        // Need to get the window manager in order to place our window.
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        /*
            This is where the magic is. In params we set the layout parameters that allow placement
            of the floating window. WindowManager.LayoutParams.TYPE_PHONE allows us to place it
            above all applications but behind the status bar.
         */
        params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        // Our default gravity.
        params.gravity = Gravity.LEFT | Gravity.TOP;

        floatingView = layoutInflater.inflate(R.layout.floating_window, null);

        windowManager.addView(floatingView, params);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String command = intent != null ? intent.getAction() : null;

        if (STOP.equals(command)) {
            stopSelf();
        } else if (UPDATE.equals(command)) {
            params.gravity = intent.getIntExtra(UPDATE_EXTRA, params.gravity);
            windowManager.updateViewLayout(floatingView, params);
        } else if (START.equals(command)) {
            Log.i(TAG, "Started " + TAG);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (floatingView != null)
            windowManager.removeView(floatingView);
    }
}
