package com.gaoyan.maileke.ui.zhihu.fragment;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.gaoyan.maileke.R;
import com.gaoyan.maileke.app.Constants;
import com.gaoyan.maileke.base.RootFragment;
import com.gaoyan.maileke.model.bean.HotListBean;
import com.gaoyan.maileke.presenter.zhihu.HotPresenter;
import com.gaoyan.maileke.base.contract.zhihu.HotContract;
import com.gaoyan.maileke.ui.zhihu.activity.ZhihuDetailActivity;
import com.gaoyan.maileke.ui.zhihu.adapter.HotAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by codeest on 2016/8/11.
 */
public class HotFragment extends RootFragment<HotPresenter> implements HotContract.View {

    @BindView(R.id.view_main)
    RecyclerView rvHotContent;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;

    List<HotListBean.RecentBean> mList;
    HotAdapter mAdapter;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_common_list;
    }

    @Override
    protected void initEventAndData() {
        super.initEventAndData();
        mList = new ArrayList<>();
        stateLoading();
        mAdapter = new HotAdapter(mContext,mList);
        rvHotContent.setVisibility(View.INVISIBLE);
        rvHotContent.setLayoutManager(new LinearLayoutManager(mContext));
        rvHotContent.setAdapter(mAdapter);
        mPresenter.getHotData();
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHotData();
            }
        });
        mAdapter.setOnItemClickListener(new HotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View shareView) {
                mPresenter.insertReadToDB(mList.get(position).getNews_id());
                mAdapter.setReadState(position,true);
                mAdapter.notifyItemChanged(position);
                Intent intent = new Intent();
                intent.setClass(mContext, ZhihuDetailActivity.class);
                intent.putExtra(Constants.IT_ZHIHU_DETAIL_ID, mList.get(position).getNews_id());
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, shareView, "shareView");
                mContext.startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    public void stateError() {
        super.stateError();
        if(swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showContent(HotListBean hotListBean) {
        if (swipeRefresh.isRefreshing()) {
            swipeRefresh.setRefreshing(false);
        }
        stateMain();
        rvHotContent.setVisibility(View.VISIBLE);
        mList.clear();
        mList.addAll(hotListBean.getRecent());
        mAdapter.notifyDataSetChanged();
    }
}
