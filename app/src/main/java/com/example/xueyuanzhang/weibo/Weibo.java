package com.example.xueyuanzhang.weibo;

import java.util.List;

/**
 * Created by xueyuanzhang on 16/5/2.
 */
public class Weibo extends ListEntry {
    private String UserName;
    private String weiboID;
    private String WeiboContent;
    private String Header;
    private String repost;
    private String comment;
    private String praise;
    private List<String> imageUrl;
    private String createTime;
    private String bmiddle_pic;
    private String source_bmiddle_pic;
    private String source_userName;
    private String source_text;
    private String description;
    private List<String> sourceImageUrl;
    private String original_pic;
    private String uid;

    public void setUserName(String UserName){
        this.UserName=UserName;
    }
    public void setUid(String uid){this.uid=uid;}
    public void setWeiboID(String weiboID){
        this.weiboID =weiboID;
    }
    public  void setWeiboContent(String WeiboContent){
        this.WeiboContent=WeiboContent;
    }
public void setHeader(String Header){
        this.Header=Header;
}
    public void setRepost(String repost){this.repost=repost;}
    public void setComment(String comment){this.comment=comment;}
    public void setPraise(String praise){this.praise=praise;}
    public void setImage(List<String> imageUrl){this.imageUrl=imageUrl;}
    public void setCreateTime(String createTime){this.createTime=createTime;}
    public void setBmiddle_pic(String bmiddle_pic){this.bmiddle_pic=bmiddle_pic;}
    public void setSource_userName(String source_userName){this.source_userName=source_userName;}
    public void setSource_text(String source_text){this.source_text=source_text;}
    public void setDescription(String description){this.description=description;}
    public void setSourceImageUrl(List<String> sourceImageUrl){this.sourceImageUrl=sourceImageUrl;}
    public void setSource_bmiddle_pic(String source_bmiddle_pic){this.source_bmiddle_pic=source_bmiddle_pic;}
    public void setOriginal_pic(String original_pic){this.original_pic=original_pic;}

    public  String getUserName(){
           return this.UserName;
    }

    public  String getWeiboID(){
         return this.weiboID;
    }
    public  String getWeiboContent(){
         return this.WeiboContent;
    }
    public String getHeader(){return this.Header;}
    public String getRepost(){return this.repost;}
    public String getComment(){return this.comment;}
    public String getPraise(){return this.praise;}
    public List<String> getImage(){return this.imageUrl;}
    public String getCreateTime(){return this.createTime;}
    public String getBmiddle_pic(){return this.bmiddle_pic;}
    public String getDescription(){return this.description;}
    public String getSource_userName(){return this.source_userName;}
    public String getSource_text(){return this.source_text;}
    public List<String> getSourceImageUrl(){return this.sourceImageUrl;}
    public String getSource_bmiddle_pic(){return this.source_bmiddle_pic;}
    public String getOriginal_pic(){return this.original_pic;}
    public String getUid(){return this.uid;}
}
