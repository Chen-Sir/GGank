package me.chensir.ggank.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;


public class GlideImageLoader implements ImageLoader {

    @Override
    public void loadImage(String url, ImageView imageView) {
        Glide.with(imageView.getContext()).load(url).into(imageView);
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView) {
        Glide.with(context).load(url).into(imageView);
    }

    @Override
    public void loadImage(Context context, int resId, ImageView imageView) {
        Glide.with(context).load(resId).into(imageView);
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView, int placeholder) {
        GlideApp.with(context).load(url).placeholder(placeholder).into(imageView);
    }


    /**
     * 以下两个方法是Glide为了ShareElement达到无缝切换效果所做的处理。
     * <p>
     * 如果使用Picasso图片加载库，由于Picasso缓存的是原图，所以对ShareElement的支持比较好，无需做其他额外操作，直接load图片就可以达到过渡动画无缝切换。
     * 但出于App整体性能和流畅度考虑，最终选择Glide作为图片加载库。
     */


    /**
     * 为ShareElement做准备，缓存原图和处理过后的图片，并且不要Transform
     */
    @Override
    public void loadImagePrepareShareElement(Context context, String url, ImageView imageView, boolean defaultTransition) {
        GlideRequest<Drawable> glideRequest = GlideApp.with(context).load(url).dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL);
        if (defaultTransition) {
            glideRequest.transition(new DrawableTransitionOptions()
                    .crossFade()).into(imageView);
        } else {
            glideRequest.into(imageView);
        }
    }


    /**
     * Glide读取原图，只从缓存中读取，使共享元素无缝切换。在被启动的Activity中使用
     */
    @Override
    public void loadImageForSharingElement(Context context, String url, ImageView imageView, ImageRequestListener imageRequestListener) {

        GlideApp.with(context).load(url).dontTransform().diskCacheStrategy(DiskCacheStrategy.DATA)
                .onlyRetrieveFromCache(true).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                imageRequestListener.onLoadFailed();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                imageRequestListener.onResourceReady();
                return false;
            }
        }).into(imageView);
    }
}
