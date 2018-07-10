package me.chensir.ggank.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import me.chensir.ggank.R;
import me.chensir.ggank.bean.Category;
import me.chensir.ggank.bean.DayGankData;
import me.chensir.ggank.bean.Gank;
import me.chensir.ggank.image.ImageLoader;
import me.chensir.ggank.image.ImageLoaderUtil;
import me.chensir.ggank.net.ServiceFactory;
import me.chensir.ggank.ui.BaseActivity;
import me.chensir.ggank.ui.adapter.CategoryViewBinder;
import me.chensir.ggank.ui.adapter.GankViewBinder;
import me.chensir.ggank.util.ActivityUtils;
import me.chensir.ggank.util.DateUtil;
import me.chensir.ggank.util.RxUtils;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;

public class GankActivity extends BaseActivity {

    private static final String EXTRA_ARG_DATE = "extra_date";
    private static final String EXTRA_ARG_IMG_URL = "extra_img_url";

    public static final String TRANSIT_GANK_MEIZI = "gank_meizi";


    @BindView(R.id.activity_gank_rv)
    RecyclerView mRecyclerView;

    @BindView(R.id.activity_gank_collapsing_iv)
    ImageView mMeiziImageView;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;


    private Items mList;
    private MultiTypeAdapter mAdapter;

    private int mYear;
    private int mMonth;
    private int mDay;


    public static Intent getCallingIntent(Context context, Date date, String url) {
        Intent intent = new Intent(context, GankActivity.class);
        intent.putExtra(EXTRA_ARG_DATE, date);
        intent.putExtra(EXTRA_ARG_IMG_URL, url);
        return intent;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected int provideContentViewResId() {
        return R.layout.activity_gank;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
        ViewCompat.setTransitionName(mMeiziImageView, TRANSIT_GANK_MEIZI);
        supportPostponeEnterTransition();
        String url = getIntent().getStringExtra(EXTRA_ARG_IMG_URL);
        ImageLoaderUtil.get().loadImageForSharingElement(this, url, mMeiziImageView, new ImageLoader.ImageRequestListener() {
            @Override
            public void onLoadFailed() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onResourceReady() {
                supportStartPostponedEnterTransition();
                mMeiziImageView.setOnClickListener(v -> startPictureActivity(url, v));
            }
        });
        parseDate();
        initRecyclerView();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        loadData();
    }

    private void parseDate() {
        Date date = (Date) getIntent().getSerializableExtra(EXTRA_ARG_DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH) + 1;
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        String title;
        if (DateUtil.isToday(date)) {
            title = "今日干货";
        } else {
            title = DateUtil.toDateString(date) + "干货";
        }
        mCollapsingToolbarLayout.setTitle(title);
    }

    @SuppressLint("CheckResult")
    private void loadData() {
        ServiceFactory.getGankApiSingleton().getDayGankData(mYear, mMonth, mDay)
                .map(DayGankData::getResults).map(this::installAllResults)
                .compose(RxUtils.defaultSchedulers())
                .compose(this.bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(list -> {
                    if (!list.isEmpty()) {
                        int delay = 0;
                        // 这里延时250毫秒刷新列表，是为了共享元素动画无卡顿切换
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            delay = 270;
                        }
                        new Handler().postDelayed(() -> mAdapter.notifyItemRangeInserted(0, list.size()), delay);
                    } else {
                        // do none.
                    }
                }, Throwable::printStackTrace);
    }

    /**
     * 加载数据，并将数据铺平。
     */
    private List<Object> installAllResults(DayGankData.Result result) {

        if (result.getAndroidList() != null) {
            mList.add(new Category(result.getAndroidList().get(0).getType()));
            mList.addAll(result.getAndroidList());
        }

        if (result.getiOSList() != null) {
            mList.add(new Category(result.getiOSList().get(0).getType()));
            mList.addAll(result.getiOSList());
        }

        if (result.getAppList() != null) {
            mList.add(new Category(result.getAppList().get(0).getType()));
            mList.addAll(result.getAppList());
        }

        if (result.get拓展资源List() != null) {
            mList.add(new Category(result.get拓展资源List().get(0).getType()));
            mList.addAll(result.get拓展资源List());
        }
        if (result.get休息视频List() != null) {
            mList.add(new Category(result.get休息视频List().get(0).getType()));
            mList.addAll(result.get休息视频List());
        }
        if (result.get瞎推荐List() != null) {
            mList.add(new Category(result.get瞎推荐List().get(0).getType()));
            mList.addAll(result.get瞎推荐List());
        }

        return mList;
    }

    private void initRecyclerView() {
        mList = new Items(0);
        mAdapter = new MultiTypeAdapter(mList);
        mAdapter.register(Gank.class, new GankViewBinder());
        mAdapter.register(Category.class, new CategoryViewBinder());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void startPictureActivity(String url, View transitView) {
        Intent intent = PictureActivity.getCallingIntent(this, url);
        ActivityUtils.startTransitionAnimationActivity(this, intent, transitView, PictureActivity.TRANSIT_MEIZI);
    }
}
