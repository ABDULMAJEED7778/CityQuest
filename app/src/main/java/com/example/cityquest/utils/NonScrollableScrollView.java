package com.example.cityquest.utils;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class NonScrollableScrollView extends ScrollView {

    private boolean isScrollable = true; // Default to scrollable

    public NonScrollableScrollView(Context context) {
        super(context);
    }

    public NonScrollableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NonScrollableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isScrollable && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isScrollable && super.onInterceptTouchEvent(ev);
    }

    // Method to enable or disable scrolling
    public void setScrollable(boolean scrollable) {
        this.isScrollable = scrollable;
    }
}
