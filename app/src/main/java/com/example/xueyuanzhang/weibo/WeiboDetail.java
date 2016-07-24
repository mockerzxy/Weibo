package com.example.xueyuanzhang.weibo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import Adapter.ViewPageOfDetailAdapter;
import Fragments.CommentFragment;
import Fragments.PraiseFragment;
import Fragments.RepostFragment;

/**
 * Created by xueyuanzhang on 16/5/14.
 */
public class WeiboDetail extends AppCompatActivity{
    public static final String ACCESS_TOKEN ="token";
    public static final String WEIBO_ID ="id";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView backIcon;
    private ViewPageOfDetailAdapter adapter;
    private List<android.support.v4.app.Fragment> list_fragment;
    private List<String> list_title;
    private RepostFragment repostFragment;
    private CommentFragment commentFragment;
    private PraiseFragment praiseFragment;
    private Toolbar toolbar;
    private AccountInfo accountInfo=new AccountInfo();
    private String weiboID;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weibo_detail);
        Intent intent=getIntent();
        accountInfo.accessToken=intent.getStringExtra("accessToken");
        System.out.println("##################"+accountInfo.accessToken);
        weiboID=intent.getStringExtra("weiboID");

        Log.i(weiboID,"INweiboDetail");
        toolbar=(Toolbar)findViewById(R.id.toolbarInDetail);
        setSupportActionBar(toolbar);
        tabLayout=(TabLayout)findViewById(R.id.tabOfDetailPage);
        viewPager=(ViewPager)findViewById(R.id.viewPageOfDetailPage);

        Bundle arguments=new Bundle();
        arguments.putString(ACCESS_TOKEN,accountInfo.accessToken);
        arguments.putString(WEIBO_ID,weiboID);
        repostFragment=new RepostFragment();
        repostFragment.setArguments(arguments);

        Bundle arguments1=new Bundle();
        arguments1.putString(ACCESS_TOKEN,accountInfo.accessToken);
        arguments1.putString(WEIBO_ID, weiboID);
        commentFragment=new CommentFragment();
        commentFragment.setArguments(arguments1);

        Bundle arguments2=new Bundle();
        arguments2.putString(ACCESS_TOKEN,accountInfo.accessToken);
        arguments2.putString(WEIBO_ID, weiboID);
        praiseFragment=new PraiseFragment();
        praiseFragment.setArguments(arguments2);

        list_fragment=new ArrayList<>();
        list_fragment.add(repostFragment);
        list_fragment.add(commentFragment);
        list_fragment.add(praiseFragment);
        list_title=new ArrayList<>();
        list_title.add("转发");
        list_title.add("评论");
        list_title.add("赞");
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(list_title.get(2)));
        adapter=new ViewPageOfDetailAdapter(getSupportFragmentManager(),list_fragment);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
//        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
               commentFragment.refreshComment();

            }
        });
        backIcon=(ImageView)findViewById(R.id.back_weiboDetail);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
