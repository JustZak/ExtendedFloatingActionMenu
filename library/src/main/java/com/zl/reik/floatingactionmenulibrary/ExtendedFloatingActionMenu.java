package com.zl.reik.floatingactionmenulibrary;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * Created by reik on 2/19/16.
 */
public class ExtendedFloatingActionMenu extends ViewGroup {
    public static final String TAG = ExtendedFloatingActionMenu.class.getSimpleName();

    private static final int ANIMATION_DURATION = 300;
    private static final int ANIMATION_DELAY_PER_ITEM = 50;

    private int mMenuButtonSize;
    private int mMenuButtonColor;
    private int mMenuButtonColorPressed;
    private int mMenuButtonIconColor;
    private int mButtonSpacing;
    private int mButtonCount = 0;
    private boolean mIsExpanded = false;
    private AnimatorSet mExpandAnimation = new AnimatorSet();
    private AnimatorSet mCollapseAnimation = new AnimatorSet();
    private Handler mUiHandler = new Handler();

    private FloatingActionMenuButton mMenuButton;

    public ExtendedFloatingActionMenu(final Context context) {
        this(context, null);
    }

    public ExtendedFloatingActionMenu(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtendedFloatingActionMenu(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExtendedFloatingActionMenu, defStyleAttr, 0);

        mMenuButtonSize = a.getInt(R.styleable.ExtendedFloatingActionMenu_fam_menuButtonSize, FloatingActionMenuButton.SIZE_NORMAL);
        mMenuButtonColor = a.getColor(R.styleable.ExtendedFloatingActionMenu_fam_menuButtonColor,
            getResources().getColor(android.R.color.holo_blue_light));
        mMenuButtonColorPressed = a.getColor(R.styleable.ExtendedFloatingActionMenu_fam_menuButtonColorPressed,
            getResources().getColor(android.R.color.holo_blue_dark));
        mMenuButtonIconColor = a.getColor(R.styleable.ExtendedFloatingActionMenu_fam_menuButtonIconColor,
            getResources().getColor(android.R.color.white));

        a.recycle();

        mButtonSpacing = (int) (getResources().getDimension(R.dimen.fab_button_spacing));
        mExpandAnimation.setDuration(ANIMATION_DURATION);
        mCollapseAnimation.setDuration(ANIMATION_DURATION);

        mMenuButton = new FloatingActionMenuButton(context);
        addView(mMenuButton, 0);
        mMenuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                toggle();
            }
        });
    }

    @Override
    public void addView(final View child, final int index) {
        mButtonCount++;
        super.addView(child, index);
    }

    @Override
    public void addView(final View child) {
        mButtonCount++;
        super.addView(child);
    }

    @Override
    public void removeView(final View view) {
        mButtonCount--;
        super.removeView(view);
    }


    public void addButton(FloatingActionButton button) {
        addView(button, mButtonCount - 1);
        mButtonCount++;
    }

    public void removeButton(FloatingActionButton button) {
        removeView(button);
        mButtonCount--;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int horizontalCenter = r - l - mMenuButton.getMeasuredWidth() / 2 - getPaddingRight();
        int menuButtonTop = b - t - mMenuButton.getMeasuredHeight() - getPaddingBottom();
        int menuButtonLeft = horizontalCenter - mMenuButton.getMeasuredWidth() /2;
        mMenuButton.layout(menuButtonLeft, menuButtonTop, menuButtonLeft + mMenuButton.getMeasuredWidth(), menuButtonTop + mMenuButton.getMeasuredHeight());
        int nextY = menuButtonTop - mButtonSpacing;

        for (int i = mButtonCount - 1; i >= 0; i--) {
            Log.d(TAG, "Next y: " + nextY);
            View child = getChildAt(i);

            if (child == mMenuButton || child.getVisibility() == GONE) continue;

            FloatingActionButton fab = (FloatingActionButton) child;

            int childX = horizontalCenter - fab.getMeasuredWidth()/2;
            int childY = nextY - child.getMeasuredHeight();
            Log.d(TAG, "childY: " + childY);

            fab.layout(childX, childY, childX + fab.getMeasuredWidth(), childY + fab.getMeasuredHeight());

//            float collapsedTranslation = mMenuButton.getY() - childY;
//            float expandedTranslation = 0f;
//
//            child.setTranslationY(mIsExpanded ? expandedTranslation : collapsedTranslation);

            nextY = childY - mButtonSpacing;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChild(mMenuButton, widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        int width = 0;

        for (int i = 0; i < mButtonCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE || child == mMenuButton) continue;

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            height += child.getMeasuredHeight();
        }

        width += mMenuButton.getMeasuredWidth() + getPaddingRight() + getPaddingLeft();
        height += mButtonSpacing * (mButtonCount - 1) + getPaddingBottom() + getPaddingTop() + mMenuButton.getMeasuredHeight();

        setMeasuredDimension(width, height);
    }

    private void collapse(final boolean animate) {
        if (mIsExpanded) {
            mMenuButton.collapse();
            int duration = animate ? ANIMATION_DURATION : 0;
            mCollapseAnimation.setDuration(duration);
            mCollapseAnimation.start();
            mExpandAnimation.cancel();

            int delay = 0;
            int counter = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child instanceof FloatingActionButton) {
                    counter++;

                    final FloatingActionButton fab = (FloatingActionButton) child;
                    mUiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (!mIsExpanded) return;

                            if (fab != mMenuButton) {
                                fab.hide();

                            }
                        }
                    }, delay);
                    delay += ANIMATION_DELAY_PER_ITEM;
                }
            }

            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsExpanded = false;
                }
            }, ++counter * ANIMATION_DELAY_PER_ITEM);
        }
    }


    public void expand(final boolean animate) {
        if (!mIsExpanded) {
            Log.d(TAG, "Expand");
            mMenuButton.expand();
            int duration = animate ? ANIMATION_DURATION : 0;
            Log.d(TAG, "duration: " + duration);
            mExpandAnimation.setDuration(duration);
            mExpandAnimation.start();
            mCollapseAnimation.cancel();

            int delay = 0;
            int counter = 0;
            for (int i = getChildCount() - 1; i >= 0; i--) {
                View child = getChildAt(i);
                if (child instanceof FloatingActionButton) {
                    counter++;
                    Log.d(TAG, "expanding a fab");
                    Log.d(TAG, "fab location: " + child.getX() + " " + child.getY());

                    final FloatingActionButton fab = (FloatingActionButton) child;
                    mUiHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsExpanded) {
                                Log.d(TAG, "already expanded");
                                return;
                            }

                            if (fab != mMenuButton) {
                                Log.d(TAG, "Showing");
                                fab.show();
                                Log.d(TAG, "visibility: " + fab.getVisibility());
                                Log.d(TAG, "fab x: " + fab.getX());
                                Log.d(TAG, "fab y: " + fab.getY());
                            }
                        }
                    }, delay);
                    delay += ANIMATION_DELAY_PER_ITEM;
                }
            }

            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mIsExpanded = true;
                }
            }, ++counter * ANIMATION_DELAY_PER_ITEM);
        }
    }

    public void toggle() {
        if (mIsExpanded) {
            collapse(true);
        } else {
            expand(true);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bringChildToFront(mMenuButton);
        mButtonCount = getChildCount();

        for (int i = 0; i < mButtonCount; i++) {
            View v = getChildAt(i);
            if (v == mMenuButton) {
                return;
            } else if (v instanceof FloatingActionButton) {
                v.setVisibility(View.GONE);
            }
        }
    }

}
