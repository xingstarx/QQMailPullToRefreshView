package com.xingstarx.refreshview.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongxingxing on 16/9/24.
 */

public class QQMailRefreshView extends View {
    public static final String TAG = "QQMailRefreshView";
    private int mWidth;
    private int mHeight;
    private float MAX_CIRCLE_RADIUS = dp2px(getContext(), 20);
    private float MIN_CIRCLE_RADIUS = dp2px(getContext(), 14);
    private float mCircleRadius;
    private Paint mPaint;
    private int mColors[] = new int[]{0xffffe464, 0xffef4a4a, 0xffceee88};
    private int DEFAULT_DURATION = 1000;
    private List<Animator> animatorList = new ArrayList<>();
    private float mChangeWidth;
    private int mPaintAlpha;

    public QQMailRefreshView(Context context) {
        super(context);
        initView();
    }

    public QQMailRefreshView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public QQMailRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        initPaint();
        mPaintAlpha = 255;
        mCircleRadius = MAX_CIRCLE_RADIUS;
        mChangeWidth = 0;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(mColors[1]);
        mPaint.setAlpha(mPaintAlpha);
        canvas.drawCircle(mWidth / 2f + mChangeWidth, mHeight / 2f, mCircleRadius, mPaint);

    }

    public void start() {
        ValueAnimator lengthAnimator = ValueAnimator.ofFloat(0, dp2px(getContext(), 100));
        lengthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mChangeWidth = (float) animation.getAnimatedValue();
            }
        });

        ValueAnimator circleRadiusAnimator = ValueAnimator.ofFloat(MAX_CIRCLE_RADIUS, MIN_CIRCLE_RADIUS);
        circleRadiusAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCircleRadius = (float) animation.getAnimatedValue();
            }
        });

        ValueAnimator alphaAnimator = ValueAnimator.ofInt(255, 150);
        alphaAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mPaintAlpha = (int) animation.getAnimatedValue();
                invalidate();
            }
        });

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(lengthAnimator, circleRadiusAnimator, alphaAnimator);
        animatorSet.setDuration(DEFAULT_DURATION);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorList.add(animatorSet);
        animatorSet.start();
    }

    public void stop() {
        clearAnimator();
    }

    private void clearAnimator() {
        for(int i = 0; i < animatorList.size(); i++) {
            Animator animator = animatorList.get(i);
            if (animator.isRunning()) {
                animator.cancel();
            }
        }
        animatorList.clear();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        Log.e(TAG, "mWidth == " + mWidth + ", mHeight == " + mHeight);
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
