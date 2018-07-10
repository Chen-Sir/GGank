package me.chensir.ggank.net;

/**
 * Created by Chensir on 2018/5/25.
 */

public class ServiceFactory {

    private static GankApi sGankApi;

    private static final byte[] LOCK = new byte[0];


    public static GankApi getGankApiSingleton() {

        synchronized (LOCK) {
            if (sGankApi == null) {
                sGankApi = new RetrofitClient(ServerHostConfig.HTTP_GANK_IO_API).getService(GankApi.class);
            }
            return sGankApi;
        }
    }


}
