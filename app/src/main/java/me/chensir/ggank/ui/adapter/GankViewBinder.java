package me.chensir.ggank.ui.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chensir.ggank.R;
import me.chensir.ggank.bean.Gank;
import me.chensir.ggank.ui.activity.WebActivity;
import me.chensir.ggank.util.StringStyles;
import me.drakeet.multitype.ItemViewBinder;

public class GankViewBinder extends ItemViewBinder<Gank, GankViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_gank_activity_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Gank gank) {
        holder.mGank = gank;
        SpannableStringBuilder builder = new SpannableStringBuilder(gank.getDesc()).append(
                StringStyles.format(holder.mTitleTv.getContext(), " (via. " +
                        gank.getWho() +
                        ")", R.style.ViaTextAppearance));
        CharSequence gankTitle = builder.subSequence(0, builder.length());
        holder.mTitleTv.setText(gankTitle);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_gank_activity_title)
        TextView mTitleTv;

        Gank mGank;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mTitleTv.setOnClickListener(v -> {
                if (mGank != null) {
                    Intent intent = WebActivity.getCallingIntent(itemView.getContext(), mGank.getUrl(), mGank.getDesc());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

    }
}
