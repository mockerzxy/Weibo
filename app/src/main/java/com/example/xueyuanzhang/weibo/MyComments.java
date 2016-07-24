package com.example.xueyuanzhang.weibo;

/**
 * Created by xueyuanzhang on 16/5/22.
 */
public class MyComments {
    private String headerOfComment;
    private String nameOfComment;
    private String timeOfComment;
    private String textOfComment;
    private String headerOfAuther;
    private String nameOfAuther;
    private String textOfSource;

    public void setHeaderOfComment(String headerOfComment){this.headerOfComment=headerOfComment;}
    public void setNameOfComment(String nameOfComment){this.nameOfComment=nameOfComment;}
    public void setTimeOfComment(String timeOfComment){this.timeOfComment=timeOfComment;}
    public void setTextOfComment(String textOfComment){this.textOfComment=textOfComment;}
    public void setHeaderOfAuther(String headerOfAuther){this.headerOfAuther=headerOfAuther;}
    public void setNameOfAuther(String nameOfAuther){this.nameOfAuther=nameOfAuther;}
    public void setTextOfSource(String textOfSource){this.textOfSource=textOfSource;}

    public String getHeaderOfComment(){return headerOfComment;}
    public String getNameOfComment(){return nameOfComment;}
    public String getTimeOfComment(){return timeOfComment;}
    public String getTextOfComment(){return textOfComment;}
    public String getHeaderOfAuther(){return headerOfAuther;}
    public String getNameOfAuther(){return nameOfAuther;}
    public String getTextOfSource(){return textOfSource;}
}
