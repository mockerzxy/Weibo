package com.example.xueyuanzhang.weibo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import Fragments.HomeFragment;

public class MainActivity extends AppCompatActivity {

    WebView authWebView;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences=getSharedPreferences("account",MODE_PRIVATE);
        editor=preferences.edit();
        Toolbar toolbar=(Toolbar)findViewById(R.id.log);
        setSupportActionBar(toolbar);


        authWebView=(WebView)findViewById(R.id.webview);
        authWebView.getSettings().setJavaScriptEnabled(true);
        //authWebView.setWebViewClient(new AuthViewWebClient());
        AuthViewWebClient authViewWebClient=new AuthViewWebClient();
        authViewWebClient.setOnSuccessListener(new onAuthSuccess());
        authWebView.setWebViewClient(authViewWebClient);
        try {
            authWebView.loadUrl("https://api.weibo.com/oauth2/authorize?client_id="+DEVELOP.APP_KEY+"&response_type=code&redirect_uri="+DEVELOP.REDIRECT_URL);
        }catch(Exception e){
        System.out.println("cannnot connect");


        }


    }

    public static class AuthViewWebClient extends WebViewClient{
        public interface onSuccessListener{
           void AuthSuccess(AccountInfo account);
        }
        public AuthViewWebClient() {

        }
        public onSuccessListener onSuccessListener;

        public void setOnSuccessListener(onSuccessListener onSuccessListener){
            this.onSuccessListener=onSuccessListener;
        }
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            if(url.startsWith(DEVELOP.REDIRECT_URL)){

               String code=UrlUtil.getCodeFromUrl(url);
                try {
                    UrlUtil.getAccessToken(code, new UrlUtil.OnAccessTokenListener() {
                        @Override
                        public void onAccessToken(String AccessToken) {
                            System.out.println(AccessToken);
                          AccountInfo account=new AccountInfo();
                            account.accessToken=AccessToken;
                            onSuccessListener.AuthSuccess(account);
                        }
                    });

                    return true;
                }catch (Exception e){
                    System.out.println("error get acccessToken");

                }

            }
            return false;

        }
    }

    public  class onAuthSuccess implements AuthViewWebClient.onSuccessListener{
        public void AuthSuccess(AccountInfo account){
            preferences=getSharedPreferences("account",MODE_PRIVATE);
            editor=preferences.edit();
            editor.putString("access_token",account.accessToken);
            editor.commit();
            Intent intent=new Intent(MainActivity.this,Home.class);
//            intent.putExtra("Account",account.accessToken);
//            System.out.println(account.accessToken);
            startActivity(intent);
        }
    }
}
