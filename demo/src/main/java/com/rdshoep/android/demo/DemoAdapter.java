package com.rdshoep.android.demo;
/*
 * @description
 *   Please write the DemoAdapter module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(2/25/2016)
 */

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DemoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> items;

    public DemoAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DemoViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_demo, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DemoViewHolder) {
            String text = items.get(position);
            ((DemoViewHolder) holder).textView.setText(text);
        }
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    static class DemoViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public DemoViewHolder(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
