package Adapter;

import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyuanzhang.weibo.ClickableSpanWithoutUnderline;
import com.example.xueyuanzhang.weibo.ListEntry;
import com.example.xueyuanzhang.weibo.LoadMoreModel;
import com.example.xueyuanzhang.weibo.R;
import com.example.xueyuanzhang.weibo.ShowImage;
import com.example.xueyuanzhang.weibo.TextUtil;
import com.example.xueyuanzhang.weibo.TimeUtil;
import com.example.xueyuanzhang.weibo.UrlUtil;
import com.example.xueyuanzhang.weibo.Weibo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by xueyuanzhang on 16/5/5.
 */
public class WeiboListAdapter extends RecyclerView.Adapter {
    public  interface onRecyclerViewHolder {
        void onItemClick(int position, String weiboID);
    }

    public  interface OnPicClickListener {
        void onPicClick(List<String> pic_url,int picPosition,ImageView view);
    }

    public interface OnLoadMoreListener {
        void onLoadMore(String weiboID);
    }

    public interface OnJumpUserPageListener{
        void onJump(String uid);
    }

    public interface OnShowReplyDialogListener{
        void onShow();
    }

    private final static int TYPE_WEIBO = 0;
    private final static int TYPE_LOAD_MORE = 1;

    public onRecyclerViewHolder onRecyclerViewHolder;
    public OnPicClickListener onPicClickListener;
    public OnLoadMoreListener onLoadMoreListener;
    public OnJumpUserPageListener onJumpUserPageListener;
    public OnShowReplyDialogListener onShowReplyDialogListener;

    public void setOnRecyclerViewHolder(onRecyclerViewHolder onRecyclerViewHolder) {
        this.onRecyclerViewHolder = onRecyclerViewHolder;
    }

    public void setOnPicClickListener(OnPicClickListener onPicClickListener) {
        this.onPicClickListener = onPicClickListener;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnJumpUserPageListener(OnJumpUserPageListener onJumpUserPageListener) {
        this.onJumpUserPageListener = onJumpUserPageListener;
    }

    public void setOnShowReplyDialogListener(OnShowReplyDialogListener onShowReplyDialogListener) {
        this.onShowReplyDialogListener = onShowReplyDialogListener;
    }

    private List<ListEntry> list;
    private int bmiddleImageWidth = 500;
    private int bmiddleImageHeight = 500;
    private int thumbnailImageWidth = 300;
    private int thumbnailImageheight = 300;

    public WeiboListAdapter(List<ListEntry> list) {
        this.list = list;
    }

    public class listItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public CircleImageView header;
        public TextView name;
        public TextView content;
        public TextView repost;
        public TextView comment;
        public TextView praise;
        public ViewStub ImageViewStub;
        public ViewStub sourceViewStub;
        public TextView createTime;


        public int position;
        public String weiboID;
        public View v;

        public listItemHolder(View view) {
            super(view);
            v = view;
            header = (CircleImageView) view.findViewById(R.id.HeaderOfList);
            name = (TextView) view.findViewById(R.id.NameOfList);
            content = (TextView) view.findViewById(R.id.contentOfList);
            repost = (TextView) view.findViewById(R.id.repost);
            comment = (TextView) view.findViewById(R.id.comment);
            praise = (TextView) view.findViewById(R.id.praise);
            ImageViewStub = (ViewStub) view.findViewById(R.id.imageViewStub);
            sourceViewStub = (ViewStub) view.findViewById(R.id.SourceWeiboStub);
            createTime = (TextView) view.findViewById(R.id.CreateTime);
            view.setOnClickListener(this);
            comment.setOnClickListener(this);
        }

        public void onClick(View view) {
            if(view==comment){
                onShowReplyDialogListener.onShow();
            }else {
                onRecyclerViewHolder.onItemClick(position, weiboID);
            }
        }
    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public String weiboID;
        public int position;
        public TextView textView;
        public ProgressBar progressBar;
        public View v;
        public LoadMoreHolder(View view) {
            super(view);
            v=view;
            textView = (TextView) view.findViewById(R.id.loadMore);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar_loadMore);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            textView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            onLoadMoreListener.onLoadMore(weiboID);
        }
    }

    @Override
    public int getItemViewType(int position) {
        ListEntry entry = list.get(position);
        if (entry instanceof LoadMoreModel) {
            return TYPE_LOAD_MORE;
        } else {
            return TYPE_WEIBO;
        }
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        if (TYPE_WEIBO == viewType) {
            View view = inflater.inflate(R.layout.weibo_list_item, viewGroup, false);
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//            view.setLayoutParams(lp);
            return new listItemHolder(view);
        } else {

            View view = inflater.inflate(R.layout.load_more_item, viewGroup, false);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new LoadMoreHolder(view);
        }

    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof listItemHolder) {
            listItemHolder listItemHolder = (listItemHolder) holder;
            listItemHolder.position = position;
            final int positionPass=position;
            Weibo weibo = (Weibo) list.get(position);

            listItemHolder.weiboID = weibo.getWeiboID();
            final String weiboIdPass=weibo.getWeiboID();
            final String uidPass=weibo.getUid();
            Picasso.with(listItemHolder.v.getContext()).load(weibo.getHeader()).into(listItemHolder.header);
            listItemHolder.header.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onJumpUserPageListener!=null){
                        onJumpUserPageListener.onJump(uidPass);
                    }
                }
            });

            listItemHolder.name.setText(weibo.getUserName());


            listItemHolder.content.setText(TextUtil.addLinkToTopic(weibo.getWeiboContent()));
            listItemHolder.content.setMovementMethod(new LinkMovementMethod());
            listItemHolder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerViewHolder.onItemClick(positionPass, weiboIdPass);
                }
            });

