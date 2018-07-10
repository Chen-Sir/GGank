package me.chensir.ggank.net;

import io.reactivex.Observable;
import me.chensir.ggank.bean.DayGankData;
import me.chensir.ggank.bean.MeiZiData;
import me.chensir.ggank.bean.RandomGankData;
import me.chensir.ggank.util.Constants;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Chensir on 2018/5/25.
 */

public interface GankApi {

    @GET("data/all/" + Constants.PAGE_SIZE + "/{page}")
    Observable<RandomGankData> getGankData(@Path("page") int page);

    @GET("data/福利/" + Constants.PAGE_SIZE + "/{page}")
    Observable<MeiZiData> getMeiZiData(@Path("page") int page);

    @GET("day/{year}/{month}/{day}")
    Observable<DayGankData> getDayGankData(@Path("year") int year, @Path("month") int month, @Path("day") int day);

}
