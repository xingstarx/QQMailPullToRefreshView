package com.xingstarx.refreshview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by xiongxingxing on 16/9/24.
 */

public class QQMailRefreshView extends View {
    public static final String TAG = "QQMailRefreshView";
    private int mWidth;
    private int mHeight;
    private int mCircleRadius = dp2px(getContext(), 10);
    private Paint mPaint;
    private int mColors[] = new int[]{0xffffe464, 0xfff75c50, 0xffceee88};

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
        float cx;
        float cy;

        cx = mWidth / 2f;
        cy = mHeight / 2f;
        mPaint.setColor(mColors[0]);
        canvas.drawCircle(cx, cy, mCircleRadius, mPaint);

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
