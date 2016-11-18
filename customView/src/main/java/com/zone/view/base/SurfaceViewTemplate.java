package com.zone.view.base;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class SurfaceViewTemplate extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    // SurfaceHolder
    protected SurfaceHolder mHolder;
    // 用于绘图的Canvas
    private Canvas mCanvas;
    // 子线程标志位
    protected boolean mIsDrawing;

    public SurfaceViewTemplate(Context context) {
        super(context);
        init();
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
        mHolder = getHolder();
        mHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.setKeepScreenOn(true);
        initView();
        //mHolder.setFormat(PixelFormat.OPAQUE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mIsDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mIsDrawing = false;
    }

    @Override
    public void run() {
        while (mIsDrawing) {
            drawRunable();
        }
    }

    private void drawRunable() {
        try {
            mCanvas = mHolder.lockCanvas();
            drawRunalbe(mCanvas);
            // drawRunable sth
        } catch (Exception e) {
        } finally {
            if (mCanvas != null)
                mHolder.unlockCanvasAndPost(mCanvas);
        }
    }

    /**
     * surfaceCreated被创建后线程一直走此方法
     */
    protected abstract void drawRunalbe(Canvas mCanvas);
    /**
     *初始化的时候会走
     */
    protected abstract void initView();
}
