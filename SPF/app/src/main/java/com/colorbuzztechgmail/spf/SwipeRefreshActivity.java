package com.colorbuzztechgmail.spf;

/**
 * Created by PC01 on 09/04/2018.
 */
/*
 * Copyright (C) 2014 Antonio Leiva Gordillo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;


public abstract class SwipeRefreshActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_swipe_refresh);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
    }

    @Override
    public void setContentView(int layoutResID) {
        View v = getLayoutInflater().inflate(layoutResID, refreshLayout, false);
        setContentView(v);
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, view.getLayoutParams());
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        refreshLayout.addView(view, params);
        initSwipeOptions();
    }

    private void initSwipeOptions() {
        refreshLayout.setOnRefreshListener(this);
        setAppearance();
        disableSwipe();
    }

    private void setAppearance() {
        refreshLayout.setColorScheme(getResources() .getColor(android.R.color.holo_blue_bright),
                getResources() .getColor(android.R.color.holo_green_light),
                getResources() .getColor(android.R.color.holo_orange_light),
                getResources() .getColor(android.R.color.holo_red_light));
    }

    /**
     * It shows the SwipeRefreshLayout progress
     */
    public void showSwipeProgress() {
        refreshLayout.setRefreshing(true);
    }

    /**
     * It shows the SwipeRefreshLayout progress
     */
    public void hideSwipeProgress() {
        refreshLayout.setRefreshing(false);
    }

    /**
     * Enables swipe gesture
     */
    public void enableSwipe() {
        refreshLayout.setEnabled(true);
    }

    /**
     * Disables swipe gesture. It prevents manual gestures but keeps the option tu show
     * refreshing programatically.
     */
    public void disableSwipe() {
        refreshLayout.setEnabled(false);
    }

    /**
     * It must be overriden by parent classes if manual swipe is enabled.
     */
    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                refreshLayout.setRefreshing(false);

            }

        }, 5000);
        // Empty implementation
    }
}