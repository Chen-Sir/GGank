package me.chensir.ggank.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chensir.ggank.R;
import me.chensir.ggank.bean.Gank;
import me.chensir.ggank.image.ImageLoaderUtil;
import me.chensir.ggank.util.Constants;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by Chensir on 2018/5/28.
 */

public class HomeGankAdapter extends RecyclerView.Adapter<HomeGankAdapter.GankViewHolder> {

    private List<Gank> mGankList;
    private Context mContext;

    private OnGankItemClickListener mOnGankItemClickListener;

    public void setOnGankItemClickListener(OnGankItemClickListener onGankItemClickListener) {
        mOnGankItemClickListener = onGankItemClickListener;
    }

    public interface OnGankItemClickListener {

        void onMeiZiClick(View view, String imageUrl);

        void onMessageClick(View view, String link, String desc);
    }


    public HomeGankAdapter(Context context, List<Gank> list) {
        mContext = context;
        mGankList = list;
    }

    public HomeGankAdapter(Context context, List<Gank> list, OnGankItemClickListener listener) {
        mContext = context;
        mGankList = list;
        mOnGankItemClickListener = listener;
    }

    @Override
    public GankViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_gank_home_list, parent, false);
        return new GankViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GankViewHolder holder, int position) {
        final Gank gank = mGankList.get(position);
        holder.mGank = gank;
        holder.mListener = mOnGankItemClickListener;
        if (gank.getType().equals(Constants.FLAG_MEIZI)) {
            holder.mTitleTv.setVisibility(View.GONE);
            holder.mMeiziIv.setVisibility(View.VISIBLE);
            ImageLoaderUtil.get().loadImagePrepareShareElement(mContext, gank.getUrl(), holder.mMeiziIv, true);
        } else {
            holder.mTitleTv.setText(gank.getDesc());
            holder.mTitleTv.setVisibility(View.VISIBLE);
            holder.mMeiziIv.setVisibility(View.GONE);
        }

        holder.mCategoryTv.setText(gank.getType());
        holder.mAuthorTv.setText(mContext.getString(R.string.via_author, gank.getWho()));
    }

    @Override
    public int getItemCount() {
        return mGankList == null ? 0 : mGankList.size();
    }

    public void replaceData(List<Gank> list) {
        setList(list);
        notifyDataSetChanged();
    }

    public void addData(List<Gank> list) {
        addList(list);
        notifyItemRangeInserted(mGankList.size() - list.size(), list.size());
    }

    private void addList(List<Gank> list) {
        mGankList.addAll(checkNotNull(list));
    }

    private void setList(List<Gank> list) {
        mGankList = checkNotNull(list);
    }


    static class GankViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_gank_title)
        TextView mTitleTv;

        @BindView(R.id.item_gank_category)
        TextView mCategoryTv;

        @BindView(R.id.item_gank_author)
        TextView mAuthorTv;

        @BindView(R.id.item_gank_meizi)
        ImageView mMeiziIv;

        Gank mGank;

        OnGankItemClickListener mListener;

        public GankViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(v -> {
                if (mListener != null && mGank != null) {
                    if (mGank.getType().equals(Constants.FLAG_MEIZI)) {
                        mListener.onMeiZiClick(mMeiziIv, mGank.getUrl());
                    } else {
                        mListener.onMessageClick(v, mGank.getUrl(), mGank.getDesc());
                    }
                }
            });
        }
    }

}
