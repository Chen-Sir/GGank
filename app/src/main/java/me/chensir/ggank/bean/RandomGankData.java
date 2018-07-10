package me.chensir.ggank.bean;

import java.util.List;

/**
 * Created by Chensir on 2018/5/25.
 */

public class RandomGankData extends BaseData{

    private List<Gank> results;

    public List<Gank> getResults() {
        return results;
    }

    public void setResults(List<Gank> results) {
        this.results = results;
    }
}
