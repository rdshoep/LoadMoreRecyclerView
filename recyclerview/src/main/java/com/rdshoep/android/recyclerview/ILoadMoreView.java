package com.rdshoep.android.recyclerview;
/*
 * @description
 *   Please write the ILoadMoreView module's description
 * @author Zhang (rdshoep@126.com)
 *   http://www.rdshoep.com/
 * @version 
 *   1.0.0(1/8/2016)
 */

public interface ILoadMoreView {

    /**
     * 设置为等待显示状态
     */
    void setWaitLoadingState();

    /**
     * 设置正在加载状态
     */
    void setLoadingState();

    /**
     * 设置无更多数据状态
     */
    void setNoMoreDataState();
}
