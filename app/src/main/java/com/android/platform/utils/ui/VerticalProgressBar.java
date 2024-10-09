package com.android.platform.utils.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class VerticalProgressBar extends View {
    private Paint backgroundPaint;
    private Paint progressPaint;
    private int max = 100;
    private int progress = 0;
    private Path path = new Path();

    public VerticalProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        backgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        backgroundPaint.setColor(0xFFDDDDCD); // Gray color for background
        backgroundPaint.setStyle(Paint.Style.FILL);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setColor(0xFF08433e); // Red color for progress
        progressPaint.setStyle(Paint.Style.FILL);
        Random random = new Random();
        int randomNumber = random.nextInt(max - 50 + 1) + 50;
        setMax(100);
        setProgress(randomNumber);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        float radius = 30; // Adjust this value as needed

        // Drawing the background with rounded corners
        Path backgroundPath = new Path();
        backgroundPath.addRoundRect(new RectF(0, 0, width, height), radius, radius, Path.Direction.CW);
        canvas.drawPath(backgroundPath, backgroundPaint);

        // Drawing the progress with rounded corners
        if (progress > 0) {
            float progressHeight = height * progress / max;
            Path progressPath = new Path();
            // Ensure that the progress bar also has rounded corners
            progressPath.addRoundRect(new RectF(0, height - progressHeight, width, height), radius, radius, Path.Direction.CW);
            canvas.drawPath(progressPath, progressPaint);
        }
    }

    public void setProgress(int progress) {
        this.progress = Math.max(0, Math.min(progress, max)); // Ensure progress does not go out of bounds
        invalidate(); // Redraw the view
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }
}