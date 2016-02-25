package com.rdshoep.android.recyclerview;
/*
 * @description
 *   Please write the LoadMoreViewHolder module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(1/10/2016)
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class LoadMoreViewHolder extends RecyclerView.ViewHolder implements ILoadMoreView {
    LoadMoreState state;

    public LoadMoreViewHolder(View itemView, LoadMoreState loadMoreState) {
        super(itemView);
        this.state = loadMoreState;
    }

    public final void bindLoadMoreViewHolder() {
        switch (state.getState()) {
            case WAIT:
                setWaitLoadingState();
                break;
            case LOADING:
                setLoadingState();
                break;
            case NO_MORE:
                setNoMoreDataState();
                break;
        }
    }
}
