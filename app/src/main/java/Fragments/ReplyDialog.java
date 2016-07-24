package Fragments;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xueyuanzhang.weibo.R;

/**
 * Created by xueyuanzhang on 16/6/6.
 */
public class ReplyDialog extends DialogFragment{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View dialogView=inflater.inflate(R.layout.reply_dialog,container,false);
//        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        return dialogView;
    }
}
