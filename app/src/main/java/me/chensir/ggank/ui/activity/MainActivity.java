package me.chensir.ggank.ui.activity;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;

import butterknife.BindView;
import me.chensir.ggank.R;
import me.chensir.ggank.ui.BaseActivity;
import me.chensir.ggank.ui.NormalFragmentPagerAdapter;
import me.chensir.ggank.ui.fragment.AboutFragment;
import me.chensir.ggank.ui.fragment.HomeFragment;
import me.chensir.ggank.ui.fragment.MeiZiFragment;

public class MainActivity extends BaseActivity {

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.bottomNavigationView)
    BottomNavigationView mBottomNavigationView;


    @Override
    protected int provideContentViewResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewPager();
        initListener();
    }

    private void initListener() {
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        mBottomNavigationView.setSelectedItemId(R.id.action_home);
                        break;
                    case 1:
                        mBottomNavigationView.setSelectedItemId(R.id.action_explore);
                        break;
                    case 2:
                        mBottomNavigationView.setSelectedItemId(R.id.action_about);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            switch (itemId) {
                case R.id.action_home:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.action_explore:
                    mViewPager.setCurrentItem(1);
                    break;
                case R.id.action_about:
                    mViewPager.setCurrentItem(2);
                    break;
            }
            return true;
        });
    }

    private void setupViewPager() {
        NormalFragmentPagerAdapter adapter = new NormalFragmentPagerAdapter(this.getSupportFragmentManager());
        adapter.addFragment(HomeFragment.newInstance(), getString(R.string.home));
        adapter.addFragment(MeiZiFragment.newInstance(), getString(R.string.welfare));
        adapter.addFragment(AboutFragment.newInstance(), getString(R.string.about));
        mViewPager.setAdapter(adapter);
    }
}
