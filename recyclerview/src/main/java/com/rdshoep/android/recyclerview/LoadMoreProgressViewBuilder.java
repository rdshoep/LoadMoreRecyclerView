package com.rdshoep.android.recyclerview;
/*
 * @description
 *   Please write the LoadMoreViewBuilder module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(1/10/2016)
 */

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class LoadMoreProgressViewBuilder {
    LoadMoreState state;

    public LoadMoreProgressViewBuilder(LoadMoreState loadMoreState) {
        this.state = loadMoreState;
    }

    public final LoadMoreState getState() {
        return state;
    }

    public final LoadMoreProgressViewBuilder setState(LoadMoreState state) {
        this.state = state;
        return this;
    }

    public int getLayoutId() {
        return R.layout.view_footer_loading_more;
    }

    public LoadMoreViewHolder createLoadMoreViewHolder(View layoutView) {
        switch (state.getType()) {
            case LoadMoreRecyclerView.TYPE_PROGRESS:
                return new ProgressViewHolder(layoutView, state);
            case LoadMoreRecyclerView.TYPE_PROGRESS_WITH_TEXT:
                return new ProgressWithContentViewHolder(layoutView, state);
            default:
                return new ProgressViewHolder(layoutView, state);
        }
    }

    static class ProgressViewHolder extends LoadMoreViewHolder {
        View loadingView;

        public ProgressViewHolder(View itemView, LoadMoreState loadMoreState) {
            super(itemView, loadMoreState);

            loadingView = itemView.findViewById(android.R.id.progress);
        }

        @Override
        public void setWaitLoadingState() {
            setLoadingState();
        }

        @Override
        public void setLoadingState() {
            loadingView.setVisibility(View.VISIBLE);
        }

        @Override
        public void setNoMoreDataState() {
            loadingView.setVisibility(View.INVISIBLE);
            itemView.setVisibility(View.INVISIBLE);
        }
    }

    static class ProgressWithContentViewHolder extends LoadMoreViewHolder implements View.OnClickListener {
        View loadingView;
        TextView infoText;

        public ProgressWithContentViewHolder(View itemView, LoadMoreState loadMoreState) {
            super(itemView, loadMoreState);

            loadingView = itemView.findViewById(android.R.id.progress);
            infoText = (TextView) itemView.findViewById(android.R.id.text1);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setWaitLoadingState() {
            if (!state.isAutoLoadMore()) {
                String waitForLoadText = state.getWaitForLoadText();
                if (!TextUtils.isEmpty(waitForLoadText)) {
                    infoText.setText(waitForLoadText);
                } else {
                    infoText.setText("Click to load more...");
                }

                infoText.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
            } else {
                setLoadingState();
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setLoadingState() {
            String loadingTip = state.getLoadingText();
            if (!TextUtils.isEmpty(loadingTip)) {
                infoText.setText(loadingTip);
            } else {
                infoText.setText("Loading...");
            }

            infoText.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.VISIBLE);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void setNoMoreDataState() {
            String noDataTip = state.getNoMoreDataText();
            if (!TextUtils.isEmpty(noDataTip)) {
                infoText.setText(noDataTip);
            } else {
                infoText.setText("No more data.");
            }

            infoText.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (!state.isLoading() && !state.isNoMoreData() && !state.isAutoLoadMore()) {
                setLoadingState();
                if (state.getOnLoadMoreListener() != null) {
                    state.getOnLoadMoreListener().onLoadMore();
                }
            }
        }
    }
}
