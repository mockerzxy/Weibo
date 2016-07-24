package com.example.xueyuanzhang.weibo;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyuanzhang.weibo.model.WeiboClass;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import Adapter.ViewPageOfDetailAdapter;
import Fragments.HomeFragment;
import Fragments.MessageFragment;
import Fragments.WriteFragment;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by xueyuanzhang on 16/5/1.
 */
public class Home extends AppCompatActivity {
    SQLiteDatabase db;
    AccountInfo accountInfo;

    private int position=0;
    public final static String ACCESS_TOKEN="arg";
    private SharedPreferences preferences;
    private TabLayout tabLayout;
    private ViewPageOfDetailAdapter adapter;
    private ViewPager viewPager;
    private List<Weibo> temp = new ArrayList<>();
    private HomeFragment homeFragment;
    private WriteFragment writeFragment;
    private MessageFragment messageFragment;
    private List<Fragment> fragmentList;
    private List<String> tabName;
    private CircleImageView circleImageView;
    private TextView showName;
    private ImageView selectPic;
    private ImageView takePic;
    private FloatingActionButton fab;
    private Dialog dialog;
    private Bitmap bitmap;
    private ImageView show;
    private Button sendButton;
    private EditText editText;
    private Toolbar toolbar;
    private ProgressDialog progressDialog;
    private String picPath;
    private String uid;
    private final static int SELECT_PIC = 1;
    private final static int TAKE_PHOTO_KEY = 2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);




        Log.d("TAG","----onCreate----");
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        accountInfo=new AccountInfo();
        preferences=getSharedPreferences("account", MODE_PRIVATE);
        accountInfo.accessToken=preferences.getString("access_token","no_exist");





        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("正在发送,请稍后");
