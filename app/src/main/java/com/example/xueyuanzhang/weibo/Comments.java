package com.example.xueyuanzhang.weibo;

/**
 * Created by xueyuanzhang on 16/5/15.
 */
public class Comments {
    private String header;
    private String name;
    private String time;
    private String text;
    public String aaa;
    public void setHeader(String header){this.header=header;}
    public void setName(String name){this.name=name;}
    public void setTime(String time){this.time=time;}
    public void setText(String text){this.text=text;}
    public String getHeader(){
        return this.header;
    }
    public String getName(){
        return this.name;
    }
    public String getTime(){
        return this.time;
    }
    public String getText(){
        return this.text;
    }
}
