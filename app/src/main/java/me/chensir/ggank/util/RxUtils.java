package me.chensir.ggank.util;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

;

public class RxUtils {

    /**
     * 请求在io 线程
     * 响应在 UI线程
     * observable.compose(RxJavaUtils.defaultSchedulers())
     */
    public static <T> ObservableTransformer<T, T> defaultSchedulers() {
        return tObservable -> tObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
