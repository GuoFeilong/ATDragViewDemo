package com.asiatravel.atdragviewdemo.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.asiatravel.atdragviewdemo.R;

/**
 * Created by Jsion on 16/10/30.
 */

public class ViewShowActivity extends AppCompatActivity {
    private ScrollView scrollView;
    private TextView textView;
    private TextView next;
    private int index;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewshow);

        scrollView = (ScrollView) findViewById(R.id.sv_container);
        textView = (TextView) findViewById(R.id.tv_content);
        next = (TextView) findViewById(R.id.tv_next);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNext();
            }
        });

    }

    private void showNext() {
        AnimatorSet animatorSetHide = new AnimatorSet();
        final ObjectAnimator transHide = ObjectAnimator.ofFloat(scrollView, "translationX", 0, getScreenWidth());
        ObjectAnimator alphHide = ObjectAnimator.ofFloat(scrollView, "alpha", 1.F, 0.F);
        animatorSetHide.setDuration(500);
        animatorSetHide.playTogether(transHide, alphHide);
        animatorSetHide.start();
        animatorSetHide.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                changeContent(true);
                showContent();
            }
        });

    }

    private void showContent() {
        AnimatorSet animatorSetShow = new AnimatorSet();
        ObjectAnimator transShow = ObjectAnimator.ofFloat(scrollView, "translationX", -getScreenWidth(), 0);
        ObjectAnimator alphShow = ObjectAnimator.ofFloat(scrollView, "alpha", 0F, 1F);
        animatorSetShow.playTogether(transShow, alphShow);
        animatorSetShow.setDuration(500);
        animatorSetShow.start();
    }

    private int getScreenWidth() {
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    private void changeContent(boolean showOrHide) {
        if (showOrHide) {
            index++;
        }
        textView.setText("我是下一题目---------" + index);
    }

}
