package com.android.platform.utils.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.android.platform.R;

public class RoundedBlurredCardView extends FrameLayout {

    private Bitmap bitmap;
    private Paint paint;
    private Path path;
    private float cornerRadius = 30f;
    private RenderScript rs;
    private ScriptIntrinsicBlur scriptBlur;
    private ProgressBar progressBar;
    private LayoutParams ProgressBarParams;
    private int progressValue = 34;

    public RoundedBlurredCardView(Context context) {
        super(context);
        init(context);
    }

    public RoundedBlurredCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        path = new Path();
        rs = RenderScript.create(context);
        scriptBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        scriptBlur.setRadius(25F);


    }

    public void setProgress(int value) {
        progressValue = value;
    }

    public void initProgressBar() {
        if (progressBar == null) {


            progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal);
            progressBar.setIndeterminate(false);
            progressBar.setMax(100);
            progressBar.setMin(0);
            progressBar.setBackgroundColor(Color.TRANSPARENT);
            progressBar.setProgressTintList(ColorStateList.valueOf(getResources().getColor(android.R.color.holo_red_dark)));
            ProgressBarParams = new LayoutParams(LayoutParams.MATCH_PARENT, 6);
            ProgressBarParams.topMargin = getHeight() / 3 * 2;
            if (ProgressBarParams.topMargin == 0)
                progressBar = null;
            else{
                addView(progressBar, ProgressBarParams);
                ObjectAnimator animator = ObjectAnimator.ofInt(progressBar, "progress", progressValue);
                animator.setDuration(1000);
                animator.start();
            }


        }

    }

    public void setImageBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            Bitmap blurredBitmap = createBlurredBitmap(bitmap);

            path.reset();
            path.addRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius, Path.Direction.CW);
            canvas.clipPath(path);

            // مقیاس‌دهی تصویر
            float scaleWidth = (float) getWidth() / bitmap.getWidth();
            float scaleHeight = (float) getHeight() / bitmap.getHeight();
            float scale = Math.min(scaleWidth, scaleHeight);

            // محاسبه مختصات
            int scaledWidth = (int) (bitmap.getWidth() * scale);
            int scaledHeight = (int) (bitmap.getHeight() * scale);
            int left = (getWidth() - scaledWidth) / 2;
            int top = (getHeight() - scaledHeight) / 2;

            canvas.drawBitmap(bitmap, null, new Rect(left, top, left + scaledWidth, top + scaledHeight), paint);

            int halfHeight = getHeight() / 3 * 2;
            Rect srcRect = new Rect(0, halfHeight, getWidth(), getHeight());
            Rect destRect = new Rect(0, halfHeight, getWidth(), getHeight());
            canvas.drawBitmap(blurredBitmap, srcRect, destRect, paint);
            initProgressBar();
        }
    }

    public Bitmap createBlurredBitmap(Bitmap original) {
        if (original == null) {
            return null;
        }


        Bitmap outputBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Allocation input = Allocation.createFromBitmap(rs, original);
        Allocation output = Allocation.createFromBitmap(rs, outputBitmap);


        float blurRadius = Math.min(25f, Math.max(1f, 25f));

        scriptBlur.setRadius(blurRadius);
        scriptBlur.setInput(input);
        scriptBlur.forEach(output);
        output.copyTo(outputBitmap);

        return outputBitmap;
    }

    public void setBlurredBackground(Bitmap bitmap) {
        setImageBitmap(bitmap);
        initProgressBar();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        rs.destroy();
        scriptBlur.destroy();
    }
}
