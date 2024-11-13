package com.android.platform.utils.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.SweepGradient;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class PodcastImageView extends AppCompatImageView {

    private Paint borderPaint;
    private int borderWidth = (int) getResources().getDimension(com.intuit.sdp.R.dimen._2sdp); // ضخامت خط دور
    private int padding = (int) getResources().getDimension(com.intuit.sdp.R.dimen._3sdp);; // فاصله بین تصویر و خط دور

    public PodcastImageView(Context context) {
        super(context);
        init();
    }

    public PodcastImageView(Context context, AttributeSet attrs) {
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
        int width = getWidth();
        int height = getHeight();

        // تعریف مستطیل با در نظر گرفتن ضخامت خط دور
        float left = borderWidth / 2f;
        float top = borderWidth / 2f;
        float right = width - borderWidth / 2f;
        float bottom = height - borderWidth / 2f;

        // شعاع گوشه‌های گرد
        float cornerRadius = 20; // می‌توانید این مقدار را تغییر دهید تا گوشه‌ها گردتر یا کمتر گرد شوند

        // تعریف رنگ‌ها و محل قرارگیری آنها در گرادیان
        int[] colors = new int[]{Color.parseColor("#29B6F6"), Color.parseColor("#0277BD"),
                Color.parseColor("#3F51B5"), Color.parseColor("#4DD0E1"),
                Color.parseColor("#3949AB"), Color.parseColor("#4DD0E1"),
                Color.parseColor("#29B6F6")}; // اضافه کردن اولین رنگ در انتها برای ایجاد یک حلقه کامل بدون درز
        float[] positions = null; // مکان‌های دقیق رنگ‌ها، خودکار تنظیم می‌شود

        SweepGradient gradient = new SweepGradient(width / 2f, height / 2f, colors, positions);
        borderPaint.setShader(gradient);

        Path path = new Path();
        // رسم مستطیل با گوشه‌های گرد
        path.addRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(path);

        // رسم خط دور (stroke) بعد از رسم تصویر
        canvas.drawRoundRect(left, top, right, bottom, cornerRadius, cornerRadius, borderPaint);

        // تنظیم `clipPath` برای فاصله بین تصویر و حاشیه
        float innerLeft = left + padding;
        float innerTop = top + padding;
        float innerRight = right - padding;
        float innerBottom = bottom - padding;
        Path innerPath = new Path();
        innerPath.addRoundRect(innerLeft, innerTop, innerRight, innerBottom, cornerRadius, cornerRadius, Path.Direction.CW);
        canvas.clipPath(innerPath);

        super.onDraw(canvas);
    }


}
