/*
 * Copyright (c) 2018. Evren Coşkun
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.evrencoskun.tableview.adapter.recyclerview.views;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.evrencoskun.tableview.ITableView;
import com.evrencoskun.tableview.R;
import com.evrencoskun.tableview.layoutmanager.CellLayoutManager;
import com.evrencoskun.tableview.listener.scroll.HorizontalRecyclerViewListener;
import com.evrencoskun.tableview.listener.scroll.VerticalRecyclerViewListener;
import com.sachinreddy.recyclerview.RecyclerView;

/**
 * Created by evrencoskun on 19/06/2017.
 */

public class TimelineRecyclerView extends RecyclerView {
    private static final String LOG_TAG = TimelineRecyclerView.class.getSimpleName();

    private int mScrolledX = 0;
    private int mScrolledY = 0;

    private boolean mIsHorizontalScrollListenerRemoved = true;
    private boolean mIsVerticalScrollListenerRemoved = true;

    private ITableView mTableView;

    public MutableLiveData<Float> xProgress = new MutableLiveData<>();
    public MutableLiveData<Integer> mTime = new MutableLiveData<>();
    public MutableLiveData<Integer> mMaxTime = new MutableLiveData<>();
    public MutableLiveData<Boolean> mIsScrolling = new MutableLiveData<>();
    public MutableLiveData<Boolean> mFabEnabled = new MutableLiveData<>();

    public MutableLiveData<Boolean> metronomeOn = new MutableLiveData<>();
    public MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();

    public MediaPlayer metronomePlayer = MediaPlayer.create(getContext(), R.raw.metronome);

    private Handler handler = new Handler();
    private Runnable runner = new Runnable() {
        @Override
        public void run() {
            mIsScrolling.postValue(false);
        }
    };

