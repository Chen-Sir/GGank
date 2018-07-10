package me.chensir.ggank.ui;

import android.os.Handler;

/**
 * 存在于FragmentPagerAdapter中的Fragment，使用懒加载
 * <p>
 * Created by Chensir on 2018/7/2.
 */

public abstract class BasePageFragment extends BaseFragment {

    protected boolean isVisibleToUser;
    protected boolean isDataInitiated; // 是否已加载过数据

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    private void prepareFetchData() {
        if (this.isVisibleToUser && !isDataInitiated) {
            //if (isViewInitiated) {
            //    isDataInitiated = true;
            //    lazyFetchData();
            //} else {
            //}
            /**
             * 这里使用Handler.post()是为了处理这种情况：
             * ViewPager直接跳转到一个未预加载的Fragment页面，此时Fragment生命周期的回调过程：setUserVisibleHint() -->setUserVisibleHint()
             * -->onAttach() --> onCreate()-->onCreateView()--> onActivityCreate() --> onStart()--> onResume()
             * 第二次setUserVisibleHint()传入的参数是true，但此时View还未初始完成。
             */
            new Handler().post(() -> {
                isDataInitiated = true;
                lazyFetchData();
            });
        }

    }

    protected abstract void lazyFetchData();

}
