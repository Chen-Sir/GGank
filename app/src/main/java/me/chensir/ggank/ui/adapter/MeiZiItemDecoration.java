package me.chensir.ggank.ui.adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class MeiZiItemDecoration extends RecyclerView.ItemDecoration {

    private int space;

    public MeiZiItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // 先重置ItemOffsets
        super.getItemOffsets(outRect, view, parent, state);

        if (!(parent.getLayoutManager() instanceof StaggeredGridLayoutManager)) {
            throw new IllegalStateException("The LayoutManager of the RecyclerView which used MeiZiItemDecoration must be StaggeredGridLayoutManager.");
        }


        StaggeredGridLayoutManager.LayoutParams params =
                (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();

        int position = parent.getChildLayoutPosition(view);
        if (position == 0 || position == 1) {
            outRect.top = space;
        }

        /**
         * 根据params.getSpanIndex()来判断左右边确定分割线
         */
        if (params.getSpanIndex() % 2 == 0) {
            outRect.left = space;
            outRect.right = space / 2;
            outRect.bottom = space;
        } else {
            //position % 2 == 1
            outRect.left = space / 2;
            outRect.right = space;
            outRect.bottom = space;
        }

    }
}
