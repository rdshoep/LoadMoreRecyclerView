package com.rdshoep.android.recyclerview;
/*
 * @description
 *   Please write the LoadMoreState module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(1/11/2016)
 */

public class LoadMoreState {
    private boolean autoLoadMore = true;
    private LoadMoreStatus state = LoadMoreStatus.WAIT;
    private int type = LoadMoreRecyclerView.TYPE_PROGRESS;

    private String loadingText;
    private String waitForLoadText;
    private String noMoreDataText;

    private OnLoadMoreListener onLoadMoreListener;

    public int getType() {
        return type;
    }

    public LoadMoreState setType(int type) {
        this.type = type;
        return this;
    }

    public boolean isLoading() {
        return state == LoadMoreStatus.LOADING;
    }

    public boolean isNoMoreData() {
        return state == LoadMoreStatus.NO_MORE;
    }

    public void setLoading(boolean loading) {
        state = loading ? LoadMoreStatus.LOADING : LoadMoreStatus.WAIT;
    }

    public LoadMoreStatus getState() {
        return state;
    }

    public LoadMoreState setState(LoadMoreStatus state) {
        this.state = state;
        return this;
    }

    public boolean isAutoLoadMore() {
        return autoLoadMore;
    }

    public LoadMoreState setAutoLoadMore(boolean autoLoadMore) {
        this.autoLoadMore = autoLoadMore;
        return this;
    }

    public String getNoMoreDataText() {
        return noMoreDataText;
    }

    public LoadMoreState setNoMoreDataText(String noMoreDataText) {
        this.noMoreDataText = noMoreDataText;
        return this;
    }

    public String getLoadingText() {
        return loadingText;
    }

    public LoadMoreState setLoadingText(String loadingText) {
        this.loadingText = loadingText;
        return this;
    }

    public String getWaitForLoadText() {
        return waitForLoadText;
    }

    public LoadMoreState setWaitForLoadText(String waitForLoadText) {
        this.waitForLoadText = waitForLoadText;
        return this;
    }

    public OnLoadMoreListener getOnLoadMoreListener() {
        return onLoadMoreListener;
    }

    public LoadMoreState setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
        return this;
    }
}
