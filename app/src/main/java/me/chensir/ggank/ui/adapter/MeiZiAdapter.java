package me.chensir.ggank.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chensir.ggank.R;
import me.chensir.ggank.bean.MeiZi;
import me.chensir.ggank.image.ImageLoaderUtil;
import me.chensir.ggank.ui.widget.ScaleImageView;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by Chensir on 2018/5/28.
 */

public class MeiZiAdapter extends RecyclerView.Adapter<MeiZiAdapter.MeiZiHolder> {

    private List<MeiZi> mMeiZiList;
    private Context mContext;


    private OnMeiZiItemClickListener mOnMeiZiItemClickListener;

    public void setOnMeiZiItemClickListener(OnMeiZiItemClickListener onMeiZiItemClickListener) {
        mOnMeiZiItemClickListener = onMeiZiItemClickListener;
    }

    public interface OnMeiZiItemClickListener {
        void onMeiZiClick(View view, Date date, String imageUrl);
    }


    public MeiZiAdapter(Context context, List<MeiZi> list) {
        mContext = context;
        mMeiZiList = list;
    }

    public MeiZiAdapter(Context context, List<MeiZi> list, OnMeiZiItemClickListener listener) {
        mContext = context;
        mMeiZiList = list;
        mOnMeiZiItemClickListener = listener;
    }

    @Override
    public MeiZiHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.item_meizi, parent, false);
        return new MeiZiHolder(v);
    }

    @Override
    public void onBindViewHolder(MeiZiHolder holder, int position) {
        if (position % 2 == 0) {
            holder.mMeiZiIv.setInitSize(1, 1);
        } else {
            holder.mMeiZiIv.setInitSize(3, 4);
        }
        final MeiZi meiZi = mMeiZiList.get(position);
        holder.mMeiZi = meiZi;
        holder.mListener = mOnMeiZiItemClickListener;
        ImageLoaderUtil.get().loadImagePrepareShareElement(mContext, meiZi.getUrl(), holder.mMeiZiIv, true);
    }

    @Override
    public int getItemCount() {
        return mMeiZiList == null ? 0 : mMeiZiList.size();
    }

    public void replaceData(List<MeiZi> list) {
        setList(list);
        notifyDataSetChanged();
    }

    public void addData(List<MeiZi> list) {
        addList(list);
        notifyItemRangeInserted(mMeiZiList.size() - list.size(), list.size());
    }

    private void addList(List<MeiZi> list) {
        mMeiZiList.addAll(checkNotNull(list));
    }

    private void setList(List<MeiZi> list) {
        mMeiZiList = checkNotNull(list);
    }


    static class MeiZiHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_meizi_iv)
        ScaleImageView mMeiZiIv;

        MeiZi mMeiZi;

        OnMeiZiItemClickListener mListener;

        public MeiZiHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mMeiZiIv.setOnClickListener(v -> {
                if (mMeiZi != null) {
                    if (mListener != null) {
                        mListener.onMeiZiClick(v, mMeiZi.getPublishedAt(), mMeiZi.getUrl());
                    }
                }
            });
        }
    }

}
