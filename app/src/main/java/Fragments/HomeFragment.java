package Fragments;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyuanzhang.weibo.AccountInfo;
import com.example.xueyuanzhang.weibo.HttpUtils;
import com.example.xueyuanzhang.weibo.ListEntry;
import com.example.xueyuanzhang.weibo.MainActivity;
import com.example.xueyuanzhang.weibo.PersonInfoActivity;
import com.example.xueyuanzhang.weibo.R;
import com.example.xueyuanzhang.weibo.ShowImage;
import com.example.xueyuanzhang.weibo.WeiboDetail;

import java.util.ArrayList;
import java.util.List;

import Adapter.WeiboListAdapter;

/**
 * Created by xueyuanzhang on 16/5/7.
 */
public class HomeFragment extends Fragment {
    public interface OnSettingListener {
        void onInitToolbar(Toolbar toolbar);
    }



    SQLiteDatabase db;

    public OnSettingListener onSettingListener;
    AccountInfo accountInfo = new AccountInfo();
    Toolbar toolbar;
    RecyclerView weiboList;
    TextView userNameInHome;
    WeiboListAdapter adapter;
    public ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences preferences;
    private ReplyDialog replyDialog;
    public List<ListEntry> temp = new ArrayList<>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        accountInfo.accessToken = preferences.getString("access_token", "no_exist");
        replyDialog=new ReplyDialog();

    }

    public void setOnSetttingListener(OnSettingListener onSettingListener) {       //设置toolbar回调
        this.onSettingListener = onSettingListener;
    }



    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar_home);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                HttpUtils.readFrendsWeibo(accountInfo.accessToken, null, new HttpUtils.OnOutOfDateListener() {
                    @Override
                    public void onCheck() {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }, new HttpUtils.OnUnConnectListener() {
                    @Override
                    public void onUnConnect() {
                        Toast.makeText(rootView.getContext(), "fail to connect Internet!please check!", Toast.LENGTH_SHORT).show();
                    }
                }, new HttpUtils.OnShowWeiboListener() {
                    @Override
                    public void onFriendWeibo(List<ListEntry> weibo) {
                        System.out.println(weibo.size());
                        temp.clear();
                        temp.addAll(weibo);
                        Log.i("temp size", String.valueOf(temp.size()));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                swipeRefreshLayout.setRefreshing(false);
                            }
                        });
                    }
                });


            }
        });



        weiboList = (RecyclerView) rootView.findViewById(R.id.WeiboList);
        adapter = new WeiboListAdapter(temp);
        weiboList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);
        weiboList.setLayoutManager(layoutManager);
        weiboList.setAdapter(adapter);
//       weiboList.addItemDecoration(new WeiboListAdapterDecora(3));
        //显示大图
        adapter.setOnPicClickListener(new WeiboListAdapter.OnPicClickListener() {
            @Override
            public void onPicClick(List<String> pic_url, int picPosition, ImageView imageView) {
                Log.i("picPo", picPosition + "");
                Intent intent = new Intent(getActivity(), ShowImage.class);
                intent.putExtra("pic_url", pic_url.toArray(new String[pic_url.size()]));
                if (pic_url.toArray(new String[pic_url.size()]) == null) {
                    Log.i("picURL", "kong");
                } else {
                    Log.i("picURL", pic_url.toArray(new String[pic_url.size()])[picPosition]);

                }
                intent.putExtra("nowPosition", picPosition);

                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity(),imageView,"image");
                startActivity(intent,activityOptions.toBundle());
            }
        });
        //进入微博详情页
        adapter.setOnRecyclerViewHolder(new WeiboListAdapter.onRecyclerViewHolder() {
            @Override
            public void onItemClick(int position, String weiboID) {
                Toast.makeText(rootView.getContext(), "need to show detail!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), WeiboDetail.class);
                intent.putExtra("id", position);
                intent.putExtra("accessToken", accountInfo.accessToken);
                intent.putExtra("weiboID", weiboID);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());

            }
        });
        //加载更多
        adapter.setOnLoadMoreListener(new WeiboListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore(String weiboID) {
                HttpUtils.readFrendsWeibo(accountInfo.accessToken, weiboID, new HttpUtils.OnOutOfDateListener() {
                    @Override
                    public void onCheck() {
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);
                    }
                }, new HttpUtils.OnUnConnectListener() {
                    @Override
                    public void onUnConnect() {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(rootView.getContext(), "fail to connect Internet!please check!", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }, new HttpUtils.OnShowWeiboListener() {
                    @Override
                    public void onFriendWeibo(List<ListEntry> weibo) {

                        temp.remove(temp.size() - 1);
                        temp.addAll(weibo);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });
        adapter.setOnJumpUserPageListener(new WeiboListAdapter.OnJumpUserPageListener() {
            @Override
            public void onJump(String uid) {
                Intent intent =new Intent(getActivity(), PersonInfoActivity.class);
                intent.putExtra("uid",uid);
                startActivity(intent);
            }
        });

        adapter.setOnShowReplyDialogListener(new WeiboListAdapter.OnShowReplyDialogListener() {

            @Override
            public void onShow() {
                replyDialog.show(getActivity().getFragmentManager(),"dialog");
            }
        });


        //********************回调首页list显示列表
        showHome();

        return rootView;


    }

    public void refresh() {
//        if (onRefreshListener != null) {
//            onRefreshListener.onRefresh(temp, adapter);
//        }
        HttpUtils.readFrendsWeibo(accountInfo.accessToken, null, new HttpUtils.OnOutOfDateListener() {
            @Override
            public void onCheck() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }, new HttpUtils.OnUnConnectListener() {
            @Override
            public void onUnConnect() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "fail to connect Internet!please check!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, new HttpUtils.OnShowWeiboListener() {
            @Override
            public void onFriendWeibo(List<ListEntry> weibo) {
                System.out.println(weibo.size());
                temp.clear();
                temp.addAll(weibo);
                Log.i("temp size", String.valueOf(temp.size()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                        hideProgressBar();
                    }
                });
            }
        });

    }

    public void scroll() {
        weiboList.scrollToPosition(0);
    }

    public void showHome() {
        HttpUtils.readFrendsWeibo(accountInfo.accessToken, null, new HttpUtils.OnOutOfDateListener() {
            @Override
            public void onCheck() {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        }, new HttpUtils.OnUnConnectListener() {
            @Override
            public void onUnConnect() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "fail to connect Internet!please check!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }, new HttpUtils.OnShowWeiboListener() {
            @Override
            public void onFriendWeibo(List<ListEntry> weibo) {
                System.out.println(weibo.size());
                temp.clear();
                temp.addAll(weibo);
                Log.i("temp size", String.valueOf(temp.size()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }


}
