package com.example.xueyuanzhang.weibo;

import com.example.xueyuanzhang.weibo.model.WeiboClass;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xueyuanzhang on 16/6/24.
 */
public class WeiboService {

    private static WeiboApi instance;

    private static WeiboApi createWeiboApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/2/")
                .addConverterFactory(GsonConverterFactory.create()
                ).build();
        return retrofit.create(WeiboApi.class);


    }

    public static WeiboApi getInstance(){
        if(instance==null){
            synchronized (WeiboService.class){
                if(instance==null){
                    instance = createWeiboApi();
                }
            }
        }
        return instance;
    }
}
