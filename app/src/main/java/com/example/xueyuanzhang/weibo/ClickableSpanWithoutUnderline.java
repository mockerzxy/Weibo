package com.example.xueyuanzhang.weibo;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * Created by xueyuanzhang on 16/5/23.
 */
public abstract class ClickableSpanWithoutUnderline extends ClickableSpan{

    public void updateDrawState(TextPaint ds){
    super.updateDrawState(ds);
        ds.setColor(Color.rgb(163,178,253));
        ds.setUnderlineText(false);
    }
}