    public TimelineRecyclerView(@NonNull Context context, @NonNull ITableView tableView) {
        super(context);

        mTableView = tableView;
        mIsScrolling.postValue(false);
        mFabEnabled.postValue(true);

        metronomeOn.postValue(true);
        isPlaying.postValue(false);

        // These are necessary.
        this.setHasFixedSize(false);
        this.setNestedScrollingEnabled(false);
        // These are for better scrolling process.
        this.setItemViewCacheSize(context.getResources().getInteger(R.integer
                .default_item_cache_size));
        this.setDrawingCacheEnabled(true);
        this.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        mScrolledX += dx;
        mScrolledY += dy;

        CellRecyclerView mColumnHeaderRecyclerView = mTableView.getColumnHeaderRecyclerView();
        CellLayoutManager mCellLayoutManager = mTableView.getCellLayoutManager();

        Integer xTopRecyclerView = computeHorizontalScrollOffset();
        Integer mTopWidth = computeHorizontalScrollRange() - computeHorizontalScrollExtent();
        Integer xBottomRecyclerView = mColumnHeaderRecyclerView.computeHorizontalScrollOffset();
        Integer mBottomWidth = mColumnHeaderRecyclerView.computeHorizontalScrollRange() - mColumnHeaderRecyclerView.computeHorizontalScrollExtent();
        Integer xThreshold = (mTopWidth - mBottomWidth) / 2;

        Float progressValue = (float) (xTopRecyclerView - xBottomRecyclerView);
        Float progressMax = (float) (mTopWidth - mBottomWidth);
        mTime.postValue(xTopRecyclerView);
        mMaxTime.postValue(mTopWidth);

        if (xTopRecyclerView >= xThreshold && xTopRecyclerView <= mTopWidth - xThreshold) {
            xProgress.postValue(0.5f);
            mColumnHeaderRecyclerView.scrollBy(dx, 0);
            for (int i = 0; i < mCellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) mCellLayoutManager.getChildAt(i);
                // Scroll horizontally
                child.scrollBy(dx, 0);
            }
        } else if (progressValue < xThreshold && dx < 0) {
            xProgress.postValue(progressValue / progressMax);
            mColumnHeaderRecyclerView.scrollBy(dx, 0);
            for (int i = 0; i < mCellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) mCellLayoutManager.getChildAt(i);
                // Scroll horizontally
                child.scrollBy(dx, 0);
            }
        } else if (progressValue > xThreshold && dx > 0) {
            xProgress.postValue(progressValue / progressMax);
            mColumnHeaderRecyclerView.scrollBy(dx, 0);
            for (int i = 0; i < mCellLayoutManager.getChildCount(); i++) {
                CellRecyclerView child = (CellRecyclerView) mCellLayoutManager.getChildAt(i);
                // Scroll horizontally
                child.scrollBy(dx, 0);
            }
        } else {
            xProgress.postValue(progressValue / progressMax);
        }

        if (dx == 0) {
            mIsScrolling.postValue(false);
        } else {
            showTimestamp(700);
        }

        if ((progressValue / progressMax) == 1.0) {
            mFabEnabled.postValue(false);
        } else {
            mFabEnabled.postValue(true);
        }

        Float beatLength = (float) ((mTopWidth / 8) / 4);

        if (metronomeOn.getValue() != null && isPlaying.getValue() != null) {
            if (isPlaying.getValue() && metronomeOn.getValue()) {
                if (!metronomePlayer.isPlaying() && (xTopRecyclerView % beatLength) == 0f)
                    metronomePlayer.start();
            }
        }

        super.onScrolled(dx, dy);
    }

    public void showTimestamp(Integer delay) {
        mIsScrolling.postValue(true);
        handler.removeCallbacks(runner);
        handler.postDelayed(runner, delay);
    }

    public int getScrolledX() {
        return mScrolledX;
    }

    public void clearScrolledX() {
        mScrolledX = 0;
    }

    public int getScrolledY() {
        return mScrolledY;
    }

    @Override
    public void addOnScrollListener(@NonNull OnScrollListener listener) {
        if (listener instanceof HorizontalRecyclerViewListener) {
            if (mIsHorizontalScrollListenerRemoved) {
                mIsHorizontalScrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            } else {
                // Do not let add the listener
                Log.w(LOG_TAG, "mIsHorizontalScrollListenerRemoved has been tried to add itself "
                        + "before remove the old one");
            }
        } else if (listener instanceof VerticalRecyclerViewListener) {
            if (mIsVerticalScrollListenerRemoved) {
                mIsVerticalScrollListenerRemoved = false;
                super.addOnScrollListener(listener);
            } else {
                // Do not let add the listener
                Log.w(LOG_TAG, "mIsVerticalScrollListenerRemoved has been tried to add itself " +
                        "before remove the old one");
            }
        } else {
            super.addOnScrollListener(listener);
        }
    }

    @Override
    public void removeOnScrollListener(@NonNull OnScrollListener listener) {
        if (listener instanceof HorizontalRecyclerViewListener) {
            if (mIsHorizontalScrollListenerRemoved) {
                // Do not let remove the listener
                Log.e(LOG_TAG, "HorizontalRecyclerViewListener has been tried to remove " +
                        "itself before add new one");
            } else {
                mIsHorizontalScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else if (listener instanceof VerticalRecyclerViewListener) {
            if (mIsVerticalScrollListenerRemoved) {
                // Do not let remove the listener
                Log.e(LOG_TAG, "mIsVerticalScrollListenerRemoved has been tried to remove " +
                        "itself before add new one");
            } else {
                mIsVerticalScrollListenerRemoved = true;
                super.removeOnScrollListener(listener);
            }
        } else {
            super.removeOnScrollListener(listener);
        }
    }

    public boolean isHorizontalScrollListenerRemoved() {
        return mIsHorizontalScrollListenerRemoved;
    }

    public boolean isScrollOthers() {
        return !mIsHorizontalScrollListenerRemoved;
    }

    /**
     * Begin a standard fling with an initial velocity along each axis in pixels per second.
     * If the velocity given is below the system-defined minimum this method will return false
     * and no fling will occur.
     *
     * @param velocityX Initial horizontal velocity in pixels per second
     * @param velocityY Initial vertical velocity in pixels per second
     * @return true if the fling was started, false if the velocity was too low to fling or
     * LayoutManager does not support scrolling in the axis fling is issued.
     * @see LayoutManager#canScrollVertically()
     * @see LayoutManager#canScrollHorizontally()
     */
    @Override
    public boolean fling(int velocityX, int velocityY) {
        // Adjust speeds to be able to provide smoother scroll.
        //velocityX *= 0.6;
        //velocityY *= 0.6;
        return super.fling(velocityX, velocityY);
    }
}
