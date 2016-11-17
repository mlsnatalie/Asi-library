package cn.studyjams.s1.contest.News.main.api;

import org.asi.core.http.HttpResult;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

public interface GankService {

    String BASE_URL = "http://www.gank.io/api/";

    @GET("day/history")
    Observable<HttpResult<List<String>>> getRecentlyDate();

}