//        showName=(TextView)findViewById(R.id.UserNameInHome);
//        circleImageView=(CircleImageView)findViewById(R.id.headerHome);
        dialog=new Dialog(this,R.style.DialogTheme);
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_write, null);
        selectPic=(ImageView)dialogView.findViewById(R.id.selectPic);
        takePic=(ImageView)dialogView.findViewById(R.id.TakePhoto);
        show=(ImageView)dialogView.findViewById(R.id.show);
        sendButton=(Button)dialogView.findViewById(R.id.sendButton);
        editText=(EditText)dialogView.findViewById(R.id.writeEditBox);

        final DrawerLayout drawerLayout=(DrawerLayout)findViewById(R.id.drawer);
        final NavigationView navigationView=(NavigationView)findViewById(R.id.navigationView);
        final View headerDrawer=navigationView.getHeaderView(0);
        final CircleImageView header=(CircleImageView)headerDrawer.findViewById(R.id.headerOfHeaderLayout);
        final TextView nameDrawer=(TextView)headerDrawer.findViewById(R.id.nameOfHeaderLayout);
        final TextView discDrawer=(TextView)headerDrawer.findViewById(R.id.discOfHeaderLayout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
//        drawerLayout.setDrawerListener(toggle);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                 Window window=getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                Window window=getWindow();
//设置状态栏颜色
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.setting:
                        break;
                    case R.id.showAccount:
                        Intent intent=new Intent(getApplicationContext(),PersonInfoActivity.class);
                        intent.putExtra("uid","");
                        startActivity(intent);
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });





        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        fab=(FloatingActionButton)findViewById(R.id.floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        //***************drawer header中显示用户名
        HttpUtils.getUserInfo(accountInfo.accessToken,null, new HttpUtils.OnGetUserInfoListener() {
            @Override
            public void onUserInfo(final User user) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toolbar.setTitle(user.getScreen_name());
                        Picasso.with(getApplicationContext()).load(user.getHeader()).into(header);
                        nameDrawer.setText(user.getScreen_name());
                        discDrawer.setText(user.getDescription());
//                        showName.setText(user.getScreen_name());
//                        Picasso.with(getApplicationContext()).load(user.getHeader()).into(circleImageView);

                    }
                });
            }
        }, new HttpUtils.OnUnConnectListener() {
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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        selectPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/s/*");
                intent.setAction(intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, SELECT_PIC);
            }
        });

        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String SDstate = Environment.getExternalStorageState();
                if (SDstate.equals(Environment.MEDIA_MOUNTED)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, TAKE_PHOTO_KEY);
                } else {
                    Toast.makeText(getApplicationContext(), "为检测到sd卡,请检查你的sd卡状态!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG",picPath);
                Log.d("TAG",editText.getText().toString());
                progressDialog.show();
                HttpUtils.sendJust(accountInfo.accessToken, editText.getText().toString(), picPath, new HttpUtils.OnUnConnectListener() {
                    @Override
                    public void onUnConnect() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "fail to connect Internet!please check!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }, new HttpUtils.OnOutOfDateListener() {
                    @Override
                    public void onCheck() {
                        progressDialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                }, new HttpUtils.OnSendSuccessListener() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                progressDialog.setMessage("发送成功");
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "发送成功！", Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });
            }
        });
        tabLayout=(TabLayout)findViewById(R.id.homeTabLayout);
        viewPager=(ViewPager)findViewById(R.id.fragmentSwitch);
        fragmentList=new ArrayList<>();
        tabName=new ArrayList<>();
        homeFragment=new HomeFragment();
        messageFragment=new MessageFragment();
        writeFragment=new WriteFragment();

        fragmentList.add(homeFragment);
        fragmentList.add(messageFragment);
        fragmentList.add(writeFragment);

        tabName.add("首页");
        tabName.add("微博");
        tabName.add("热搜");

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(0)));
//        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(1)));
//        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(2)));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.home_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.message_icon));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.earth));
        adapter=new ViewPageOfDetailAdapter(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                toolbar.setTitle(tabName.get(tab.getPosition()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                homeFragment.scroll();
                homeFragment.showProgressBar();
                homeFragment.refresh();
            }
        });
//        tabLayout.setupWithViewPager(viewPager);









    }

    private void insertData(SQLiteDatabase db){}


    public void onStop(){
        super.onStop();
        Log.d("TAG","----onStop----");
    }
    public void onPause(){
        super.onPause();
        Log.d("TAG","----onPause----");
    }
    public void onResume(){
        super.onResume();
        Log.d("TAG","----onResume----");
    }
    public void onDestroy(){
        super.onDestroy();
        Log.d("TAG","----onDestroy----");

    }

    public void showPhotoToken(){
        show.setImageBitmap(bitmap);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
           doPhoto(requestCode, data);
        }

    }

    public void doPhoto(int requestCode, Intent data) {
        if (requestCode == SELECT_PIC) {
            if (data == null) {
                Toast.makeText(this, "选择图片文件失败", Toast.LENGTH_SHORT).show();
            } else {
                Uri photouri = data.getData();
                if (photouri == null) {
                    Toast.makeText(getApplicationContext(), "选择文件图片出错", Toast.LENGTH_SHORT).show();
                } else {
                    String[] po = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.managedQuery(photouri, po, null, null, null);
                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(po[0]);
                        cursor.moveToFirst();
                        picPath = cursor.getString(columnIndex);
                        if (Integer.parseInt(Build.VERSION.SDK) < 14) {
                            cursor.close();
                        }
                    }
                    Log.d("TAG",picPath);
                    File file = new File(picPath);
                    Picasso.with(this).load(file).into(show);
                }

            }

        } else if (requestCode == TAKE_PHOTO_KEY) {
            Toast.makeText(getApplicationContext(),data.getExtras().get("data").toString(),Toast.LENGTH_SHORT).show();
            Log.i("TAKPIC",data.getExtras().get("data").toString());
            bitmap = (Bitmap) data.getExtras().get("data");
        }

    }

    protected void onSaveInstanceState(Bundle bundle){
        Log.d("TAG","2");
        super.onSaveInstanceState(bundle);
            if (bitmap!=null) {
                Bitmap bitmapSave = bitmap;
                bundle.putParcelable("photo", bitmapSave);
            }

    }
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        Log.d("TAG","3");
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState.getParcelable("photo")!=null) {
            bitmap = savedInstanceState.getParcelable("photo");
            showPhotoToken();
            dialog.show();
        }
    }



}
