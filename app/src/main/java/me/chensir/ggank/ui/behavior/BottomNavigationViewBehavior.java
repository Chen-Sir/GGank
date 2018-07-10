package me.chensir.ggank.ui.behavior;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Chensir on 2018/5/30.
 */

public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<View> {

    public BottomNavigationViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //得到依赖View的滑动距离
        int top = ((AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) dependency.getLayoutParams()).getBehavior()).getTopAndBottomOffset();
        //因为BottomNavigation的滑动与ToolBar是反向的，所以取-top值
        child.setTranslationY(-top);
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
