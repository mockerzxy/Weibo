package com.example.xueyuanzhang.weibo;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.View;

/**
 * Created by xueyuanzhang on 16/6/2.
 */
public class TextUtil {
    public static SpannableStringBuilder addLinkToTopic(String content){
        SpannableStringBuilder builder=new SpannableStringBuilder(content);
        boolean ifFirst=true;
        int startPo=0;
        int endPo;
        for(int i=0;i<builder.length();i++){
            if(builder.charAt(i)=='#'){
                if(ifFirst){
                    startPo=i;
                    ifFirst=false;
                }
                else{
                    endPo=i+1;
                    ClickableSpanWithoutUnderline span=new ClickableSpanWithoutUnderline() {
                        @Override
                        public void onClick(View widget) {

                        }
                    };
                    builder.setSpan(span, startPo, endPo, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ifFirst=true;
                }
            }
        }
        return builder;

    }
    public static SpannableStringBuilder setSourceAuthorSpan(SpannableStringBuilder builder,String authorName){

        ClickableSpanWithoutUnderline span=new ClickableSpanWithoutUnderline() {
            @Override
            public void onClick(View widget) {

            }
        };
        builder.setSpan(span,0,authorName.length()+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }
}
