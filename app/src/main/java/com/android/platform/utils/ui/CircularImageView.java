package com.android.platform.utils.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {

    private Paint borderPaint;
    private int borderWidth = (int) getResources().getDimension(com.intuit.sdp.R.dimen._5sdp);; // ضخامت خط دور

    public CircularImageView(Context context) {
        super(context);
        init();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_CROP);


        borderPaint = new Paint();
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Path path = new Path();
        int radius = Math.min(getWidth()/2, getHeight()/2);

        // رسم دایره تصویر
        path.addCircle(getWidth()/2, getHeight()/2, radius, Path.Direction.CW);
        canvas.clipPath(path);
        super.onDraw(canvas);

        // رسم خط دور (stroke) بعد از رسم تصویر
        canvas.drawCircle(getWidth()/2, getHeight()/2, radius - borderWidth / 2, borderPaint);
    }
}

