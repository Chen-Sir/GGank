package me.chensir.ggank.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Chensir on 2018/6/22.
 */

public class DayGankData extends BaseData {

    private List<String> category;
    private Result results;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public Result getResults() {
        return results;
    }

    public void setResults(Result results) {
        this.results = results;
    }

    public static class Result {

        /**
         * 就是用中文的！
         */

        @SerializedName("Android")
        private List<Gank> androidList;

        @SerializedName("休息视频")
        private List<Gank> 休息视频List;

        @SerializedName("iOS")
        private List<Gank> iOSList;

        @SerializedName("App")
        private List<Gank> AppList;

        @SerializedName("福利")
        private List<Gank> 妹纸List;

        @SerializedName("拓展资源")
        private List<Gank> 拓展资源List;

        @SerializedName("瞎推荐")
        private List<Gank> 瞎推荐List;

        public List<Gank> getAndroidList() {
            return androidList;
        }

        public void setAndroidList(List<Gank> androidList) {
            this.androidList = androidList;
        }

        public List<Gank> get休息视频List() {
            return 休息视频List;
        }

        public void set休息视频List(List<Gank> 休息视频List) {
            this.休息视频List = 休息视频List;
        }

        public List<Gank> getiOSList() {
            return iOSList;
        }

        public void setiOSList(List<Gank> iOSList) {
            this.iOSList = iOSList;
        }

        public List<Gank> get妹纸List() {
            return 妹纸List;
        }

        public void set妹纸List(List<Gank> 妹纸List) {
            this.妹纸List = 妹纸List;
        }

        public List<Gank> get拓展资源List() {
            return 拓展资源List;
        }

        public void set拓展资源List(List<Gank> 拓展资源List) {
            this.拓展资源List = 拓展资源List;
        }

        public List<Gank> get瞎推荐List() {
            return 瞎推荐List;
        }

        public void set瞎推荐List(List<Gank> 瞎推荐List) {
            this.瞎推荐List = 瞎推荐List;
        }


        public List<Gank> getAppList() {
            return AppList;
        }

        public void setAppList(List<Gank> appList) {
            AppList = appList;
        }
    }


}
