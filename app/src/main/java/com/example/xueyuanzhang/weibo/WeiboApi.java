package com.example.xueyuanzhang.weibo;

import com.example.xueyuanzhang.weibo.model.WeiboClass;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by xueyuanzhang on 16/6/24.
 */
public interface WeiboApi {
    @GET("statuses/home_timeline.json")
    Call<WeiboClass> getWeiboClass(@Query("access_token") String access_token);
}
