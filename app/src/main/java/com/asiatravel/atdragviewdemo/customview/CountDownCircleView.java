package com.asiatravel.atdragviewdemo.customview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.asiatravel.atdragviewdemo.R;

/**
 * Created by user on 16/9/19.
 */

public class CountDownCircleView extends View {
    private static final int VIEW_DEF_SIZE = 60;
    private static final long DEF_ANIMATION_TIME = 3000;

    private int circleBgColor;
    private int circlePbColor;
    private int circlePbWidth;
    private int circleTextColor;
    private int circleTextSize;

    private int circleViewSize;
    private int circleRadio;
    private Point circleCenter;

    private Paint circleBgPaint;
    private Paint circlePbPaint;
    private Paint circleTextPaint;
    private RectF pbOval;
    private String circleDesc;
    private Point circleDescPosition;
    private float endSweepAngel;
    private ValueAnimator valueAnimator;
    private OnCountDownFinishedListener countDownFinishedListener;

    public CountDownCircleView(Context context) {
        this(context, null);
    }

    public CountDownCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrWithStyle(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        circleBgPaint = creatPaint(circleBgColor, 0, Paint.Style.FILL, 0);
        circlePbPaint = creatPaint(circlePbColor, 0, Paint.Style.STROKE, circlePbWidth);
        circleTextPaint = creatPaint(circleTextColor, circleTextSize, Paint.Style.FILL, 0);
        circleCenter = new Point();
        circleDesc = "跳过";
        valueAnimator = getValA(0.F, 1.F);
    }

    private void initAttrWithStyle(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CountDownCircleView, defStyleAttr, R.style.def_countdown_circle_view);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.CountDownCircleView_circle_bg_color:
                    circleBgColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CountDownCircleView_circle_pb_color:
                    circlePbColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CountDownCircleView_circle_pb_width:
                    circlePbWidth = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.CountDownCircleView_circle_text_color:
                    circleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CountDownCircleView_circle_text_size:
                    circleTextSize = typedArray.getDimensionPixelSize(attr, 0);
                    break;
            }
        }
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        circleViewSize = Math.min(w, h);
        circleRadio = circleViewSize / 2;
        circleCenter.set(w / 2, h / 2);
        pbOval = new RectF(circlePbWidth, circlePbWidth, circleViewSize - circlePbWidth, circleViewSize - circlePbWidth);
        circleDescPosition = calculateTextRange(circleDesc);
    }

    private Point calculateTextRange(String circleDesc) {
        Point point = new Point();
        int textW = (int) circleTextPaint.measureText(circleDesc);
        point.set(circleViewSize / 2 - textW / 2, circleViewSize / 2 + getFontHeight(circleTextSize) / 2);
        return point;
    }

    public int getFontHeight(float fontSize) {
        Paint paint = new Paint();
        paint.setTextSize(fontSize);
        Paint.FontMetrics fm = paint.getFontMetrics();
        return (int) Math.ceil(fm.descent - fm.ascent) - 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthMeasure = 0;
        int heightMeasure = 0;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            widthMeasure = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, VIEW_DEF_SIZE, getContext().getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthMeasure, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            heightMeasure = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, VIEW_DEF_SIZE, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightMeasure, MeasureSpec.EXACTLY);
        }

        int viewMeasureSpec = widthMeasure - heightMeasure >= 0 ? heightMeasureSpec : widthMeasureSpec;
        super.onMeasure(viewMeasureSpec, viewMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircleBg(canvas);
        drawCirclePb(canvas);
        drawCircleText(canvas);
    }

    private void drawCircleText(Canvas canvas) {
        canvas.drawText(circleDesc, circleDescPosition.x, circleDescPosition.y, circleTextPaint);
    }

    private void drawCirclePb(Canvas canvas) {
        canvas.drawArc(pbOval, -90, endSweepAngel, false, circlePbPaint);
    }

    private void drawCircleBg(Canvas canvas) {
        canvas.drawCircle(circleCenter.x, circleCenter.y, circleRadio, circleBgPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (null != valueAnimator) {
            valueAnimator.cancel();
        }
    }

    private Paint creatPaint(int paintColor, int textSize, Paint.Style style, int lineWidth) {
        Paint paint = new Paint();
        paint.setColor(paintColor);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(lineWidth);
        paint.setDither(true);
        paint.setTextSize(textSize);
        paint.setStyle(style);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    private ValueAnimator getValA(float start, float end) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start, end);
        valueAnimator.setDuration(DEF_ANIMATION_TIME);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }

    public void startCountDown(final OnCountDownFinishedListener countDownFinishedListener) {
        this.countDownFinishedListener = countDownFinishedListener;
        if (null != valueAnimator) {
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    endSweepAngel = 360 * Float.valueOf(animation.getAnimatedValue().toString());
                    invalidate();
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    if (null != countDownFinishedListener) {
                        countDownFinishedListener.countDownStop();
                        valueAnimator.cancel();
                    }
                }
            });
            valueAnimator.start();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (null != countDownFinishedListener) {
                    countDownFinishedListener.countDownStop();
                    valueAnimator.cancel();
                }
                break;
        }
        return true;
    }

    public interface OnCountDownFinishedListener {
        void countDownStop();
    }
}
