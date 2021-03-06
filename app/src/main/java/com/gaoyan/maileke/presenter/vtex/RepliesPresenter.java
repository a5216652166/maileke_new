package com.gaoyan.maileke.presenter.vtex;

import com.gaoyan.maileke.base.RxPresenter;
import com.gaoyan.maileke.model.DataManager;
import com.gaoyan.maileke.model.bean.NodeListBean;
import com.gaoyan.maileke.model.bean.RealmLikeBean;
import com.gaoyan.maileke.model.bean.RepliesListBean;
import com.gaoyan.maileke.base.contract.vtex.RepliesContract;
import com.gaoyan.maileke.util.RxUtil;
import com.gaoyan.maileke.widget.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by codeest on 16/12/23.
 */

public class RepliesPresenter extends RxPresenter<RepliesContract.View> implements RepliesContract.Presenter{

    private DataManager mDataManager;

    @Inject
    public RepliesPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getContent(String topic_id) {
        addSubscribe(mDataManager.fetchRepliesList(topic_id)
                .compose(RxUtil.<List<RepliesListBean>>rxSchedulerHelper())
                .subscribeWith(new CommonSubscriber<List<RepliesListBean>>(mView) {
                    @Override
                    public void onNext(List<RepliesListBean> repliesListBeen) {
                        mView.showContent(repliesListBeen);
                    }
                })
        );
    }

    @Override
    public void getTopInfo(String topic_id) {
        addSubscribe(mDataManager.fetchTopicInfo(topic_id)
                .compose(RxUtil.<List<NodeListBean>>rxSchedulerHelper())
                .filter(new Predicate<List<NodeListBean>>() {
                    @Override
                    public boolean test(@NonNull List<NodeListBean> nodeListBeen) throws Exception {
                        return nodeListBeen.size() > 0;
                    }
                })
                .map(new Function<List<NodeListBean>, NodeListBean>() {
                    @Override
                    public NodeListBean apply(List<NodeListBean> nodeListBeen) {
                        return nodeListBeen.get(0);
                    }
                })
                .subscribeWith(new CommonSubscriber<NodeListBean>(mView) {
                    @Override
                    public void onNext(NodeListBean nodeListBean) {
                        mView.showTopInfo(nodeListBean);
                    }
                })
        );
    }

    @Override
    public void insert(RealmLikeBean bean) {
        mDataManager.insertLikeBean(bean);
    }

    @Override
    public void delete(String id) {
        mDataManager.deleteLikeBean(id);
    }

    @Override
    public boolean query(String id) {
        return mDataManager.queryLikeId(id);
    }
}
