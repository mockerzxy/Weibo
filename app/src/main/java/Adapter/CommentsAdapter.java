package Adapter;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.xueyuanzhang.weibo.Comments;
import com.example.xueyuanzhang.weibo.R;
import com.example.xueyuanzhang.weibo.TimeUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xueyuanzhang on 16/5/15.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder>{
    public interface OnItemViewClickListener{
        void onResponse();
    }
    public OnItemViewClickListener onItemViewClickListener;
    public List<Comments> list;
    public CommentsAdapter(List<Comments> list){this.list=list;}
    public void setOnItemViewClickListener(OnItemViewClickListener onItemViewClickListener){
        this.onItemViewClickListener=onItemViewClickListener;
    }
    public class CommentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public CircleImageView header;
        public TextView name;
        public TextView time;
        public TextView text;
        public View itemView;
        public int position;
        public CommentHolder(View view){
            super(view);
            itemView=view;
            header=(CircleImageView) view.findViewById(R.id.commentHeader);
            name=(TextView)view.findViewById(R.id.commentName);
            time=(TextView)view.findViewById(R.id.commentCreateTime);
            text=(TextView)view.findViewById(R.id.commentText);
            view.setOnClickListener(this);
        }
        public void onClick(View v){
            onItemViewClickListener.onResponse();
        }
    }
    public CommentHolder onCreateViewHolder(ViewGroup viewGroup,int position){
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_list_item,null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);
        return new CommentHolder(view);
    }

    public void onBindViewHolder(CommentHolder holder,int position){
     CommentHolder commentHolder=holder;
        Comments comments=list.get(position);
        commentHolder.position=position;
        Picasso.with(commentHolder.itemView.getContext()).load(comments.getHeader()).into(commentHolder.header);
        commentHolder.name.setText(comments.getName());
        commentHolder.time.setText(TimeUtil.CovertTimeToDescription(comments.getTime()));
        commentHolder.text.setText(comments.getText());

    }
    public int getItemCount(){
    return  list.size();
    }
}
