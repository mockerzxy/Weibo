package com.example.xueyuanzhang.weibo;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by xueyuanzhang on 16/5/25.
 */
public class TimeUtil {
    public static String CovertTimeToDescription(String timeStamp){
        Date date=new Date(timeStamp);
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Calendar now=new GregorianCalendar();
        SimpleDateFormat timeFormat=new SimpleDateFormat("HH:mm");
        SimpleDateFormat monthFormat=new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat yearFormat=new SimpleDateFormat("yyyy-MM-dd");
        String justNow="刚刚";
        String minuteAgo="分钟前";
        String secondAgo="秒前";
        String yesterday="昨天";

        if(calendar.get(Calendar.YEAR)!=now.get(Calendar.YEAR)){
            return yearFormat.format(date);
        }else if(calendar.get(Calendar.MONTH)!=now.get(Calendar.MONTH)||calendar.get(Calendar.DAY_OF_MONTH)!=now.get(Calendar.DAY_OF_MONTH)){
            return monthFormat.format(date);
        }else{
            int subSecond=now.get(Calendar.HOUR)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND)-
                    calendar.get(Calendar.HOUR)*3600-calendar.get(Calendar.MINUTE)*60-calendar.get(Calendar.SECOND);
            if(subSecond<5) {
                return justNow;
            }else if(subSecond>=5&&subSecond<60){
                    return subSecond+secondAgo;

            }else if(60<=subSecond&&subSecond<3600){
                int minute=subSecond/60;
                int second=subSecond%60;
                if(second>30){
                    minute=minute+1;
                }
                return minute+minuteAgo;
            }
            else{
                return timeFormat.format(date);
            }

        }
    }
}
