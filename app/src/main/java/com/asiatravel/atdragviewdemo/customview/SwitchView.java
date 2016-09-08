package com.asiatravel.atdragviewdemo.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.asiatravel.atdragviewdemo.R;

/**
 * Created by Jsion on 16/9/8.
 */

public class SwitchView extends View {
    private static final int DEF_H = 40;
    private static final int DEF_W = 90;

    private int switchViewBgCloseColor;
    private int switchViewBgOpenColor;
    private int switchViewStrockColor;
    private int switchViewBallColor;
    private int switchViewStrockWidth;

    private Paint viewBgPaint;
    private Paint viewStrockPaint;
    private Paint viewBallPaint;

    private int viewHeight;
    private int viewWidth;
    private int strockRadio;
    private int solidRadio;

    private RectF bgRectF, bgStrockRectF;
    private boolean isOpen;
    private float swichBallx;

    public SwitchView(Context context) {
        this(context, null);
    }

    public SwitchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.SwitchView, defStyleAttr, R.style.def_switch_view);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.SwitchView_switch_bg_close_color:
                    switchViewBgCloseColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SwitchView_switch_bg_open_color:
                    switchViewBgOpenColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SwitchView_switch_bg_strock_width:
                    switchViewStrockWidth = typedArray.getDimensionPixelOffset(attr, 0);
                    break;
                case R.styleable.SwitchView_switch_strock_color:
                    switchViewStrockColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.SwitchView_switch_ball_color:
                    switchViewBallColor = typedArray.getColor(attr, Color.BLACK);
                    break;
            }
        }
        typedArray.recycle();
        initData();
    }

    private void initData() {
        viewBgPaint = creatPaint(switchViewBgCloseColor, 0, Paint.Style.FILL, 0);
        viewBallPaint = creatPaint(switchViewBallColor, 0, Paint.Style.FILL, 0);
        viewStrockPaint = creatPaint(switchViewStrockColor, 0, Paint.Style.FILL, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewHeight = h;
        viewWidth = w;

        strockRadio = viewHeight / 2;
        solidRadio = (viewHeight - 2 * switchViewStrockWidth) / 2;

        swichBallx = strockRadio;
        bgStrockRectF = new RectF(0, 0, viewWidth, viewHeight);
        bgRectF = new RectF(switchViewStrockWidth, switchViewStrockWidth, viewWidth - switchViewStrockWidth, viewHeight - switchViewStrockWidth);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int measureWidth;
        int measureHeight;

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED) {
            measureWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_W, getResources().getDisplayMetrics());
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(measureWidth, MeasureSpec.EXACTLY);
        }

        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED) {
            measureHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEF_H, getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(measureHeight, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawSwichBg(canvas);
        drawSwichBallByFlag(canvas);
    }

    private void drawSwichBallByFlag(Canvas canvas) {
        canvas.drawCircle(swichBallx, strockRadio, solidRadio, viewBallPaint);
    }

    private void drawSwichBg(Canvas canvas) {
        canvas.drawRoundRect(bgStrockRectF, strockRadio, strockRadio, viewStrockPaint);
        canvas.drawRoundRect(bgRectF, solidRadio, solidRadio, viewBgPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                if (moveX < strockRadio) {
                    moveX = strockRadio;
                }
                if (moveX > viewWidth - strockRadio) {
                    moveX = viewWidth - strockRadio;
                }
                swichBallx = moveX;
                break;
            case MotionEvent.ACTION_UP:
                int upX = (int) event.getX();
                if (upX > viewWidth / 2) {
                    setOpenState(true);
                } else {
                    setOpenState(false);
                }
                break;
        }
        invalidate();
        return true;
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

    public void setOpenState(boolean openState) {
        isOpen = openState;
        swichBallx = isOpen ? viewWidth - strockRadio : strockRadio;
        int currentStrockColor = isOpen ? switchViewBgOpenColor : switchViewStrockColor;
        int currentBgColor = isOpen ? switchViewBgOpenColor : switchViewBgCloseColor;
        viewStrockPaint.setColor(currentStrockColor);
        viewBgPaint.setColor(currentBgColor);
    }

}
