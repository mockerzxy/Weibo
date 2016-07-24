package Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueyuanzhang.weibo.MyComments;
import com.example.xueyuanzhang.weibo.R;
import com.example.xueyuanzhang.weibo.TimeUtil;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xueyuanzhang on 16/5/22.
 */
public class MyCommentsAdapter extends RecyclerView.Adapter{
    public ImageView header;
    public TextView text;
    public List<MyComments> list=new ArrayList<>();

    public MyCommentsAdapter(List<MyComments> list){
        this.list=list;
    }

    public class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CircleImageView headerOfComment;
        public TextView  nameOfComment;
        public TextView timeOfComment;
        public TextView  textOfComment;
        public ImageView headerOfAuther;
        public TextView nameOfAuther;
        public  TextView textSource;
        public  int position;
        public View v;
        public ListHolder(View view){
            super(view);
            v=view;
            headerOfComment=(CircleImageView) view.findViewById(R.id.myCommentHeader);
            nameOfComment=(TextView)view.findViewById(R.id.myCommentName);
            timeOfComment=(TextView)view.findViewById(R.id.myCommentCreateTime);
            textOfComment=(TextView)view.findViewById(R.id.myCommentText);
            headerOfAuther=(ImageView)view.findViewById(R.id.AutherHeaderInCommentAty);
            nameOfAuther=(TextView)view.findViewById(R.id.AutherNameInCommentAty);
            textSource=(TextView)view.findViewById(R.id.textOfSourceInCommentAty);
            view.setOnClickListener(this);
        }
        public void onClick(View view){

        }
    }
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mycomment_list_item,viewGroup,false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new ListHolder(view);
    }
    public void onBindViewHolder(RecyclerView.ViewHolder holder,int position){
        ListHolder listHolder=(ListHolder)holder;
        MyComments mycomment=list.get(position);
        listHolder.position=position;
        Picasso.with(listHolder.v.getContext()).load(mycomment.getHeaderOfComment()).into(listHolder.headerOfComment);
        listHolder.nameOfComment.setText(mycomment.getNameOfComment());
        listHolder.timeOfComment.setText(TimeUtil.CovertTimeToDescription(mycomment.getTimeOfComment()));
        listHolder.textOfComment.setText(mycomment.getTextOfComment());
        Picasso.with(listHolder.v.getContext()).load(mycomment.getHeaderOfAuther()).into(listHolder.headerOfAuther);
        listHolder.nameOfAuther.setText(mycomment.getNameOfAuther());
        listHolder.textSource.setText(mycomment.getTextOfSource());
    }

    public int getItemCount(){
        return list.size();
    }
}
