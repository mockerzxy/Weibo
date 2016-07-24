package Fragments;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xueyuanzhang.weibo.AccountInfo;
import com.example.xueyuanzhang.weibo.Comments;
import com.example.xueyuanzhang.weibo.HttpUtils;
import com.example.xueyuanzhang.weibo.R;
import com.example.xueyuanzhang.weibo.WeiboDetail;

import java.util.ArrayList;
import java.util.List;

import Adapter.CommentsAdapter;
import Adapter.WeiboListAdapterDecora;

/**
 * Created by xueyuanzhang on 16/5/15.
 */
public class CommentFragment extends Fragment{

    private String weiboId;
    private AccountInfo accountInfo=new AccountInfo();
    final List<Comments> list=new ArrayList<>();
    final CommentsAdapter adapter=new CommentsAdapter(list);
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(WeiboDetail.ACCESS_TOKEN)&&getArguments().containsKey(WeiboDetail.WEIBO_ID)) {
            accountInfo.accessToken = getArguments().getString(WeiboDetail.ACCESS_TOKEN);
            weiboId=getArguments().getString(WeiboDetail.WEIBO_ID);
            System.out.println(accountInfo.accessToken+"///////////////-------------");
           System.out.println(weiboId+"*******************!!!!!!!!!");
        }


    }
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
        View view=inflater.inflate(R.layout.comment_fragment,container,false);
        RecyclerView commentList=(RecyclerView)view.findViewById(R.id.commentList);

        commentList.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false);
        commentList.setLayoutManager(layoutManager);
        commentList.setAdapter(adapter);
        commentList.addItemDecoration(new WeiboListAdapterDecora(5));
        HttpUtils.getComments(accountInfo.accessToken, weiboId, new HttpUtils.OnGetCommentsListener() {
            @Override
            public void onComments(List<Comments> comments) {
                System.out.println("数组大小" + comments.size());
                list.clear();
                list.addAll(comments);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        return view;
    }
    public void refreshComment(){
        HttpUtils.getComments(accountInfo.accessToken, weiboId, new HttpUtils.OnGetCommentsListener() {
            @Override
            public void onComments(List<Comments> comments) {
                System.out.println("数组大小"+comments.size());
                list.clear();
                list.addAll(comments);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }
}
