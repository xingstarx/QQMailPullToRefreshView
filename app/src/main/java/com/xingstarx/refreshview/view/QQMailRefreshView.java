package com.xingstarx.refreshview.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
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
    private static final int PLAY_STOP_ANIMATION = 0;
    private static final int PLAY_START_ANIMATION = 1;
    private int mWidth;
    private int mHeight;
    private final float MAX_CIRCLE_RADIUS = dp2px(getContext(), 6);
    private final float MIN_CIRCLE_RADIUS = dp2px(getContext(), 4);
    private final int MAX_PAINT_ALPHA = 255;
    private final int MIN_PAINT_ALPHA = 150;
    private final int MAX_CHANGE_WIDTH = dp2px(getContext(), 20);

    private Paint mPaint;
    private int mColors[] = new int[]{0xffffe464, 0xffef4a4a, 0xffceee88};
    private int DEFAULT_DURATION = 1000;
    private List<Animator> animatorList = new ArrayList<>();
    private float mChangeWidth;
    private int playState;

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
        for (int i = 0; i < mColors.length; i++) {
            mPaint.setColor(mColors[i]);
            drawCirce(canvas, MAX_CHANGE_WIDTH * (i - 1) + mChangeWidth, mPaint);
        }

    }

    /**
     * 通过数学计算得到的表达式,x代表变化的长度的值,根据变化的长度,计算出圆圈的半径
     * y = (M - N) / a * x + M (x < 0)
     * y = (N - M) / a * x + M (x > 0)
     */
    private float getFuncRadius(float canvasTranslateX) {
        if (canvasTranslateX < 0) {
            return (MAX_CIRCLE_RADIUS - MIN_CIRCLE_RADIUS) / MAX_CHANGE_WIDTH * canvasTranslateX + MAX_CIRCLE_RADIUS;
        } else {
            return (MIN_CIRCLE_RADIUS - MAX_CIRCLE_RADIUS) / MAX_CHANGE_WIDTH * canvasTranslateX + MAX_CIRCLE_RADIUS;
        }
    }

    /**
     * 通过数学计算得到的表达式,x代表变化的长度的值,根据变化的长度,计算出颜色的alpha值 ,跟上面的解析式是一样的
     * y = (M - N) / a * x + M (x < 0)
     * y = (N - M) / a * x + M (x > 0)
     */
    private int getFuncAlpha(float canvasTranslateX) {
        if (canvasTranslateX < 0) {
            return (int) (canvasTranslateX * (MAX_PAINT_ALPHA - MIN_PAINT_ALPHA) / MAX_CHANGE_WIDTH + MAX_PAINT_ALPHA);
        } else {
            return (int) (canvasTranslateX * (MIN_PAINT_ALPHA - MAX_PAINT_ALPHA) / MAX_CHANGE_WIDTH + MAX_PAINT_ALPHA);
        }
    }

    private void drawCirce(Canvas canvas, float canvasTranslateX, @NonNull Paint paint) {
        if (canvasTranslateX > MAX_CHANGE_WIDTH) {
            canvasTranslateX -= 3 * MAX_CHANGE_WIDTH;
        }
        if (canvasTranslateX < -MAX_CHANGE_WIDTH) {
            return;
        }
        paint.setAlpha(getFuncAlpha(canvasTranslateX));
        canvas.translate(canvasTranslateX, 0);
        canvas.drawCircle(mWidth / 2, mHeight / 2, getFuncRadius(canvasTranslateX), paint);
        canvas.translate(-canvasTranslateX, 0);
    }


    public void start() {
        if (playState == PLAY_STOP_ANIMATION) {
            clearAnimator();
            playState = PLAY_START_ANIMATION;
            startChangeWidthAnimation();
        }
    }

    public void stop() {
        if (playState == PLAY_START_ANIMATION) {
            playState = PLAY_STOP_ANIMATION;
            clearAnimator();
        }
    }

    public void startChangeWidthAnimation() {
        ValueAnimator lengthAnimator = ValueAnimator.ofFloat(0, 3 * MAX_CHANGE_WIDTH);
        lengthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mChangeWidth = (float) animation.getAnimatedValue();
                invalidate();
            }
        });

        lengthAnimator.setDuration(DEFAULT_DURATION);
        lengthAnimator.setInterpolator(new LinearInterpolator());
        lengthAnimator.setRepeatCount(Integer.MAX_VALUE);
        animatorList.add(lengthAnimator);
        lengthAnimator.start();
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
