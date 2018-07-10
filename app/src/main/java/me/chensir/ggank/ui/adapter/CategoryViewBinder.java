package me.chensir.ggank.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chensir.ggank.R;
import me.chensir.ggank.bean.Category;
import me.drakeet.multitype.ItemViewBinder;

public class CategoryViewBinder extends ItemViewBinder<Category, CategoryViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_category_gank_activity_list, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull Category category) {
        holder.mCategoryTv.setText(category.categoryText);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_gank_activity_category_title)
        TextView mCategoryTv;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
