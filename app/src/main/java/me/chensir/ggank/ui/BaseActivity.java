package me.chensir.ggank.ui;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.chensir.ggank.R;

/**
 * Created by Chensir on 2018/5/25.
 */

public abstract class BaseActivity extends RxAppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.app_bar_layout)
    AppBarLayout mAppBar;

    private boolean mIsHidden = false;

    @LayoutRes
    abstract protected int provideContentViewResId();

    public void onToolbarClick() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewResId());
        ButterKnife.bind(this);
        if (mToolbar == null || mAppBar == null) {
            throw new IllegalStateException(
                    "The subclass of BaseActivity must contain a toolbar or Appbar.");
        }
        mToolbar.setOnClickListener(v -> onToolbarClick());
        setSupportActionBar(mToolbar);
        if (canBack()) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean canBack() {
        return false;
    }


    protected void hideOrShowToolbar() {
        mAppBar.animate()
                .translationY(mIsHidden ? 0 : -mAppBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        mIsHidden = !mIsHidden;
    }

}
