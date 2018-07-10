package me.chensir.ggank.image;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

public class ImageLoaderUtil {

    private ImageLoader mImageLoader;

    private static volatile ImageLoaderUtil mInstance;

    private ImageLoaderUtil() {
        mImageLoader = new GlideImageLoader();
    }

    public static ImageLoaderUtil get() {
        if (mInstance == null) {
            synchronized (ImageLoaderUtil.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoaderUtil();
                }
            }
        }
        return mInstance;
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public void loadImage(String url, ImageView imageView) {
        mImageLoader.loadImage(url, imageView);
    }

    public void loadImage(Context context, String url, ImageView imageView) {
        mImageLoader.loadImage(context, url, imageView);
    }

    public void loadImage(Context context, @DrawableRes int resId, ImageView imageView) {
        mImageLoader.loadImage(context, resId, imageView);
    }

    public void loadImage(Context context, String url, ImageView imageView, @DrawableRes int placeholder) {
        mImageLoader.loadImage(context, url, imageView, placeholder);
    }

    public void loadImagePrepareShareElement(Context context, String url, ImageView imageView, boolean defaultTransition) {
        mImageLoader.loadImagePrepareShareElement(context, url, imageView, defaultTransition);
    }

    public void loadImageForSharingElement(Context context, String url, ImageView imageView, ImageLoader.ImageRequestListener imageRequestListener) {
        mImageLoader.loadImageForSharingElement(context, url, imageView, imageRequestListener);
    }


}
