package com.example.xueyuanzhang.weibo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.MyCommentsAdapter;
import Adapter.WeiboListAdapterDecora;

/**
 * Created by xueyuanzhang on 16/5/22.
 */
public class CommentMine extends AppCompatActivity{
    public RecyclerView myCommentList;
    public MyCommentsAdapter adapter;
    private SharedPreferences preferences;
    private AccountInfo accountInfo=new AccountInfo();
    public List<MyComments> temp=new ArrayList<>();
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.comment_mine);
        preferences=getSharedPreferences("account",MODE_PRIVATE);
        accountInfo.accessToken=preferences.getString("access_token","no_exist");

        myCommentList=(RecyclerView)findViewById(R.id.myCommentsList);
        adapter=new MyCommentsAdapter(temp);
        myCommentList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        myCommentList.setLayoutManager(layoutManager);
        myCommentList.setAdapter(adapter);
        myCommentList.addItemDecoration(new WeiboListAdapterDecora(16));

        HttpUtils.getMyComments(accountInfo.accessToken, new HttpUtils.OnUnConnectListener() {
            @Override
            public void onUnConnect() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "fail to connect Internet!please check!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }, new HttpUtils.OnOutOfDateListener() {
            @Override
            public void onCheck() {
                Intent intent = new Intent(CommentMine.this, MainActivity.class);
                startActivity(intent);
            }
        }, new HttpUtils.OnMyCommentsListener() {
            @Override
            public void onMyComments(List<MyComments> list) {
                temp.addAll(list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
