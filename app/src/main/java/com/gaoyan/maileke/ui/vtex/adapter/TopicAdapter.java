package com.gaoyan.maileke.ui.vtex.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaoyan.maileke.R;
import com.gaoyan.maileke.app.Constants;
import com.gaoyan.maileke.component.ImageLoader;
import com.gaoyan.maileke.model.bean.TopicListBean;
import com.gaoyan.maileke.ui.vtex.activity.RepliesActivity;
import com.gaoyan.maileke.widget.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by codeest on 16/12/23.
 */

public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private List<TopicListBean> mList;

    public TopicAdapter(Context context, List<TopicListBean> mList) {
        this.mContext = context;
        this.mList = mList;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_vtex, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        TopicListBean bean = mList.get(position);
        ImageLoader.load(mContext, bean.getImgUrl(), holder.ivTopicFace);
        holder.tvTopicName.setText(bean.getName());
        holder.tvTopicTips.setText(bean.getUpdateTime() + " • 最后回复 " + bean.getLastUser());
        holder.tvTopicComment.setText(String.valueOf(bean.getCommentNum()));
        holder.tvTopicNode.setText(bean.getNode());
        holder.tvTopicTitle.setText(bean.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(mContext, RepliesActivity.class);
                intent.putExtra(Constants.IT_VTEX_TOPIC_ID, mList.get(holder.getAdapterPosition()).getTopicId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void updateData(List<TopicListBean> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_topic_face)
        SquareImageView ivTopicFace;
        @BindView(R.id.tv_topic_name)
        TextView tvTopicName;
        @BindView(R.id.tv_topic_tips)
        TextView tvTopicTips;
        @BindView(R.id.tv_topic_comment)
        TextView tvTopicComment;
        @BindView(R.id.tv_topic_node)
        TextView tvTopicNode;
        @BindView(R.id.tv_topic_title)
        TextView tvTopicTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
