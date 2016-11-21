package com.asiatravel.atdragviewdemo.customview;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.asiatravel.atdragviewdemo.R;

/**
 * Created by feilong.guo on 16/11/21.
 */

public class AndTextViewLayout extends LinearLayout {
    private int andTextColor;
    private int andTextSize;
    private int andTextBbackgroundColor;
    private boolean andTextAnimUp;
    private boolean andTextAnimDown;
    private boolean andTextAnimLeft;
    private boolean andTextAnimRight;
    private int aniDuration;
    private String andTextDesc;

    private TextView andTextView;

    public AndTextViewLayout(Context context) {
        this(context, null);
    }

    public AndTextViewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AndTextViewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AndTextViewLayout, defStyleAttr, R.style.def_and_text_layout);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.AndTextViewLayout_and_text_color:
                    andTextColor = typedArray.getColor(attr, 0);
                    break;
                case R.styleable.AndTextViewLayout_and_text_size:
                    andTextSize = typedArray.getInteger(attr, 0);
                    break;
                case R.styleable.AndTextViewLayout_and_text_background_color:
                    andTextBbackgroundColor = typedArray.getColor(attr, 0);
                    break;
                case R.styleable.AndTextViewLayout_and_text_anim_duration_second:
                    aniDuration = typedArray.getInteger(attr, 0) * 1000;
                    break;
                case R.styleable.AndTextViewLayout_and_text_anim_up:
                    andTextAnimUp = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.AndTextViewLayout_and_text_anim_down:
                    andTextAnimDown = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.AndTextViewLayout_and_text_anim_left:
                    andTextAnimLeft = typedArray.getBoolean(attr, false);
                    break;
                case R.styleable.AndTextViewLayout_and_text_anim_right:
                    andTextAnimRight = typedArray.getBoolean(attr, true);
                    break;
                case R.styleable.AndTextViewLayout_and_text_desc:
                    andTextDesc = typedArray.getString(attr);
                    break;
            }
        }
        typedArray.recycle();
        initAndTextView();

    }

    private void initAndTextView() {
        andTextView = new TextView(getContext());
        LinearLayout.LayoutParams layoutParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        andTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, andTextSize);
        andTextView.setTextColor(andTextColor);
        andTextView.setText(andTextDesc);
        andTextView.setBackgroundColor(andTextBbackgroundColor);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        andTextView.setLayoutParams(layoutParams);
        addView(andTextView);
    }

    public void startAndTextAnim() {
        ObjectAnimator objectAnimator = creatCurrentAnimation();
        objectAnimator.setDuration(aniDuration);
        objectAnimator.setInterpolator(new LinearInterpolator());
        objectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        objectAnimator.start();
    }

    private ObjectAnimator creatCurrentAnimation() {
        ObjectAnimator objectAnimator = null;
        if (andTextAnimRight) {
            objectAnimator = ObjectAnimator.ofFloat(andTextView, "translationX", -getWidth(), getWidth() + andTextView.getWidth());
        } else if (andTextAnimLeft) {
            objectAnimator = ObjectAnimator.ofFloat(andTextView, "translationX", getWidth() + andTextView.getWidth(), -getWidth());
        } else if (andTextAnimUp) {
            objectAnimator = ObjectAnimator.ofFloat(andTextView, "translationY", -getHeight(), getHeight() + andTextView.getHeight());
        } else if (andTextAnimDown) {
            objectAnimator = ObjectAnimator.ofFloat(andTextView, "translationY", getHeight() + andTextView.getHeight(), -getHeight());
        }
        return objectAnimator;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 测试
        startAndTextAnim();
    }
}
