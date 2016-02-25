package com.rdshoep.android.recyclerview;
/*
 * @description
 *   Please write the RecyclerViewScrollListener module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(8/27/2015)
 */

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {
    static final String TAG = LoadMoreScrollListener.class.getSimpleName();
    private int autoLoadLineCount = 1;
    protected boolean loading = false;
    protected boolean enabled = false;

    public LoadMoreScrollListener() {
    }

    /**
     * constructor
     * @param autoLoadLineCount auto load line count
     */
    public LoadMoreScrollListener(int autoLoadLineCount) {
        this.autoLoadLineCount = autoLoadLineCount;
    }

    /**
     * set the min invisible line number to trigger the loadMore event
     * @param autoLoadLineCount
     * @return
     */
    public LoadMoreScrollListener setAutoLoadLineCount(int autoLoadLineCount) {
        this.autoLoadLineCount = autoLoadLineCount;
        return this;
    }

    /**
     * enable/disable listen scroll event which handle loadMore
     * @param enable enable or disable handle scroll event
     * @return
     */
    public LoadMoreScrollListener setLoadMoreEnable(boolean enable){
        this.enabled = enable;
        return this;
    }

    public int getAutoLoadLineCount() {
        return autoLoadLineCount;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isLoadMoreEnabled() {
        return enabled;
    }

    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        Log.d(TAG, String.format("onScrolled: enabled:%b,loading:%b", enabled, loading));
        if(enabled && !loading) {
            int spanCount = 1;
            RecyclerView.LayoutManager mLayoutManager = recyclerView.getLayoutManager();
            if (mLayoutManager instanceof GridLayoutManager) {
                spanCount = ((GridLayoutManager) mLayoutManager).getSpanCount();
                //TODO need calculate some grid item occupy more than one span
            }

            int endPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
            int totalCount = recyclerView.getAdapter().getItemCount();

            int lastLineCount = totalCount % spanCount;
            int minNeedLoadMorePosition = totalCount - 1 - autoLoadLineCount * spanCount;
            if (lastLineCount != 0) {
                minNeedLoadMorePosition += spanCount - lastLineCount;
            }

            if (endPosition >= minNeedLoadMorePosition) {
                onLoadMore();
            }
        }
    }

    /**
     * abstract load more event, invoked by onScrollListener event
     */
    public abstract void onLoadMore();

    /**
     * invoke this method to restart listen scroll event
     */
    public void onLoadComplete(){
        this.loading = false;
    }
}
