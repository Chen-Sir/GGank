package me.chensir.ggank.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;

import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import me.chensir.ggank.R;
import me.chensir.ggank.image.ImageLoader;
import me.chensir.ggank.image.ImageLoaderUtil;
import me.chensir.ggank.ui.BaseActivity;
import me.chensir.ggank.util.Constants;

public class PictureActivity extends BaseActivity {

    private static final String EXTRA_ARG_PIC_URL = "extra_pic_url";
    public static final String TRANSIT_MEIZI = "meizi";

    @BindView(R.id.activity_pic_iv)
    PhotoView mImageView;


    public static Intent getCallingIntent(Context context, String url) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra(EXTRA_ARG_PIC_URL, url);
        return intent;
    }

    @Override
    protected int provideContentViewResId() {
        return R.layout.activity_picture;
    }

    @Override
    public boolean canBack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(Constants.FLAG_MEIZI);
        String url = getIntent().getStringExtra(EXTRA_ARG_PIC_URL);
        ViewCompat.setTransitionName(mImageView, TRANSIT_MEIZI);
        supportPostponeEnterTransition();
        ImageLoaderUtil.get().loadImageForSharingElement(this, url, mImageView, new ImageLoader.ImageRequestListener() {
            @Override
            public void onLoadFailed() {
                supportStartPostponedEnterTransition();
            }

            @Override
            public void onResourceReady() {
                supportStartPostponedEnterTransition();
                mImageView.setOnPhotoTapListener((view, x, y) -> hideOrShowToolbar());
            }
        });
    }

}
