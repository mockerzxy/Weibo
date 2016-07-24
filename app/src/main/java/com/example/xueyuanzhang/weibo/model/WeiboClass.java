package com.example.xueyuanzhang.weibo.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xueyuanzhang on 16/6/24.
 */
public class WeiboClass {
    private List<WeiboData> statuses;

    public List<WeiboData> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<WeiboData> statuses) {
        this.statuses = statuses;
    }
}
