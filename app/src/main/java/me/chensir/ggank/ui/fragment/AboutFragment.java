package me.chensir.ggank.ui.fragment;

import me.chensir.ggank.R;
import me.chensir.ggank.ui.BasePageFragment;


public class AboutFragment extends BasePageFragment {


    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AboutFragment.
     */
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Override
    protected int provideLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    protected void lazyFetchData() {
        // do nothing.
    }

}
