package com.asiatravel.atdragviewdemo.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by user on 16/9/19.
 */

public class MyElongVerticalTextView extends TextView {

    public MyElongVerticalTextView(Context context) {
        super(context);
    }

    public MyElongVerticalTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyElongVerticalTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String verticalText = generateVerticalText(text.toString());
        super.setText(verticalText, type);
    }

    private String generateVerticalText(String desc) {
        StringBuilder stringBuffer = new StringBuilder();
        char[] chars = desc.toCharArray();
        for (char currentChar : chars) {
            String temp = currentChar + "\n";
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }
}
