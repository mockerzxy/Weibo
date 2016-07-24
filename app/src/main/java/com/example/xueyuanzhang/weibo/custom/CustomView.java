package com.example.xueyuanzhang.weibo.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xueyuanzhang on 16/7/21.
 */
public class CustomView extends View {

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(4);
        canvas.drawRect(20, 80, 220, 280, p);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int heightWidth = MeasureSpec.getMode(heightMeasureSpec);
        int wid = MeasureSpec.getSize(widthMeasureSpec);
        int heigh = MeasureSpec.getSize(heightMeasureSpec);
        heigh = wid / 16 * 9;

//        heightMeasureSpec=widthMeasureSpec/16*9;
        setMeasuredDimension(wid, heigh);
//        super.onMeasure(width, height);
    }
}
