package me.chensir.ggank.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;

import butterknife.BindView;
import me.chensir.ggank.R;
import me.chensir.ggank.bean.RandomGankData;
import me.chensir.ggank.net.ServiceFactory;
import me.chensir.ggank.ui.BasePageFragment;
import me.chensir.ggank.ui.activity.PictureActivity;
import me.chensir.ggank.ui.activity.WebActivity;
import me.chensir.ggank.ui.adapter.HomeGankAdapter;
import me.chensir.ggank.util.ActivityUtils;
import me.chensir.ggank.util.RxUtils;


public class HomeFragment extends BasePageFragment {

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.home_rv)
    RecyclerView mRecyclerView;

    private HomeGankAdapter mGankAdapter;

    private int mPage = 1;


    private HomeGankAdapter.OnGankItemClickListener mGankItemListener = new HomeGankAdapter.OnGankItemClickListener() {
        @Override
        public void onMeiZiClick(View view, String imageUrl) {
            startPictureActivity(imageUrl, view);
        }

        @Override
        public void onMessageClick(View view, String link, String desc) {
            Intent intent = WebActivity.getCallingIntent(getActivity(), link, desc);
            getActivity().startActivity(intent);
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment HomeFragment.
     */
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setupRecyclerView();
        setupRefreshLayout();
        return view;
    }

    private void setupRefreshLayout() {
        mRefreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
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

    public void loadData() {
        loadData(true);
    }

    @SuppressLint("CheckResult")
    public void loadData(boolean clean) {
        ServiceFactory.getGankApiSingleton().getGankData(mPage)
                .map(RandomGankData::getResults)
                .compose(RxUtils.defaultSchedulers())
                .compose(this.bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(list -> {
                    if (!isAdded()) {
                        return;
                    }
                    if (clean) {
                        mRefreshLayout.finishRefresh();
                        mGankAdapter.replaceData(list);
                    } else {
                        mRefreshLayout.finishLoadMore(150);
                        // If root layout have both AppBarLayout and RecyclerView, the scrolling operation of RecyclerView will change,
                        // the RecyclerView would resume previous fling velocity after the new ViewHolders are inserted in the end.
                        // It is related to the nested scrolling behavior which has been enhanced in support libraries v26,
                        // Google calls it “Carry on Scrolling”.
                        // Users may accidentally miss some interesting items if they do not scroll back,
                        // therefore, we decide to maintain the previous behavior by calling RecyclerView’s stopScroll() when ViewHolders are inserted.
                        // That will stop the mysterious force from nested scrolling mechanism!
                        mRecyclerView.stopScroll();
                        mGankAdapter.addData(list);
                    }
                }, throwable -> {
                    throwable.printStackTrace();
                    if (clean) {
                        mRefreshLayout.finishRefresh(false);
                    } else {
                        mRefreshLayout.finishLoadMore(false);
                    }
                });
    }

    private void setupRecyclerView() {
        mGankAdapter = new HomeGankAdapter(getContext(), new ArrayList<>(0), mGankItemListener);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecyclerView.setAdapter(mGankAdapter);
    }


    private void startPictureActivity(String url, View transitView) {
        Intent intent = PictureActivity.getCallingIntent(getActivity(), url);
        ActivityUtils.startTransitionAnimationActivity(getActivity(), intent, transitView, PictureActivity.TRANSIT_MEIZI);
    }

    @Override
    protected void lazyFetchData() {
        mRefreshLayout.autoRefresh(300);
    }
}
