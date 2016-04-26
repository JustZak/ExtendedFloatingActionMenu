package com.zl.reik.floatingactionmenulibrary;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 * Created by reik on 2/20/16.
 */
public class PlusCancelDrawable extends Drawable{
    public static final String TAG = PlusCancelDrawable.class.getSimpleName();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mSize;
    private final Rect mClipBounds;
    private int mRectWidth;

    public PlusCancelDrawable(int size, int color) {
        mClipBounds = new Rect();
        mSize = size;
        mRectWidth = mSize/8;

        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void draw(final Canvas canvas) {
        canvas.getClipBounds(mClipBounds);
        canvas.drawRect(
            mClipBounds.centerX() - mRectWidth/2,
            mClipBounds.centerY() - mSize/2,
            mClipBounds.centerX() + mRectWidth/2,
            mClipBounds.centerY() + mSize/2,
            mPaint);
        canvas.drawRect(
            mClipBounds.centerX() - mSize/2,
            mClipBounds.centerY() - mRectWidth/2,
            mClipBounds.centerX() + mSize/2,
            mClipBounds.centerY() + mRectWidth/2,
            mPaint);
    }

    @Override
    public void setAlpha(final int alpha) {
        if (alpha != mPaint.getAlpha()) {
            mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    @Override
    public void setColorFilter(final ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }
}
