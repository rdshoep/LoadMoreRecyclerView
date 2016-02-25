package com.rdshoep.android.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.rdshoep.android.recyclerview.LoadMoreRecyclerView;
import com.rdshoep.android.recyclerview.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener
        , OnLoadMoreListener, Runnable {

    SwipeRefreshLayout refreshLayout;
    LoadMoreRecyclerView mRecyclerView;

    int page = 0;
    int pageSize = 20;
    List<String> items = new ArrayList<>();
    DemoAdapter mAdapter;

    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshList);
        mRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);

        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light
                , android.R.color.holo_orange_light, android.R.color.holo_red_light);
        refreshLayout.setOnRefreshListener(this);

        mAdapter = new DemoAdapter(items);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadMoreListener(this);

        refresh();
    }

    public void refresh() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        onRefresh();
    }

    @Override
    public void onLoadMore() {
        page++;
        mHandler.postDelayed(this, 3000);
    }

    @Override
    public void onRefresh() {
        items.clear();
        page = 1;

        mHandler.postDelayed(this, 3000);
    }

    @Override
    public void run() {
        int count = page * pageSize;
        int start = items.size();

        for (int i = start; i < count; ) {
            items.add("Item " + ++i);
        }

        if (page == 1) {
            refreshLayout.setRefreshing(false);
        }

        if (page >= 4) {
            mRecyclerView.notifyNoMoreData();
        } else {
            mRecyclerView.setLoadMoreEnabled(true);
            mRecyclerView.notifyWaitForLoad();
        }
    }
}
