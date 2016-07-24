package Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xueyuanzhang.weibo.ListEntry;
import com.example.xueyuanzhang.weibo.R;
import com.example.xueyuanzhang.weibo.TextUtil;
import com.example.xueyuanzhang.weibo.TimeUtil;
import com.example.xueyuanzhang.weibo.Weibo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xueyuanzhang on 16/6/5.
 */
public class MyWeiboAdapter extends RecyclerView.Adapter {
    private List<ListEntry> list;
    private Context context;

    public MyWeiboAdapter(Context context, List<ListEntry> list) {
        this.list = list;
        this.context = context;
    }

    private class Itemholder extends RecyclerView.ViewHolder {
        private CircleImageView header;
        private TextView createTime;
        private TextView name;
        private TextView textContent;
        private ViewStub imageStub;
        private ViewStub sourceStub;
        private View contentView;

        public Itemholder(View itemView) {
            super(itemView);
            contentView = itemView;
            header = (CircleImageView) itemView.findViewById(R.id.header);
            createTime = (TextView) itemView.findViewById(R.id.CreateTime);
            name = (TextView) itemView.findViewById(R.id.name);
            textContent = (TextView) itemView.findViewById(R.id.textContent);
            imageStub = (ViewStub) itemView.findViewById(R.id.imageViewStub);
            sourceStub = (ViewStub) itemView.findViewById(R.id.SourceWeiboStub);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_weibo_list_item, parent, false);
        return new Itemholder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Itemholder itemholder = (Itemholder) holder;
        Weibo weibo = (Weibo) list.get(position);
        Picasso.with(itemholder.contentView.getContext()).load(weibo.getHeader()).into(itemholder.header);
        itemholder.name.setText(weibo.getUserName());
        itemholder.createTime.setText(TimeUtil.CovertTimeToDescription(weibo.getCreateTime()));
        if (weibo.getWeiboContent() != null) {
            itemholder.textContent.setText(TextUtil.addLinkToTopic(weibo.getWeiboContent()));
        } else {
            itemholder.textContent.setText("转发微博");
        }
        if (weibo.getImage().size() != 0) {
            loadPic(itemholder, weibo);
        } else {
            if (itemholder.contentView.findViewById(R.id.imageInflateId) != null) {
                itemholder.contentView.findViewById(R.id.imageInflateId).setVisibility(View.GONE);
            }
        }
        if (weibo.getSource_userName().length() != 0) {
            loadSourceWeibo(itemholder, weibo);
        } else {
            if (itemholder.contentView.findViewById(R.id.sourceWeiboInflatedId) != null) {
                itemholder.contentView.findViewById(R.id.sourceWeiboInflatedId).setVisibility(View.GONE);
            }
        }

    }

    private void loadPic(Itemholder itemholder, Weibo weibo) {
        View imageStubView;
        if (itemholder.contentView.findViewById(R.id.imageInflateId) != null) {
            imageStubView = itemholder.contentView.findViewById(R.id.imageInflateId);
            imageStubView.setVisibility(View.VISIBLE);
        } else {
            imageStubView = itemholder.imageStub.inflate();
        }
        GridLayout gridLayout = (GridLayout) imageStubView.findViewById(R.id.gridLayout);
        gridLayout.removeAllViewsInLayout();
        if(weibo.getImage().size()==1){
            ImageView imageView=(ImageView)LayoutInflater.from(context).inflate(R.layout.image_view_grid,gridLayout,false);
            Picasso.with(context).load(weibo.getBmiddle_pic()).into(imageView);
            gridLayout.addView(imageView);
        }else {
            for (int i = 0; i < weibo.getImage().size(); i++) {
                ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.image_view_grid, gridLayout, false);
                Picasso.with(context).load(weibo.getImage().get(i)).resize(300, 300).centerCrop().into(imageView);
                gridLayout.addView(imageView);
            }
        }
    }

    private void loadSourceWeibo(Itemholder itemholder, Weibo weibo) {
        View sourceStubView;
        if (itemholder.contentView.findViewById(R.id.sourceWeiboInflatedId) != null) {
            sourceStubView = itemholder.contentView.findViewById(R.id.sourceWeiboInflatedId);
            sourceStubView.setVisibility(View.VISIBLE);
        } else {
            sourceStubView = itemholder.sourceStub.inflate();
        }
        TextView textView = (TextView) sourceStubView.findViewById(R.id.textContentOfSource);
        GridLayout gridLayout = (GridLayout) sourceStubView.findViewById(R.id.gridLayout);
        gridLayout.removeAllViewsInLayout();
        if(weibo.getSourceImageUrl().size()==0){
            gridLayout.setVisibility(View.GONE);
        }
        else {
            gridLayout.setVisibility(View.VISIBLE);
        }
        if(weibo.getSourceImageUrl().size()==1){
            ImageView imageView=(ImageView)LayoutInflater.from(context).inflate(R.layout.image_view_grid,gridLayout,false);
            Picasso.with(context).load(weibo.getSource_bmiddle_pic()).into(imageView);
            gridLayout.addView(imageView);
        }else {
            for (int i = 0; i < weibo.getSourceImageUrl().size(); i++) {
                ImageView imageView = (ImageView) LayoutInflater.from(context).inflate(R.layout.image_view_grid, gridLayout, false);
                Picasso.with(context).load(weibo.getSourceImageUrl().get(i)).resize(300, 300).centerCrop().into(imageView);
                gridLayout.addView(imageView);
            }
        }

        textView.setText(TextUtil.setSourceAuthorSpan(TextUtil.addLinkToTopic("@" + weibo.getSource_userName() + ":" + weibo.getSource_text()), weibo.getSource_userName()));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
}
