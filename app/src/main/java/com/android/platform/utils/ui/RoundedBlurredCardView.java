package com.android.platform.utils.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class RoundedBlurredCardView extends FrameLayout {

    private Bitmap bitmap;
    private Paint paint;
    private Path path;
    private float cornerRadius = 30f;
    private RenderScript rs;
    private ScriptIntrinsicBlur scriptBlur;

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

            canvas.drawBitmap(bitmap, 0, 0, paint);


            int halfHeight = getHeight() / 3 * 2;
            Rect srcRect = new Rect(0, halfHeight, getWidth(), getHeight());
            Rect destRect = new Rect(0, halfHeight, getWidth(), getHeight());
            canvas.drawBitmap(blurredBitmap, srcRect, destRect, paint);
        }
    }

    public Bitmap createBlurredBitmap(Bitmap original) {
        if (original == null) {
            return null;
        }

        // ایجاد یک bitmap جدید برای خروجی
        Bitmap outputBitmap = Bitmap.createBitmap(original.getWidth(), original.getHeight(), Bitmap.Config.ARGB_8888);
        Allocation input = Allocation.createFromBitmap(rs, original);
        Allocation output = Allocation.createFromBitmap(rs, outputBitmap);

        // تنظیم مقدار بلور با محدودیت
        float blurRadius = Math.min(25f, Math.max(1f, 25f));

        scriptBlur.setRadius(blurRadius);
        scriptBlur.setInput(input);
        scriptBlur.forEach(output);
        output.copyTo(outputBitmap);

        return outputBitmap;
    }

    public void setBlurredBackground(Bitmap bitmap) {
        setImageBitmap(bitmap);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        rs.destroy();
        scriptBlur.destroy();
    }
}
