package com.rdshoep.android.recyclerview;
/*
 * @description
 *   Please write the LoadMoreRecyclerViewAdapter module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(8/27/2015)
 */

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class LoadMoreRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    static final String TAG = "LoadMoreRecyclerView";
    static final int VIEW_TYPE_FOOTER = 20000;

    private RecyclerView.Adapter contentAdapter;
    private LoadMoreState state;
    private LoadMoreProgressViewBuilder builder;
    private boolean loadMoreEnabled;

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();

            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);

            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);

            notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);

            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);

            notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);


            notifyItemRangeChanged(fromPosition, toPosition);
        }
    };

    public LoadMoreRecyclerViewAdapter(@NonNull RecyclerView.Adapter contentAdapter,
                                       @NonNull LoadMoreState state) {
        this.contentAdapter = contentAdapter;
        this.state = state;
        this.builder = new LoadMoreProgressViewBuilder(state);
    }

    public RecyclerView.Adapter getContentAdapter() {
        return contentAdapter;
    }

    public LoadMoreRecyclerViewAdapter setContentAdapter(@NonNull RecyclerView.Adapter contentAdapter) {
        this.contentAdapter = contentAdapter;
        return this;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_FOOTER:
                return onCreateFooterViewHolder(parent, viewType);
            default:
                return contentAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_FOOTER:
                onBindFooterViewHolder(holder, position);
                break;
            default:
                contentAdapter.onBindViewHolder(holder, position);
                break;
        }
    }

    @Override
    public final int getItemViewType(int position) {
        if (position >= getContentCount()) {
            return VIEW_TYPE_FOOTER;
        } else {
            return contentAdapter.getItemViewType(position);
        }
    }

    @Override
    public final int getItemCount() {
        return getContentCount() + getFooterCount();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);

        contentAdapter.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        if (position >= getContentCount()) {
            return position;
        }

        return contentAdapter.getItemId(position);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        contentAdapter.onViewRecycled(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);

        contentAdapter.onViewAttachedToWindow(holder);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);

        contentAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        contentAdapter.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        contentAdapter.unregisterAdapterDataObserver(adapterDataObserver);
        contentAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    /**
     * get content item count
     *
     * @return content item count
     */
    private int getContentCount() {
        return contentAdapter.getItemCount();
    }

    /**
     * get footer item count
     * this version only support one footer item
     *
     * @return footer item count
     */
    private int getFooterCount() {
        return loadMoreEnabled ? 1 : 0;
    }

    /**
     * create footer viewHolder
     * if you want display custom footer view, you can extends this class
     * and rewrite this method and #onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position)>
     * to bind custom viewHolder
     *
     * @param parent   parent view
     * @param viewType current footer viewType
     * @return footer viewHolder
     */
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return builder.createLoadMoreViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(builder.getLayoutId(), parent, false));
    }

    /**
     * bind viewHolder for footer, show footer content
     *
     * @param holder   current footer viewHolder
     * @param position footer item position
     * @see #onCreateFooterViewHolder(ViewGroup, int)
     */
    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadMoreViewHolder) {
            ((LoadMoreViewHolder) holder).bindLoadMoreViewHolder();
        }
    }

    public boolean isLoadMoreEnabled() {
        return loadMoreEnabled;
    }

    public LoadMoreRecyclerViewAdapter setLoadMoreEnabled(boolean loadMoreEnabled) {
        if (this.loadMoreEnabled != loadMoreEnabled) {
            this.loadMoreEnabled = loadMoreEnabled;
        }
        return this;
    }

    public LoadMoreProgressViewBuilder getBuilder() {
        return builder;
    }

    public LoadMoreRecyclerViewAdapter setBuilder(LoadMoreProgressViewBuilder builder) {
        this.builder = builder;
        if (this.state != null) {
            this.builder.setState(this.state);
        }
        return this;
    }
}
