package com.rdshoep.android.recyclerview;
/*
 * @description
 *   Please write the LoadMoreRecyclerView module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(8/27/2015)
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

public class LoadMoreRecyclerView extends RecyclerView {
    static final String TAG = LoadMoreRecyclerView.class.getSimpleName();

    public static final int TYPE_PROGRESS = 1;
    public static final int TYPE_PROGRESS_WITH_TEXT = 2;

    private LoadMoreScrollListener scrollListener;
    private OnLoadMoreListener loadMoreListener;
    private LoadMoreState state;
    private LoadMoreRecyclerViewAdapter mAdapter;

    private boolean loadMoreEnabled = false;
    int autoLoadLineCount = 1;

    private boolean hasDataChanged = false;
    private LoadMoreHandler mHandler = new LoadMoreHandler(this);

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        state = new LoadMoreState();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadMoreRecyclerView, defStyle, 0);
        autoLoadLineCount = a.getInt(R.styleable.LoadMoreRecyclerView_autoLoadLine, 1);

        state.setAutoLoadMore(a.getBoolean(R.styleable.LoadMoreRecyclerView_autoLoad, true));
        state.setType(a.getInt(R.styleable.LoadMoreRecyclerView_loadMoreType, TYPE_PROGRESS_WITH_TEXT));

        String loadingText = a.getString(R.styleable.LoadMoreRecyclerView_loadingText);
        state.setLoadingText(TextUtils.isEmpty(loadingText) ? context.getString(R.string.loading) : loadingText);

        String waitForLoadText = a.getString(R.styleable.LoadMoreRecyclerView_waitForLoadText);
        state.setWaitForLoadText(TextUtils.isEmpty(waitForLoadText) ? context.getString(R.string.click_to_load_data) : waitForLoadText);

        String noMoreDataText = a.getString(R.styleable.LoadMoreRecyclerView_loadEndText);
        state.setNoMoreDataText(TextUtils.isEmpty(noMoreDataText) ? context.getString(R.string.no_more_data) : noMoreDataText);

        a.recycle();

        init();
    }

    protected void init() {
        scrollListener = new LoadMoreScrollListener() {
            @Override
            public void onLoadMore() {
                loading = true;
                LoadMoreRecyclerView.this.loadMore();
            }
        };
        scrollListener.setAutoLoadLineCount(autoLoadLineCount);
    }

    /**
     * set load more listener to handle loadMore event
     *
     * @param loadMoreListener loadMore listener
     * @return loadMoreRecyclerView.this
     */
    public LoadMoreRecyclerView setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
        state.setOnLoadMoreListener(loadMoreListener);
        return this;
    }

    /**
     * set the min invisible line number to trigger the loadMore event
     *
     * @param lineNumber auto load trigger line number
     * @return this caller view
     */
    public LoadMoreRecyclerView setAutoLoadLineCount(int lineNumber) {
        autoLoadLineCount = lineNumber;
        scrollListener.setAutoLoadLineCount(lineNumber);
        return this;
    }

    @Override
    public void setAdapter(@NonNull Adapter adapter) {
        if (mAdapter == null) {
            mAdapter = new LoadMoreRecyclerViewAdapter(adapter, state);
        } else {
            mAdapter.setContentAdapter(adapter);
        }

        super.setAdapter(mAdapter);
    }

    public void setLoadMoreEnabled(boolean enabled) {
        if (enabled == loadMoreEnabled) return;

        if (state.isAutoLoadMore()) {
            setAutoModeEnabled(enabled);
        }

        state.setState(LoadMoreStatus.WAIT);
        mAdapter.setLoadMoreEnabled(enabled);

        loadMoreEnabled = enabled;
    }

    private void setAutoModeEnabled(boolean enabled) {
        if (scrollListener.isLoadMoreEnabled() == enabled) return;

        if (enabled) {
            addOnScrollListener(scrollListener);
        } else {
            removeOnScrollListener(scrollListener);
        }
        scrollListener.setLoadMoreEnable(enabled);
    }

    private void setLoadingState() {
        state.setState(LoadMoreStatus.LOADING);
    }

    private void setLoadCompleteState() {
        state.setState(LoadMoreStatus.WAIT);

        if (state.isAutoLoadMore()) {
            scrollListener.onLoadComplete();
        }
    }

    private void setWaitForLoadState() {
        state.setState(LoadMoreStatus.WAIT);

        if (state.isAutoLoadMore()) {
            setAutoModeEnabled(true);
        }
    }

    private void setNoMoreDataState() {
        state.setState(LoadMoreStatus.NO_MORE);

        if (state.isAutoLoadMore()) {
            setAutoModeEnabled(false);
        }
    }

    private boolean verifyLoadMoreEnabled() {
        if (!loadMoreEnabled) {
            Log.w(TAG, "verifyLoadMoreEnabled: loadMoreEnabled is disabled!");
        }

        return loadMoreEnabled;
    }

    /**
     * do something when loading more
     */
    public final void loadMore() {
        if (!verifyLoadMoreEnabled()) return;

        if (!state.isLoading()) {
            notifyLoading();

            if (loadMoreListener != null) {
                loadMoreListener.onLoadMore();
            }
        }
    }

    protected void notifyLoading() {
        if (!verifyLoadMoreEnabled()) return;

        if (!state.isLoading()) {
            setLoadingState();

            notifyDataChanged();
        }
    }

    /**
     * reset to listen load more event when load more complete
     * if you forgot to invoke this method, you can't receive loadMore event again
     */
    public final void notifyLoadComplete() {
        if (!verifyLoadMoreEnabled()) return;

        if (state.getState() == LoadMoreStatus.LOADING) {
            setLoadCompleteState();

            notifyDataChanged();
        }
    }

    public void notifyWaitForLoad() {
        if (!verifyLoadMoreEnabled()) return;

        if (state.getState() == LoadMoreStatus.NO_MORE) {
            if (state.isAutoLoadMore()) {
                setAutoModeEnabled(true);
            }
        } else if (state.getState() == LoadMoreStatus.LOADING) {
            setLoadCompleteState();
        }

        state.setState(LoadMoreStatus.WAIT);

        notifyDataChanged();
    }

    public void notifyNoMoreData() {
        if (!verifyLoadMoreEnabled()) return;

        if (state.getState() == LoadMoreStatus.LOADING) {
            setLoadCompleteState();
        }

        if (state.getState() != LoadMoreStatus.NO_MORE) {
            setNoMoreDataState();

            notifyDataChanged();
        }
    }

    private void notifyDataChanged() {
        Log.d(TAG, String.format("notifyDataChanged: state:%s, scrollLoadMore: %b, isLoading: %b"
                , state.getState(), scrollListener.isLoadMoreEnabled(), scrollListener.isLoading()));

        //通过本方法避免多次无效的notifyDataChanged执行
        hasDataChanged = true;
        mHandler.sendEmptyMessage(LoadMoreHandler.MESSAGE_DATA_CHANGED);
    }

    @SuppressWarnings("unused")
    public void setCustomProgressBuilder(LoadMoreProgressViewBuilder builder) {
        mAdapter.setBuilder(builder);
    }

    public LoadMoreState getState() {
        return state;
    }

    static class LoadMoreHandler extends Handler {
        static final int MESSAGE_DATA_CHANGED = 1;
        LoadMoreRecyclerView mRecyclerView;

        public LoadMoreHandler(@NonNull LoadMoreRecyclerView mRecyclerView) {
            this.mRecyclerView = mRecyclerView;
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_DATA_CHANGED:
                    if (mRecyclerView.hasDataChanged) {
                        mRecyclerView.mAdapter.notifyDataSetChanged();
                        mRecyclerView.hasDataChanged = false;
                    }
                    break;
            }
        }
    }
}
