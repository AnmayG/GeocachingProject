package com.example.geocachingapp.ui.customViews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewOutlineProvider;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;

import com.example.geocachingapp.R;

public class CircleImageView extends AppCompatImageView {
    public CircleImageView(@NonNull Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setOutlineProvider(ViewOutlineProvider.BACKGROUND);
        setClipToOutline(true);
        setBackgroundResource(R.drawable.bg_circle);
        setScaleType(ScaleType.CENTER_CROP);
    }
}
