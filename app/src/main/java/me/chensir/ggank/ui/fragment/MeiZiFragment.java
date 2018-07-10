package me.chensir.ggank.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import me.chensir.ggank.R;
import me.chensir.ggank.bean.MeiZiData;
import me.chensir.ggank.net.ServiceFactory;
import me.chensir.ggank.ui.BasePageFragment;
import me.chensir.ggank.ui.activity.GankActivity;
import me.chensir.ggank.ui.adapter.MeiZiAdapter;
import me.chensir.ggank.ui.adapter.MeiZiItemDecoration;
import me.chensir.ggank.util.ActivityUtils;
import me.chensir.ggank.util.RxUtils;


public class MeiZiFragment extends BasePageFragment {

    @BindView(R.id.meizi_fragment_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private MeiZiAdapter mMeiZiAdapter;
    private int mPage = 1;


    private MeiZiAdapter.OnMeiZiItemClickListener mMeiZiItemClickListener = this::startGankActivity;


    public MeiZiFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static MeiZiFragment newInstance() {
        return new MeiZiFragment();
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.fragment_mei_zi;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = super.onCreateView(inflater, container, savedInstanceState);
        setupRefreshLayout();
        setupRecyclerView();
        return root;
    }


    public void loadData() {
        loadData(true);
    }

    @SuppressLint("CheckResult")
    public void loadData(boolean clean) {
        ServiceFactory.getGankApiSingleton().getMeiZiData(mPage).map(MeiZiData::getResults)
                .compose(RxUtils.defaultSchedulers())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(list -> {
                    if (!isAdded()) {
                        return;
                    }
                    if (clean) {
                        mRefreshLayout.finishRefresh();
                        mMeiZiAdapter.replaceData(list);
                    } else {
                        mRefreshLayout.finishLoadMore(150);
                        mRecyclerView.stopScroll();
                        mMeiZiAdapter.addData(list);
                    }
                }, throwable -> {
                    if (clean) {
                        mRefreshLayout.finishRefresh(false);
                    } else {
                        mRefreshLayout.finishLoadMore(false);
                    }
                });
    }

    private void setupRecyclerView() {
        mMeiZiAdapter = new MeiZiAdapter(getContext(), new ArrayList<>(0), mMeiZiItemClickListener);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        int space = getResources().getDimensionPixelSize(R.dimen.normal_space);
        MeiZiItemDecoration meiZiItemDecoration = new MeiZiItemDecoration(space);
        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(meiZiItemDecoration);
        mRecyclerView.setAdapter(mMeiZiAdapter);
    }

    private void setupRefreshLayout() {

        mRefreshLayout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                mPage += 1;
                loadData(false);
            }

            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                mPage = 1;
                loadData();
            }
        });
    }

    private void startGankActivity(View view, Date date, String imageUrl) {
        Intent intent = GankActivity.getCallingIntent(getActivity(), date, imageUrl);
        ActivityUtils.startTransitionAnimationActivity(getActivity(), intent, view, GankActivity.TRANSIT_GANK_MEIZI);
    }

    @Override
    protected void lazyFetchData() {
        mRefreshLayout.autoRefresh();
    }
}
