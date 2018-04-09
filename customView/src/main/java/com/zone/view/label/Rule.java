package com.zone.view.label;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.view.MotionEvent;

/**
 * MIT License
 * Copyright (c) [2018] [Zone]
 */

public abstract class Rule {
    Bitmap bt;
    RectF srcRect = new RectF(), dstRect = new RectF();
    Matrix martix = new Matrix(), invert_Matrix = new Matrix();


    public abstract void down(LabelView labelView,MotionEvent event, LabelView.LabelAttri temp);




    public Bitmap getBt() {
        return bt;
    }

    public void setBt(Bitmap bt) {
        this.bt = bt;
        srcRect.set(0, 0, bt.getWidth(), 1F * bt.getHeight());
    }

    public RectF getSrcRect() {
        return srcRect;
    }

    public void setSrcRect(RectF srcRect) {
        this.srcRect = srcRect;
    }

    public RectF getDstRect() {
        return dstRect;
    }

    public void setDstRect(RectF dstRect) {
        this.dstRect = dstRect;
    }

    public Matrix getMartix() {
        return martix;
    }

    public void setMartix(Matrix martix) {
        this.martix = martix;
    }

    public Matrix getInvert_Matrix() {
        return invert_Matrix;
    }

    public void setInvert_Matrix(Matrix invert_Matrix) {
        this.invert_Matrix = invert_Matrix;
    }

}
