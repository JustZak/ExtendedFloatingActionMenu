package com.zl.reik.floatingactionmenulibrary;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by reik on 2/19/16.
 */
public class FloatingActionMenuButton extends FloatingActionButton {
    private static final String TAG = FloatingActionMenuButton.class.getSimpleName();
    public static final int SIZE_MINI = 1;
    public static final int SIZE_NORMAL = 0;

    private int mIconColor;
    private Paint mIconPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Animation mExpandAnimation;
    private Animation mCollapseAnimation;
    private AnimationSet mExpandAnimationSet;
    private AnimationSet mCollapseAnimationSet;
    private boolean mIsAnimating = false;
    private boolean isOpen = false;
    private PlusCancelDrawable mDrawable;
    private int mButtonSize;

    public FloatingActionMenuButton(final Context context) {
        this(context, null);
    }

    public FloatingActionMenuButton(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionMenuButton(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,
            android.support.design.R.styleable.FloatingActionButton, defStyleAttr,
            android.support.design.R.style.Widget_Design_FloatingActionButton);
        mButtonSize =  a.getInt(android.support.design.R.styleable.FloatingActionButton_fabSize, SIZE_NORMAL);
        a.recycle();

        mCollapseAnimationSet = new AnimationSet(context, attrs);
        mExpandAnimationSet = new AnimationSet(context, attrs);

        mCollapseAnimation = new RotateAnimation(45, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mCollapseAnimation.setDuration(250);
        mCollapseAnimation.setInterpolator(new OvershootInterpolator());
        mCollapseAnimation.setFillAfter(true);
        mCollapseAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mIsAnimating = false;
                isOpen = !isOpen;
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });
        mCollapseAnimationSet.addAnimation(mCollapseAnimation);
        mCollapseAnimationSet.setFillAfter(true);
        mCollapseAnimationSet.setInterpolator(new OvershootInterpolator(5.0f));

        mExpandAnimation = new RotateAnimation(0, 45, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mExpandAnimation.setDuration(250);
        mExpandAnimation.setInterpolator(new OvershootInterpolator());
        mExpandAnimation.setFillAfter(true);
        mExpandAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(final Animation animation) {
                mIsAnimating = true;
                Log.d(TAG, "Animation Starting");
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                mIsAnimating = false;
                Log.d(TAG, "Animation ending");
                isOpen = !isOpen;
            }

            @Override
            public void onAnimationRepeat(final Animation animation) {

            }
        });
        mExpandAnimationSet.addAnimation(mExpandAnimation);
        mExpandAnimationSet.setFillAfter(true);
        mExpandAnimationSet.setInterpolator(new OvershootInterpolator(5.0f));

        mDrawable = new PlusCancelDrawable(getSizeDimension() /4, android.R.color.white);
    }

    final int getSizeDimension() {
        switch (mButtonSize) {
            case SIZE_MINI:
                return getResources().getDimensionPixelSize(R.dimen.fab_size_mini);
            case SIZE_NORMAL:
            default:
                return getResources().getDimensionPixelSize(R.dimen.fab_size_normal);
        }
    }

    public void setIconColor(int color) {
        mIconColor = color;
        invalidate();
    }

    public void toggle() {
        Log.d(TAG, "Toggle");
        if (mIsAnimating) {
            return;
        }
        if (isOpen) {
            startAnimation(mCollapseAnimationSet);
        } else {
            startAnimation(mExpandAnimationSet);
        }
    }

    public void collapse() {
        if (mIsAnimating) {
            return;
        }
        if (isOpen) {
            startAnimation(mCollapseAnimationSet);
        }
    }

    public void expand() {
        if (mIsAnimating) {
            return;
        }
        if (!isOpen) {
            startAnimation(mExpandAnimationSet);
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.draw(canvas);
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "width:" + getWidth());
        Log.d(TAG, "height: " + getHeight());
    }
}
