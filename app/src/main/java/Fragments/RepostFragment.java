package Fragments;

//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xueyuanzhang.weibo.AccountInfo;
import com.example.xueyuanzhang.weibo.R;
import com.example.xueyuanzhang.weibo.WeiboDetail;

/**
 * Created by xueyuanzhang on 16/5/15.
 */
public class RepostFragment extends Fragment{

    public AccountInfo accountInfo=new AccountInfo();
    public String weiboId;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(WeiboDetail.ACCESS_TOKEN)&&getArguments().containsKey(WeiboDetail.WEIBO_ID)) {
            accountInfo.accessToken = getArguments().getString(WeiboDetail.ACCESS_TOKEN);
            weiboId=getArguments().getString(WeiboDetail.WEIBO_ID);
        }


    }
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState){
        View view=inflater.inflate(R.layout.repost_fragment,container,false);
        return view;
    }
}
