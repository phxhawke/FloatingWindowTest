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

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment{
        private int params = 0;
        private Intent intent;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            intent = new Intent(getActivity().getApplicationContext(), FloatingWindowService.class);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int mask = 0;

                    switch (v.getId()) {
                        case R.id.button_start:
                            intent.setAction(FloatingWindowService.START);
                            getActivity().startService(intent);
                            break;
                        case R.id.button_stop:
                            intent.setAction(FloatingWindowService.STOP);
                            getActivity().startService(intent);
                            break;
                        case R.id.button_top:
                            mask = ~Gravity.BOTTOM;
                            params |= Gravity.TOP;
                            params &= mask;
                            break;
                        case R.id.button_left:
                            mask = ~Gravity.RIGHT;
                            params |= Gravity.LEFT;
                            params &= mask;
                            break;
                        case R.id.button_center:
                            if ((mask & Gravity.CENTER)==Gravity.CENTER){
                                mask = ~(Gravity.CENTER);
                                params &= mask;
                            } else {
                                mask = ~(Gravity.TOP|Gravity.BOTTOM);
                                params &= mask;
                                params |= Gravity.CENTER;
                            }
                            break;
                        case R.id.button_right:
                            mask = ~Gravity.LEFT;
                            params |= Gravity.RIGHT;
                            params &= mask;
                            break;
                        case R.id.button_bottom:
                            mask = ~Gravity.TOP;
                            params |= Gravity.BOTTOM;
                            params &= mask;
                            break;
                        case R.id.button_update:
                            intent.setAction(FloatingWindowService.UPDATE);
                            intent.putExtra(FloatingWindowService.UPDATE_EXTRA, params);
                            getActivity().startService(intent);
                            break;
                    }
                }
            };

            rootView.findViewById(R.id.button_start).setOnClickListener(onClickListener);
            rootView.findViewById(R.id.button_stop).setOnClickListener(onClickListener);
            rootView.findViewById(R.id.button_top).setOnClickListener(onClickListener);
            rootView.findViewById(R.id.button_left).setOnClickListener(onClickListener);
            rootView.findViewById(R.id.button_center).setOnClickListener(onClickListener);
            rootView.findViewById(R.id.button_right).setOnClickListener(onClickListener);
            rootView.findViewById(R.id.button_bottom).setOnClickListener(onClickListener);
            rootView.findViewById(R.id.button_update).setOnClickListener(onClickListener);

            return rootView;
        }



    }
}
