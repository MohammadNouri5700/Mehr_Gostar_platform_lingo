package com.android.platform.utils.ui;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.android.platform.R;
import com.google.android.material.card.MaterialCardView;



public class CardViewShadowCustom extends MaterialCardView {

    private Paint shadowPaint;
    private RectF shadowBounds;

    public CardViewShadowCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
        initShadowPaint();
    }

    private void initShadowPaint() {
        shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(getResources().getColor(R.color.blue));
        shadowPaint.setShadowLayer(10.0f, 0.0f, 5.0f, getResources().getColor(R.color.blue));
        setLayerType(LAYER_TYPE_SOFTWARE, shadowPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        shadowBounds = new RectF(0, 0, getWidth(), getHeight());
        canvas.drawRoundRect(shadowBounds, getRadius(), getRadius(), shadowPaint);
    }
}