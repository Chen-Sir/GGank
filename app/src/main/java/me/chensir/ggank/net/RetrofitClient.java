package me.chensir.ggank.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import me.chensir.ggank.BuildConfig;
import me.chensir.ggank.util.Constants;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Chensir on 2018/5/25.
 */

public class RetrofitClient {

    private Retrofit mRetrofit;

    final static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .serializeNulls()
            .create();

    RetrofitClient(String baseUrl) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }
        httpClient.connectTimeout(Constants.HTTP_TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient okHttpClient = httpClient.build();

        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(baseUrl)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson));
        mRetrofit  =  builder.build();
    }

    public <T> T getService(Class<T> tClass){
        return mRetrofit.create(tClass);
    }

}
