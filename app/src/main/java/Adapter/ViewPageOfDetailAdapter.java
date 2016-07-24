package Adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.io.Closeable;
import java.util.List;

import Fragments.CommentFragment;
import Fragments.RepostFragment;


/**
 * Created by xueyuanzhang on 16/5/15.
 */
public class ViewPageOfDetailAdapter extends FragmentPagerAdapter{
    private List<Fragment> list_fragments;



    public ViewPageOfDetailAdapter(FragmentManager fm,List<Fragment> list_fragments) {
        super(fm);
        this.list_fragments=list_fragments;

    }

    @Override
    public Fragment getItem(int position) {
           return list_fragments.get(position);
    }



    @Override
    public int getCount() {
        return list_fragments.size();
    }


//    @Override
//    public CharSequence getPageTitle(int position) {
//
//        return list_Title.get(position);
//    }
}
