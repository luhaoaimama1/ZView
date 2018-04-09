package com.zone.view.label;

import android.view.MotionEvent;

/**
 * MIT License
 * Copyright (c) [2018] [Zone]
 */

public class FilpRule extends Rule {
    @Override
    public void down(LabelView labelView, MotionEvent event, LabelView.LabelAttri temp) {
        labelView.isFlip(temp);
    }
}