//            listItemHolder.content.setText(weibo.getWeiboContent());
            listItemHolder.repost.setText("转发");
            listItemHolder.comment.setText("评论");
            listItemHolder.praise.setText("赞");
            if (weibo.getRepost() != "0") {
                listItemHolder.repost.setText(weibo.getRepost());
            }
            if (weibo.getComment() != "0") {
                listItemHolder.comment.setText(weibo.getComment());
            }
            if (weibo.getPraise() != "0") {
                listItemHolder.praise.setText(weibo.getPraise());
            }
            loadImage(listItemHolder, weibo);
            loadSourceWeibo(listItemHolder, weibo);

            listItemHolder.createTime.setText(TimeUtil.CovertTimeToDescription(weibo.getCreateTime()));
            System.out.println(listItemHolder.name);
        } else if (holder instanceof LoadMoreHolder) {

            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.position = position;
            loadMoreHolder.textView.setVisibility(View.VISIBLE);
            loadMoreHolder.progressBar.setVisibility(View.GONE);
            Weibo weibo = (Weibo) list.get(list.size() - 2);
            loadMoreHolder.weiboID = weibo.getWeiboID();
        }
    }

    public int getItemCount() {
        return list.size();
    }

    public void loadImage(listItemHolder listItemHolder, final Weibo weibo) {
        if (weibo.getImage().size() != 0) {
            View viewImage;
            if (listItemHolder.v.findViewById(R.id.imageInflateId) != null) {
                viewImage = listItemHolder.v.findViewById(R.id.imageInflateId);
                viewImage.setVisibility(View.VISIBLE);
            } else {
                viewImage = listItemHolder.ImageViewStub.inflate();
            }
            final ImageView viewStub_ImageView_1 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_1);
            final ImageView viewStub_ImageView_2 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_2);
            final ImageView viewStub_ImageView_3 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_3);
            final ImageView viewStub_ImageView_4 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_4);
            final ImageView viewStub_ImageView_5 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_5);
            final ImageView viewStub_ImageView_6 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_6);
            final ImageView viewStub_ImageView_7 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_7);
            final ImageView viewStub_ImageView_8 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_8);
            final ImageView viewStub_ImageView_9 = (ImageView) viewImage.findViewById(R.id.viewStub_ImageView_9);

            viewStub_ImageView_1.setVisibility(View.VISIBLE);
            viewStub_ImageView_2.setVisibility(View.VISIBLE);
            viewStub_ImageView_3.setVisibility(View.VISIBLE);
            viewStub_ImageView_4.setVisibility(View.VISIBLE);
            viewStub_ImageView_5.setVisibility(View.VISIBLE);
            viewStub_ImageView_6.setVisibility(View.VISIBLE);
            viewStub_ImageView_7.setVisibility(View.VISIBLE);
            viewStub_ImageView_8.setVisibility(View.VISIBLE);
            viewStub_ImageView_9.setVisibility(View.VISIBLE);

            viewStub_ImageView_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=0;
                        onPicClickListener.onPicClick(weibo.getImage(),0,viewStub_ImageView_1);
                    }
                }
            });
            viewStub_ImageView_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=1;
                        onPicClickListener.onPicClick(weibo.getImage(),1,viewStub_ImageView_2);
                    }
                }
            });
            viewStub_ImageView_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=2;
                        onPicClickListener.onPicClick(weibo.getImage(),2,viewStub_ImageView_3);
                    }
                }
            });
            viewStub_ImageView_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=3;
                        onPicClickListener.onPicClick(weibo.getImage(),3,viewStub_ImageView_4);
                    }
                }
            });
            viewStub_ImageView_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=4;
                        onPicClickListener.onPicClick(weibo.getImage(),4,viewStub_ImageView_5);
                    }
                }
            });
            viewStub_ImageView_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=5;
                        onPicClickListener.onPicClick(weibo.getImage(),5,viewStub_ImageView_6);
                    }
                }
            });
            viewStub_ImageView_7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=6;
                        onPicClickListener.onPicClick(weibo.getImage(),6,viewStub_ImageView_7);
                    }
                }
            });
            viewStub_ImageView_8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=7;
                        onPicClickListener.onPicClick(weibo.getImage(),7,viewStub_ImageView_8);
                    }
                }
            });
            viewStub_ImageView_9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=8;
                        onPicClickListener.onPicClick(weibo.getImage(),8,viewStub_ImageView_9);
                    }
                }
            });


            switch (weibo.getImage().size()) {


                case 1:
                    Picasso.with(viewImage.getContext()).load(weibo.getBmiddle_pic()).resize(bmiddleImageWidth, bmiddleImageHeight).centerCrop().into(viewStub_ImageView_1);
                    viewStub_ImageView_2.setVisibility(View.GONE);
                    viewStub_ImageView_3.setVisibility(View.GONE);
                    viewStub_ImageView_4.setVisibility(View.GONE);
                    viewStub_ImageView_5.setVisibility(View.GONE);
                    viewStub_ImageView_6.setVisibility(View.GONE);
                    viewStub_ImageView_7.setVisibility(View.GONE);
                    viewStub_ImageView_8.setVisibility(View.GONE);
                    viewStub_ImageView_9.setVisibility(View.GONE);

                    break;
                case 2:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    viewStub_ImageView_3.setVisibility(View.GONE);
                    viewStub_ImageView_4.setVisibility(View.GONE);
                    viewStub_ImageView_5.setVisibility(View.GONE);
                    viewStub_ImageView_6.setVisibility(View.GONE);
                    viewStub_ImageView_7.setVisibility(View.GONE);
                    viewStub_ImageView_8.setVisibility(View.GONE);
                    viewStub_ImageView_9.setVisibility(View.GONE);

                    break;
                case 3:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_3);
                    viewStub_ImageView_4.setVisibility(View.GONE);
                    viewStub_ImageView_5.setVisibility(View.GONE);
                    viewStub_ImageView_6.setVisibility(View.GONE);
                    viewStub_ImageView_7.setVisibility(View.GONE);
                    viewStub_ImageView_8.setVisibility(View.GONE);
                    viewStub_ImageView_9.setVisibility(View.GONE);
                    break;
                case 4:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_3);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_4);
                    viewStub_ImageView_5.setVisibility(View.GONE);
                    viewStub_ImageView_6.setVisibility(View.GONE);
                    viewStub_ImageView_7.setVisibility(View.GONE);
                    viewStub_ImageView_8.setVisibility(View.GONE);
                    viewStub_ImageView_9.setVisibility(View.GONE);
                    break;
                case 5:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_3);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_4);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_5);
                    viewStub_ImageView_6.setVisibility(View.GONE);
                    viewStub_ImageView_7.setVisibility(View.GONE);
                    viewStub_ImageView_8.setVisibility(View.GONE);
                    viewStub_ImageView_9.setVisibility(View.GONE);
                    break;
                case 6:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_3);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_4);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_5);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_6);
                    viewStub_ImageView_7.setVisibility(View.GONE);
                    viewStub_ImageView_8.setVisibility(View.GONE);
                    viewStub_ImageView_9.setVisibility(View.GONE);
                    break;
                case 7:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_3);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_4);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_5);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_6);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(6)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_7);
                    viewStub_ImageView_8.setVisibility(View.GONE);
                    viewStub_ImageView_9.setVisibility(View.GONE);
                    break;
                case 8:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_3);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_4);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_5);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_6);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(6)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_7);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(7)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_8);
                    viewStub_ImageView_9.setVisibility(View.GONE);
                    break;
                case 9:
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_1);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_2);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_3);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_4);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_5);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_6);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(6)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_7);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(7)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_8);
                    Picasso.with(viewImage.getContext()).load(weibo.getImage().get(8)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_ImageView_9);
                    break;
            }


        } else {
            if (listItemHolder.v.findViewById(R.id.imageInflateId) != null) {
                listItemHolder.v.findViewById(R.id.imageInflateId).setVisibility(View.GONE);
            }
        }
    }

    public void loadSourceWeibo(listItemHolder listItemHolder, final Weibo weibo) {
        if (weibo.getSource_userName().length()!=0) {
            View viewImage;
            if (listItemHolder.v.findViewById(R.id.sourceWeiboInflatedId) != null) {
                viewImage = listItemHolder.v.findViewById(R.id.sourceWeiboInflatedId);
                viewImage.setVisibility(View.VISIBLE);
            } else {
                viewImage = listItemHolder.sourceViewStub.inflate();
            }
            TextView sourceName = (TextView) viewImage.findViewById(R.id.SourceAuthorName);
            final ImageView viewStub_sourceImageView_1 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_1);
            final ImageView viewStub_sourceImageView_2 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_2);
            final ImageView viewStub_sourceImageView_3 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_3);
            final ImageView viewStub_sourceImageView_4 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_4);
            final ImageView viewStub_sourceImageView_5 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_5);
            final ImageView viewStub_sourceImageView_6 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_6);
            final ImageView viewStub_sourceImageView_7 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_7);
            final ImageView viewStub_sourceImageView_8 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_8);
            final ImageView viewStub_sourceImageView_9 = (ImageView) viewImage.findViewById(R.id.viewStub_sourceImageView_9);

            viewStub_sourceImageView_1.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_2.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_3.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_4.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_5.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_6.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_7.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_8.setVisibility(View.VISIBLE);
            viewStub_sourceImageView_9.setVisibility(View.VISIBLE);

            viewStub_sourceImageView_1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=0;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),0,viewStub_sourceImageView_1);
                    }
                }
            });
            viewStub_sourceImageView_2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=1;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),1,viewStub_sourceImageView_2);
                    }
                }
            });
            viewStub_sourceImageView_3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=2;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),2,viewStub_sourceImageView_3);
                    }
                }
            });
            viewStub_sourceImageView_4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=3;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),3,viewStub_sourceImageView_4);
                    }
                }
            });
            viewStub_sourceImageView_5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=4;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),4,viewStub_sourceImageView_5);
                    }
                }
            });
            viewStub_sourceImageView_6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=5;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),5,viewStub_sourceImageView_6);
                    }
                }
            });
            viewStub_sourceImageView_7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=6;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),6,viewStub_sourceImageView_7);
                    }
                }
            });
            viewStub_sourceImageView_8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=7;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),7,viewStub_sourceImageView_8);
                    }
                }
            });
            viewStub_sourceImageView_9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onPicClickListener != null) {
                        int picPosition=8;
                        onPicClickListener.onPicClick(weibo.getSourceImageUrl(),8,viewStub_sourceImageView_9);
                    }
                }
            });

            sourceName.setText(TextUtil.setSourceAuthorSpan(TextUtil.addLinkToTopic("@" + weibo.getSource_userName() + ":"+weibo.getSource_text()),weibo.getSource_userName()));
