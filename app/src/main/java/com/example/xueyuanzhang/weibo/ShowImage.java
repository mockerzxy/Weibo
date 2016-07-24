package com.example.xueyuanzhang.weibo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xueyuanzhang on 16/5/13.
 */
public class ShowImage extends AppCompatActivity{
    private static final int RIGHT=1;
    private static final int LEFT=0;
    private ImageView imageView;
    private int position;
    private List<String> list=new ArrayList<>();
    private GestureDetector gestureDetector;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image);
        Intent intent = getIntent();
        imageView=(ImageView)findViewById(R.id.showOriginalPic);

        list= Arrays.asList(intent.getStringArrayExtra("pic_url"));
        Log.i("picURL",list.toString());
        position=intent.getIntExtra("nowPosition",0);
        Log.i("position",position+"");
        Picasso.with(this).load(UrlUtil.getOriginal_pic_url(list.get(position))).into(imageView);
        gestureDetector=new GestureDetector(ShowImage.this,new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float x = e2.getX() - e1.getX();
                float y = e2.getY() - e1.getY();

                if (x > 0) {
                    doResult(LEFT);
                } else if (x < 0) {
                    doResult(RIGHT);
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }



    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }


    private void doResult(int result){
        if(result==RIGHT){
            System.out.println("go right");
            if(position<list.size()-1) {
                position=position+1;
                Log.i("position",position+"");
                Picasso.with(this).load(UrlUtil.getOriginal_pic_url(list.get(position))).into(imageView);
            }
        }
        else if(result==LEFT){
            System.out.println("go left");
            if(position>0) {
                position=position-1;
                Log.i("position",position+"");
                Picasso.with(this).load(UrlUtil.getOriginal_pic_url(list.get(position))).into(imageView);
            }
        }
    }
}
