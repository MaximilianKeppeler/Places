package com.mk.places.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mk.places.R;

public class ButtonLayout extends LinearLayout {

    public ButtonLayout(Context context) {
        super(context);
    }

    public ButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setbAmount(int bAmount) {
        setWeightSum(bAmount);
    }

    public void addButton(String bText, String bLink, boolean horizontal) {

        if (horizontal)
        setOrientation(HORIZONTAL);

        else {
            setOrientation(VERTICAL);
        }

        Button nButton;

        nButton = (Button) LayoutInflater.from(getContext()).inflate(R.layout.borderless_button, this, false);

        final LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        nButton.setText(bText);
        nButton.setTag(bLink);

        addView(nButton, params);

    }

}
