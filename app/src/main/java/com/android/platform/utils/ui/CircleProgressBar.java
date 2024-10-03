package com.android.platform.utils.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.android.platform.R;

public class CircleProgressBar extends View {
    private Paint paint;
    private Paint backgroundPaint;
    private int progress = 0;
    private int max = 100;

    public CircleProgressBar(Context context) {
        super(context);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(getResources().getColor(R.color.progressbarProgress));  // رنگ پیشرفت
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);     // ضخامت نوار

        backgroundPaint = new Paint();
        backgroundPaint.setColor(getResources().getColor(R.color.progressbarProgressBackground)); // رنگ پس‌زمینه
        backgroundPaint.setStyle(Paint.Style.STROKE);
        backgroundPaint.setStrokeWidth(6);   // ضخامت نوار
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float radius = Math.min(width, height) / 2f - paint.getStrokeWidth();

        // رسم پس‌زمینه
        canvas.drawCircle(width / 2f, height / 2f, radius, backgroundPaint);

        // رسم پیشرفت
        float angle = 360f * progress / max;
        canvas.drawArc(paint.getStrokeWidth() / 2f, paint.getStrokeWidth() / 2f,
                width - paint.getStrokeWidth() / 2f, height - paint.getStrokeWidth() / 2f,
                -90, angle, false, paint);
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate(); // برای به‌روزرسانی View
    }

    public void setMax(int max) {
        this.max = max;
    }
}

