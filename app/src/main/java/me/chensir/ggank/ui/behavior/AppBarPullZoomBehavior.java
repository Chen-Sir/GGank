package me.chensir.ggank.ui.behavior;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import me.chensir.ggank.R;

/**
 * AppBar下拉放大Behavior，但这个实现方法实际表现非常差劲。暂时弃用。
 * <p>
 * Created by Chensir on 2018/7/4.
 */

@Deprecated
public class AppBarPullZoomBehavior extends AppBarLayout.Behavior {

    private static final float MAX_ZOOM_HEIGHT = 550;  // 下拉放大最大距离
    private static final long ANIMATION_DURATION = 200;

    private View mTargetView; //要下拉放大的View
    private int mAppbarHeight; //记录AppbarLayout原始高度
    private int mTargetViewHeight; //记录TargetView原始高度

    private float mTotalDy; //手指在Y轴下拉滑动的总距离
    private float mScaleValue; //TargetView缩放比例
    private int mLastBottom; //Appbar的变化高度

    private boolean isRecovering = false;//是否正在自动回弹中
    private boolean isAnimate; //是否做动画


    public AppBarPullZoomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean handled = super.onLayoutChild(parent, abl, layoutDirection);
        mTargetView = abl.findViewById(R.id.activity_gank_collapsing_iv);
        if (mTargetView != null) {
            init(abl);
        }
        return handled;
    }

    /**
     * 进行初始化操作，在这里获取到TargetView的引用和Appbar的原始高度
     */
    private void init(AppBarLayout abl) {
        abl.setClipChildren(false);
        mAppbarHeight = abl.getHeight();
        mTargetViewHeight = mTargetView.getHeight();
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        isAnimate = true;
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dx, int dy, int[] consumed, int type) {
        if (mTargetView != null && ((dy < 0 && child.getBottom() >= mAppbarHeight) || (dy > 0 && child.getBottom() > mAppbarHeight))) {
            scaleTargetView(child, target, dy);
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }

        //if (mImageView != null && child.getBottom() >= mAppbarHeight && dy < 0 && type == ViewCompat.TYPE_TOUCH) {
        //    zoomHeaderImageView(child, dy);
        //} else {
        //    if (mImageView != null && child.getBottom() > mAppbarHeight && dy > 0 && type == ViewCompat.TYPE_TOUCH) {
        //        consumed[1] = dy;
        //        zoomHeaderImageView(child, dy);
        //    } else {
        //        if (valueAnimator == null || !valueAnimator.isRunning()) {
        //            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        //        }
        //
        //    }
        //}

    }

    private void scaleTargetView(AppBarLayout abl, View target, int dy) {
        mTotalDy += -dy;
        mTotalDy = Math.min(mTotalDy, MAX_ZOOM_HEIGHT);
        mScaleValue = Math.max(1f, 1f + mTotalDy / MAX_ZOOM_HEIGHT);
        mTargetView.setScaleX(mScaleValue);
        mTargetView.setScaleY(mScaleValue);
        mLastBottom = mAppbarHeight + (int) (mTargetViewHeight / 2 * (mScaleValue - 1));
        abl.setBottom(mLastBottom);
        target.setScrollY(0);
    }

    @Override
    public boolean onNestedPreFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull AppBarLayout child, @NonNull View target, float velocityX, float velocityY) {
        if (velocityY > 0) {
            isAnimate = false;
        }
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        recovery(abl);
        super.onStopNestedScroll(coordinatorLayout, abl, target, type);
    }

    private void recovery(AppBarLayout abl) {
        if (mTotalDy > 0) {
            mTotalDy = 0;
            if (isAnimate) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(mScaleValue, 1f).setDuration(ANIMATION_DURATION);
                valueAnimator.addUpdateListener(animation -> {
                    float value = (float) animation.getAnimatedValue();
                    mTargetView.setScaleX(value);
                    mTargetView.setScaleY(value);
                    abl.setBottom((int) (mLastBottom - (mLastBottom - mAppbarHeight) * animation.getAnimatedFraction()));
                });
                //valueAnimator.addListener(new AnimatorListenerAdapter() {
                //    @Override
                //    public void onAnimationEnd(Animator animation) {
                //        isRecovering = false;
                //    }
                //});
                //isRecovering = true;
                valueAnimator.start();
            } else {
                isRecovering = false;
                mTargetView.setScaleX(1f);
                mTargetView.setScaleY(1f);
                abl.setBottom(mAppbarHeight);
            }
        }
    }
}
