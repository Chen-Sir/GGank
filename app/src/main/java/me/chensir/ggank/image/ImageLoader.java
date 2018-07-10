package me.chensir.ggank.image;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

public interface ImageLoader {

    void loadImage(String url, ImageView imageView);

    void loadImage(Context context, String url, ImageView imageView);

    void loadImage(Context context, @DrawableRes int resId, ImageView imageView);

    void loadImage(Context context, String url, ImageView imageView, @DrawableRes int placeholder);

    void loadImagePrepareShareElement(Context context, String url, ImageView imageView, boolean defaultTransition);

    void loadImageForSharingElement(Context context, String url, ImageView imageView, ImageRequestListener imageRequestListener);


    interface ImageRequestListener {

        void onLoadFailed();

        void onResourceReady();

    }

}
