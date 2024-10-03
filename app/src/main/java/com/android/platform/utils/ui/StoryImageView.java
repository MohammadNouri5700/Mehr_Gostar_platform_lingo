package com.android.platform.utils.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SweepGradient;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class StoryImageView extends AppCompatImageView {

    private Paint borderPaint;
    private int borderWidth = (int) getResources().getDimension(com.intuit.sdp.R.dimen._4sdp);
    private int padding = (int) getResources().getDimension(com.intuit.sdp.R.dimen._2sdp);;

    public StoryImageView(Context context) {
        super(context);
        init();
    }

    public StoryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setScaleType(ScaleType.CENTER_CROP);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(borderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int radius = Math.min(getWidth() / 2, getHeight() / 2);
        int adjustedRadius = radius - borderWidth / 2;

        int[] colors = new int[]{Color.parseColor("#FFB3C3"), Color.parseColor("#FFB3C3"),
                Color.parseColor("#FFB3C3"), Color.parseColor("#FFB3C3"),
                Color.parseColor("#FFB3C3"), Color.parseColor("#FFB3C3"),
                Color.parseColor("#FFB3C3")};
        float[] positions = null;

        SweepGradient gradient = new SweepGradient(getWidth() / 2, getHeight() / 2, colors, positions);
        borderPaint.setShader(gradient);

        Path path = new Path();
        // رسم دایره تصویر
        path.addCircle(getWidth() / 2, getHeight() / 2, adjustedRadius, Path.Direction.CW);
        canvas.clipPath(path);

        // رسم خط دور (stroke) بعد از رسم تصویر
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, adjustedRadius, borderPaint);

        // تنظیم `clipPath` برای فاصله بین تصویر و حاشیه
        int innerRadius = adjustedRadius - padding;
        Path innerPath = new Path();
        innerPath.addCircle(getWidth() / 2, getHeight() / 2, innerRadius, Path.Direction.CW);
        canvas.clipPath(innerPath);
        super.onDraw(canvas);
    }
}
