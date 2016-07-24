package com.example.xueyuanzhang.weibo;

/**
 * Created by xueyuanzhang on 16/5/2.
 */
public class UrlManage {
    public static String AuthUrl="";
    public static String SendWeiboUrl="";
    public static String ReadFriendWeiboIdUrl="https://api.weibo.com/2/statuses/friends_timeline/ids.json";
    public static String showFriendWeiboUrl="https://api.weibo.com/2/statuses/show.json";
    public static String showHomeWeiboUrl="https://api.weibo.com/2/statuses/home_timeline.json";
    public static String getUserInfoUrl="https://api.weibo.com/2/users/show.json";
    public static String getUid="https://api.weibo.com/2/account/get_uid.json";
    public static String getCommentsUrl="https://api.weibo.com/2/comments/show.json";
    public final static String GET_ACCESS_TOKEN_INFO="https://api.weibo.com/oauth2/get_token_info";
    public final static String SEND_WEIBO_WITH_PIC="https://upload.api.weibo.com/2/statuses/upload.json";
    public final static String GET_COMMENTS_TO_ME="https://api.weibo.com/2/comments/to_me.json";
}
