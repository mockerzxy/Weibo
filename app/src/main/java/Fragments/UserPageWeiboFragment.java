package Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xueyuanzhang.weibo.HttpUtils;
import com.example.xueyuanzhang.weibo.ListEntry;
import com.example.xueyuanzhang.weibo.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.MyWeiboAdapter;

/**
 * Created by xueyuanzhang on 16/6/4.
 */
public class UserPageWeiboFragment extends Fragment{
    private RecyclerView weiboList;
    private MyWeiboAdapter weiboListAdapter;
    private List<ListEntry> weiboDataList=new ArrayList<>();
    private SharedPreferences preferences;
    private String accessToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences=getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        accessToken=preferences.getString("access_token","no_exist");
        Log.i("TQQ",accessToken);
    }

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_page_weibo_fragment,container,false);
        initRecyclerView(view);
        getWeiboInfo();
        return view;
    }
    private void initRecyclerView(View view ){
        weiboList=(RecyclerView)view.findViewById(R.id.myWeiboList);
        weiboListAdapter=new MyWeiboAdapter(getContext(),weiboDataList);
        weiboList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        weiboList.setLayoutManager(layoutManager);
        weiboList.setAdapter(weiboListAdapter);
    }
    private void getWeiboInfo(){
        HttpUtils.readFrendsWeibo(accessToken, null, new HttpUtils.OnOutOfDateListener() {
            @Override
            public void onCheck() {

            }
        }, new HttpUtils.OnUnConnectListener() {
            @Override
            public void onUnConnect() {

            }
        }, new HttpUtils.OnShowWeiboListener() {
            @Override
            public void onFriendWeibo(final List<ListEntry> weibo) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        weiboDataList.clear();
                        weiboDataList.addAll(weibo);
                        weiboListAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }
}
