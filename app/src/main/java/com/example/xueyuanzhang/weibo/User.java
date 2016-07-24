package com.example.xueyuanzhang.weibo;

/**
 * Created by xueyuanzhang on 16/5/13.
 */
public class User {
    private String screen_name;
    private String followers_count;
    private String friends_count;
    private String statuses_count;
    private String description;
    private String header;

    public void setScreen_name(String screen_name){this.screen_name=screen_name;}
    public void setFollowers_count(String followers_count){this.followers_count=followers_count;}
    public void setFriends_count(String friends_count){this.friends_count=friends_count;}
    public void setDescription(String description){this.description=description;}
    public void setStatuses_count(String statuses_count){this.statuses_count=statuses_count;}
    public void setHeader(String header){this.header=header;}
    public String getScreen_name(){return this.screen_name;}
    public String getFollowers_count(){return this.followers_count;}
    public String getFriends_count(){return this.friends_count;}
    public String getStatuses_count(){return this.statuses_count;}
    public String getHeader(){return this.header;}
    public String getDescription(){return this.description;}
}
