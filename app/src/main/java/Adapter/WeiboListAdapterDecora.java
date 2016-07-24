package Adapter;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by xueyuanzhang on 16/5/5.
 */
public class WeiboListAdapterDecora extends RecyclerView.ItemDecoration {
    int space;

    public WeiboListAdapterDecora(int space){
        this.space=space;
    }
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){
        if(parent.getChildPosition(view)!=0){
            outRect.top=space;
        }
    }
}
