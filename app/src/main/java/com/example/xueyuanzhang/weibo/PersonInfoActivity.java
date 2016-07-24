package com.example.xueyuanzhang.weibo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

import Adapter.MyWeiboAdapter;
import Adapter.ViewPageOfDetailAdapter;
import Fragments.UserPageHomeFragment;
import Fragments.UserPagePicFragment;
import Fragments.UserPageWeiboFragment;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xueyuanzhang on 16/6/3.
 */
public class PersonInfoActivity extends AppCompatActivity{
    public String uid;
    private String accessToken;
    private SharedPreferences preferences;
    private CircleImageView circleImageView;
    private TextView name;
    private TextView nameInToolbar;
    private Toolbar toolbar;
    private String userName;
    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<String> titleList;
    private List<Fragment> fragmentList;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private UserPageWeiboFragment weiboFragment;
    private UserPageHomeFragment homeFragment;
    private UserPagePicFragment picFragment;
    private ViewPageOfDetailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_person_info);
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");
        Log.i("USERID",uid);
        if(uid.equals("")){
            uid=null;
        }
        preferences=getSharedPreferences("account",MODE_PRIVATE);
        accessToken=preferences.getString("access_token","no_exist");
        initView();
        getInfo();
        initTabLayout();

    }
    private void getInfo(){
        HttpUtils.getUserInfo(accessToken, uid, new HttpUtils.OnGetUserInfoListener() {
            @Override
            public void onUserInfo(final User user) {
                userName=user.getScreen_name();
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       Picasso.with(getApplicationContext()).load(user.getHeader()).into(circleImageView);
                       name.setText(user.getScreen_name());
                   }
               });
            }
        }, new HttpUtils.OnUnConnectListener() {
            @Override
            public void onUnConnect() {
                Toast.makeText(getApplicationContext(),"fail to connect Internet!please check!",Toast.LENGTH_SHORT).show();
            }
        }, new HttpUtils.OnOutOfDateListener() {
            @Override
            public void onCheck() {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        circleImageView=(CircleImageView)findViewById(R.id.headerOfUserPage);
        name=(TextView)findViewById(R.id.nameOfUserPage);
        toolbar=(Toolbar)findViewById(R.id.toolbarInUserPage);
//        nameInToolbar=(TextView)findViewById(R.id.userName);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapseLayout);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24dp);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPageOfUserPage);

        appBarLayout=(AppBarLayout)findViewById(R.id.appBarLayout);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(appBarLayout.getTotalScrollRange()==Math.abs(verticalOffset)){
//                    nameInToolbar.setText(userName);
                    collapsingToolbarLayout.setTitle(userName);
                    collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.white));


                }else {
//                    nameInToolbar.setText("");
                }
            }

        });

    }

    private void initTabLayout(){
        fragmentList=new ArrayList<>();
        titleList=new ArrayList<>();
        weiboFragment =new UserPageWeiboFragment();
        homeFragment =new UserPageHomeFragment();
        picFragment=new UserPagePicFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(weiboFragment);
        fragmentList.add(picFragment);
        titleList.add("主页");
        titleList.add("微博");
        titleList.add("相册");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titleList.get(2)));
        adapter=new ViewPageOfDetailAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
//        tabLayout.setupWithViewPager(viewPager);
    }

}
