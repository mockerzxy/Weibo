package Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.xueyuanzhang.weibo.AccountInfo;
import com.example.xueyuanzhang.weibo.CommentMine;
import com.example.xueyuanzhang.weibo.Home;
import com.example.xueyuanzhang.weibo.R;

import Adapter.MyCommentsAdapter;

/**
 * Created by xueyuanzhang on 16/5/22.
 */
public class MessageFragment extends Fragment{
    private AccountInfo accountInfo=new AccountInfo();
    public LinearLayout commentView;
    private SharedPreferences preferences;

    public void onCreate(Bundle saveInstanceState){
       super.onCreate(saveInstanceState);
        preferences=getActivity().getSharedPreferences("account", Context.MODE_PRIVATE);
        accountInfo.accessToken=preferences.getString("access_token","no_exist");
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
        View rootView=inflater.inflate(R.layout.message_fragment,container,false);
        commentView=(LinearLayout)rootView.findViewById(R.id.showComments);

        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), CommentMine.class);
                startActivity(intent);
            }
        });
        return rootView;
    }
}
