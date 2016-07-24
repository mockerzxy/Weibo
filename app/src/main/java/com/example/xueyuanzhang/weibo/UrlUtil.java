package com.example.xueyuanzhang.weibo;


import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xueyuanzhang on 16/5/1.
 */
public class UrlUtil {

    public interface OnAccessTokenListener {
        void onAccessToken(String accessToken);
    }

    public static String getCodeFromUrl(String url){
        int positionTemp=url.indexOf('?');
        int positionStart=positionTemp+1;
        String Code=url.substring(positionStart);
        return Code;
    }
    public static void getAccessToken(String Code, final OnAccessTokenListener onAccessTokenListener) {

        String url="https://api.weibo.com/oauth2/access_token?client_id="+DEVELOP.APP_KEY+"&client_secret="+DEVELOP.CLIENT_SECRET
                +"&grant_type=authorization_code&redirect_uri="+DEVELOP.REDIRECT_URL+"&"+Code;
        HttpUtils.getAccessToken(url, new HttpUtils.OnResponseListener() {
            @Override
            public void onResponse(String JsonResult) {
                String AccessToken=null;

                try {
                    JSONObject jsonObject = new JSONObject(JsonResult);
                    AccessToken = jsonObject.get("access_token").toString();
                }catch (JSONException e){}

                if (onAccessTokenListener != null) {
                    onAccessTokenListener.onAccessToken(AccessToken);
                }

        }


        });

    }
    public static String getOriginal_pic_url(String url){
        String original_pic_url=url.replace("thumbnail","large");
        Log.i(original_pic_url,"InUrlUtil");
        return original_pic_url;
    }
}