//            listItemHolder.content.setMovementMethod(new LinkMovementMethod());

            if (weibo.getSourceImageUrl().size() != 0) {
                switch (weibo.getSourceImageUrl().size()) {
                    case 1:
                        Picasso.with(viewImage.getContext()).load(weibo.getSource_bmiddle_pic()).resize(bmiddleImageWidth, bmiddleImageHeight).centerCrop().into(viewStub_sourceImageView_1);
                        viewStub_sourceImageView_2.setVisibility(View.GONE);
                        viewStub_sourceImageView_3.setVisibility(View.GONE);
                        viewStub_sourceImageView_4.setVisibility(View.GONE);
                        viewStub_sourceImageView_5.setVisibility(View.GONE);
                        viewStub_sourceImageView_6.setVisibility(View.GONE);
                        viewStub_sourceImageView_7.setVisibility(View.GONE);
                        viewStub_sourceImageView_8.setVisibility(View.GONE);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);
                        break;
                    case 2:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        viewStub_sourceImageView_3.setVisibility(View.GONE);
                        viewStub_sourceImageView_4.setVisibility(View.GONE);
                        viewStub_sourceImageView_5.setVisibility(View.GONE);
                        viewStub_sourceImageView_6.setVisibility(View.GONE);
                        viewStub_sourceImageView_7.setVisibility(View.GONE);
                        viewStub_sourceImageView_8.setVisibility(View.GONE);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);

                        break;
                    case 3:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_3);
                        viewStub_sourceImageView_4.setVisibility(View.GONE);
                        viewStub_sourceImageView_5.setVisibility(View.GONE);
                        viewStub_sourceImageView_6.setVisibility(View.GONE);
                        viewStub_sourceImageView_7.setVisibility(View.GONE);
                        viewStub_sourceImageView_8.setVisibility(View.GONE);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);
                        break;
                    case 4:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_3);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_4);
                        viewStub_sourceImageView_5.setVisibility(View.GONE);
                        viewStub_sourceImageView_6.setVisibility(View.GONE);
                        viewStub_sourceImageView_7.setVisibility(View.GONE);
                        viewStub_sourceImageView_8.setVisibility(View.GONE);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);
                        break;
                    case 5:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_3);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_4);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_5);
                        viewStub_sourceImageView_6.setVisibility(View.GONE);
                        viewStub_sourceImageView_7.setVisibility(View.GONE);
                        viewStub_sourceImageView_8.setVisibility(View.GONE);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);
                        break;
                    case 6:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_3);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_4);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_5);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_6);
                        viewStub_sourceImageView_7.setVisibility(View.GONE);
                        viewStub_sourceImageView_8.setVisibility(View.GONE);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);
                        break;
                    case 7:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_3);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_4);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_5);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_6);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(6)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_7);
                        viewStub_sourceImageView_8.setVisibility(View.GONE);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);
                        break;
                    case 8:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_3);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_4);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_5);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_6);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(6)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_7);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(7)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_8);
                        viewStub_sourceImageView_9.setVisibility(View.GONE);
                        break;
                    case 9:
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(0)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_1);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(1)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_2);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(2)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_3);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(3)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_4);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(4)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_5);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(5)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_6);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(6)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_7);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(7)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_8);
                        Picasso.with(viewImage.getContext()).load(weibo.getSourceImageUrl().get(8)).resize(thumbnailImageWidth, thumbnailImageheight).centerCrop().into(viewStub_sourceImageView_9);
                        break;

                }
            } else {
                viewStub_sourceImageView_1.setVisibility(View.GONE);
                viewStub_sourceImageView_2.setVisibility(View.GONE);
                viewStub_sourceImageView_3.setVisibility(View.GONE);
                viewStub_sourceImageView_4.setVisibility(View.GONE);
                viewStub_sourceImageView_5.setVisibility(View.GONE);
                viewStub_sourceImageView_6.setVisibility(View.GONE);
                viewStub_sourceImageView_7.setVisibility(View.GONE);
                viewStub_sourceImageView_8.setVisibility(View.GONE);
                viewStub_sourceImageView_9.setVisibility(View.GONE);
            }


        } else {
            if (listItemHolder.v.findViewById(R.id.sourceWeiboInflatedId) != null) {
                listItemHolder.v.findViewById(R.id.sourceWeiboInflatedId).setVisibility(View.GONE);
            }
        }

    }


}
