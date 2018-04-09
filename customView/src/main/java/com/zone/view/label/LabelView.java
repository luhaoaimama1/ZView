package com.zone.view.label;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.zone.customview.ninegridview.R;
import com.zone.view.label.util.DragZoomRorateUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 我最终完美的控件
 * todo   总结
 *
 * @author Zone
 */
public class LabelView extends View {
    /**
     * 这个是对于屏幕的属性 而不是真正VIEW中的宽高
     */
    private List<LabelAttri> labelAttri_list = new ArrayList<LabelAttri>();
    private Mode mode = Mode.NONE;
    private int width, height;
    //用来显示 bt的rect通过矩阵map后 的rect长的样子
    private boolean rimVisibility;

    private enum Mode {
        NONE, ZOOM, DRAG
    }

    /**
     * 0为00
     * 0---1---2
     * |       |
     * 7   8   3
     * |       |
     * 6---5---4
     */
    public class LabelAttri {
        //矩形永远不变  每次都计算  自己的位移 旋转 放大  最后的矩形范围
        MotionEvent firstDown, firstUp;//for doubleclick
        public boolean isFlip;
        public float downX = 0, downY = 0,
                offsetX = 0, offsetY = 0,
                offsetX_history = 0, offsetY_history = 0,
                scaleRadio = 1F, scaleRadio_history = 1,
                ratote = 0, ratoteHistory = 0,
                oriDis = 0, oriRatote = 0;
        public Matrix oriMatrix = new Matrix(), invert_Matrix = new Matrix();

        /**
         * ---------------------dst则是变化的 不变的属性 start------------------
         */
        public float centerLeft_first = 0, centerTop_first = 0;
        public Bitmap bitmap = null;
        public float[] srcPs, dstPs;
        public RectF rect_img_widget = new RectF(), dst_rect_img_widget = new RectF();

        /**
         * ---------------------不变的属性    end------------------
         */
        @Override
        public String toString() {
            log_("{downX:" + downX + ",downY" + downY + ",offsetX" + offsetX + ",offsetY" + offsetY + ",offsetX_history" + offsetX_history
                    + ",offsetY_history" + offsetY_history + ",centerLeft_first" + centerLeft_first + ",centerTop_first" + centerTop_first + "}");
            return super.toString();
        }

    }

    private RectF getMainBtRect(LabelAttri item) {

        float scaleRadioTotal = item.scaleRadio * item.scaleRadio_history;
        float rorateTotal = item.ratote + item.ratoteHistory;
        item.oriMatrix.reset();
        item.oriMatrix.postScale(scaleRadioTotal, scaleRadioTotal, item.rect_img_widget.centerX(), item.rect_img_widget.centerY());
        item.oriMatrix.postRotate(rorateTotal, item.rect_img_widget.centerX(), item.rect_img_widget.centerY());
        item.oriMatrix.postTranslate(item.offsetX + item.offsetX_history + 1F * width / 2 - item.rect_img_widget.width() / 2,
                item.offsetY + item.offsetY_history + 1F * height / 2 - item.rect_img_widget.height() / 2);

        item.oriMatrix.mapRect(item.dst_rect_img_widget, item.rect_img_widget);
        item.oriMatrix.mapPoints(item.dstPs, item.srcPs);
        return item.dst_rect_img_widget;
    }

    private Matrix getMainBtMaritx(LabelAttri item) {
        getMainBtRect(item);
        return item.oriMatrix;
    }

    private void removeLabel(LabelAttri labelAttri) {
        if (labelAttri.bitmap != null) {
            boolean deleteIsOk = true;
            for (LabelAttri item : labelAttri_list) {
                if (item == labelAttri) continue;

                if (item.bitmap == labelAttri.bitmap)
                    deleteIsOk = false;
            }
            if (deleteIsOk) {
                labelAttri.bitmap.recycle();
                labelAttri.bitmap = null;
            }
            labelAttri_list.remove(labelAttri);
        }
    }

    public void removeAllLabels() {
        for (LabelAttri item : labelAttri_list) {
            removeLabel(item);
        }
    }

    /**
     * 0为00
     * 0---1---2
     * |       |
     * 7   8   3
     * |       |
     * 6---5---4
     */
    public LabelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        Bitmap scaleBt = BitmapFactory.decodeResource(context.getResources(), R.drawable.fangda);
        Bitmap quitBt = BitmapFactory.decodeResource(context.getResources(), R.drawable.guanbi);
        Bitmap flipBt = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_flip);

        addRule(scaleBt, 2, new ScaleRule());
        addRule(quitBt, 6, new QuitRule());
        addRule(flipBt, 4, new FilpRule());
    }

    public LabelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LabelView(Context context) {
        this(context, null);
    }

    LabelAttri tempTouch = null;
    LabelAttri showHelper = null;
    private boolean quitDown = false;//触发事件  判断消耗用的

    //触摸点 通过逆向矩阵   找到图形最初 的点的位置
    private float[] getInvertEventPoint(LabelAttri labelAttri, MotionEvent event, int index) {
        float[] src = new float[]{event.getX(), event.getY()};
        float[] dst = new float[src.length];
        if (index == -1) {
            getMainBtMaritx(labelAttri).invert(labelAttri.invert_Matrix);
            labelAttri.invert_Matrix.mapPoints(dst, src);
        } else {
            getHelperMatrix(labelAttri, index).invert(rules[index].invert_Matrix);
            rules[index].invert_Matrix.mapPoints(dst, src);
        }
        return dst;
    }


    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    private static final int DOUBLE_TAP_MIN_TIME = 40;

    private boolean isConsideredDoubleTap(MotionEvent firstDown, MotionEvent firstUp,
                                          MotionEvent secondDown) {

        final long deltaTime = secondDown.getEventTime() - firstUp.getEventTime();
        log_("time:" + deltaTime);
        if (deltaTime > DOUBLE_TAP_TIMEOUT || deltaTime < DOUBLE_TAP_MIN_TIME) {
            log_("双击false");
            return false;
        }

        int deltaX = (int) firstDown.getX() - (int) secondDown.getX();
        int deltaY = (int) firstDown.getY() - (int) secondDown.getY();
        int slop = ViewConfiguration.get(getContext()).getScaledDoubleTapSlop();
        log_("双击计算：" + (deltaX * deltaX + deltaY * deltaY < slop * slop));
        return (deltaX * deltaX + deltaY * deltaY < slop * slop);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:// 手指压下屏幕

                //先判断 是碰到那个图片
                for (int i = labelAttri_list.size() - 1; i >= 0; i--) {
                    LabelAttri temp = labelAttri_list.get(i);
                    boolean isFirstTouch = showHelper == null || showHelper != temp;
                    if (isFirstTouch && isDrag(event, i, temp)) break;

                    if (showHelper != null && showHelper == temp) {

                        //不break 因为如果是拖拽的话 也可能是 辅助按钮的范围内 所以还有判断辅助按钮是否包含
                        boolean isDrag = isDrag(event, i, temp);

                        boolean isHelper = false;
                        for (int j = 0; j < rules.length; j++) {
                            if (rules[j] == null) continue;

                            float[] invertEventPoint = getInvertEventPoint(temp, event, j);
                            if (isHelper = rules[j].srcRect.contains(invertEventPoint[0], invertEventPoint[1])) {
                                rules[j].down(this, event, temp);
                                break;
                            }
                        }
                        if (isDrag || isHelper) break;
                    }
                }

                if (tempTouch == null)
                    showHelper = null;

                if (tempTouch != null && mOnDoubleTapListener != null) {
                    if (tempTouch.firstDown != null && tempTouch.firstUp != null &&
                            isConsideredDoubleTap(tempTouch.firstDown, tempTouch.firstUp, event)) {
                        mOnDoubleTapListener.onDoubleTap(labelAttri_list.indexOf(tempTouch));
                    }
                    //必须用MotionEvent.obtain 不然 双击计算为0
                    tempTouch.firstDown = MotionEvent.obtain(event);
                }

                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:// 手指在屏幕移动，该 事件会不断地触发
                if (tempTouch != null) {
                    if (mode == Mode.DRAG) {
                        tempTouch.offsetX = event.getX() - tempTouch.downX;
                        tempTouch.offsetY = event.getY() - tempTouch.downY;

                    } else if (mode == Mode.ZOOM) {// 缩放与旋转
                        //缩放
                        float tempDis = getDistanceFromRectCenter(getMainBtRect(showHelper), event);
                        tempTouch.scaleRadio = tempDis / tempTouch.oriDis;
                        //旋转
                        float tempRatote = getDegreesFromRectCenter(getMainBtRect(showHelper), event);
                        tempTouch.ratote = tempRatote - tempTouch.oriRatote;
                    }
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:// 手指离开屏
                mode = Mode.NONE;
                if (tempTouch != null) {
                    if (mOnDoubleTapListener != null)
                        tempTouch.firstUp = MotionEvent.obtain(event);
                    //记录一些值
                    tempTouch.offsetX_history += tempTouch.offsetX;
                    tempTouch.offsetX = 0;
                    tempTouch.offsetY_history += tempTouch.offsetY;
                    tempTouch.offsetY = 0;
                    tempTouch.scaleRadio_history *= tempTouch.scaleRadio;
                    tempTouch.scaleRadio = 1F;
                    tempTouch.ratoteHistory += tempTouch.ratote;
                    tempTouch.ratote = 0;
                    tempTouch = null;
                }
                break;
            default:
                break;
        }
        if (!quitDown) {
            if (tempTouch != null) {
                //点到了就消费  不传递了
                return true;
            } else {
                return false;
            }
        } else {
            //点击的时候消耗掉  消耗的时候还原了
            quitDown = false;
            return true;
        }
    }

    void isFlip(LabelAttri temp) {
        tempTouch = temp;
        temp.isFlip = !temp.isFlip;
    }

    void isQuit(LabelAttri temp) {
        quitDown = true;
        log_("碰到退出标志了");
        mode = Mode.NONE;
        removeLabel(temp);
        tempTouch = null;
        showHelper = null;
    }

    void isScale(MotionEvent event, LabelAttri temp) {
        mode = Mode.ZOOM;
        tempTouch = showHelper;
        tempTouch.oriDis = getDistanceFromRectCenter(temp.dst_rect_img_widget, event);
        tempTouch.oriRatote = getDegreesFromRectCenter(temp.dst_rect_img_widget, event);
        log_("碰到缩放标志了");
    }

    private boolean isDrag(MotionEvent event, int i, LabelAttri temp) {
        float[] main_invertEventPoint = getInvertEventPoint(temp, event, -1);
        if (temp.rect_img_widget.contains(main_invertEventPoint[0], main_invertEventPoint[1])) {
            log_("碰到标签了 index:" + i);
            mode = Mode.DRAG;
            //记录一些属性 与初始一些值
            temp.downX = event.getX();
            temp.downY = event.getY();
            tempTouch = temp;
            showHelper = temp;
            return true;
        }
        return false;
    }

    private static float getDistanceFromRectCenter(RectF rectf, MotionEvent event) {
        PointF start = new PointF(rectf.centerX(), rectf.centerY());
        PointF end = new PointF(event.getX(), event.getY());
        return DragZoomRorateUtils.distance(start, end);
    }

    private static float getDegreesFromRectCenter(RectF rectf, MotionEvent event) {
        PointF start = new PointF(rectf.centerX(), rectf.centerY());
        PointF end = new PointF(event.getX(), event.getY());
        return DragZoomRorateUtils.degrees(start, end);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (LabelAttri item : labelAttri_list) {
            Matrix btMatrix = getMainBtMaritx(item);
            if (item.isFlip) {
                Matrix matrix = new Matrix();
                matrix.postScale(-1, 1, item.bitmap.getWidth() / 2, item.bitmap.getHeight() / 2);
                btMatrix.preConcat(matrix);
            }
            canvas.drawBitmap(item.bitmap, btMatrix, null);
            if (showHelper != null && item == showHelper) {
                Paint paint = new Paint();
                paint.setColor(Color.WHITE);
                paint.setStrokeWidth(3);
                paint.setStyle(Style.STROKE);

                /**
                 * 0为00
                 * 0---1---2
                 * |       |
                 * 7   8   3
                 * |       |
                 * 6---5---4
                 *
                 * 这里必须通过 line绘制 不能通过rect绘制 因为mapRect后的Rect不是bt的Rect而与之相切的Rect
                 */
                float[] dstPs = item.dstPs;
                canvas.drawLine(dstPs[0], dstPs[1], dstPs[4], dstPs[5], paint);
                canvas.drawLine(dstPs[4], dstPs[5], dstPs[8], dstPs[9], paint);
                canvas.drawLine(dstPs[8], dstPs[9], dstPs[12], dstPs[13], paint);
                canvas.drawLine(dstPs[12], dstPs[13], dstPs[0], dstPs[1], paint);

                //绘制 MapRect之后 相切的Rect
                if (rimVisibility)
                    canvas.drawRect(item.dst_rect_img_widget, paint);

                //
                for (int i = 0; i < rules.length; i++) {
                    if (rules[i] != null)
                        canvas.drawBitmap(rules[i].bt, getHelperMatrix(item, i), null);
                }
            }
        }
        super.onDraw(canvas);
    }

    public void hideHelper() {
        showHelper = null;
        invalidate();
    }

    public Bitmap save() {
        return save(Config.ARGB_8888);
    }

    public Bitmap save(Config config) {
        Bitmap save = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(save);
        for (LabelAttri item : labelAttri_list)
            canvas.drawBitmap(item.bitmap, getMainBtMaritx(item), null);
        return save;
    }

    public int getLabelSize() {
        return labelAttri_list.size();
    }

    /**
     * 0为00
     * 0---1---2
     * |       |
     * 7   8   3
     * |       |
     * 6---5---4
     */
    public void addBitmap(Bitmap bt) {
        //当给map的时候布局已经好了
        LabelAttri labelAttri = new LabelAttri();
        //位图
        labelAttri.bitmap = bt;
        float mainBmpWidth = 1F * bt.getWidth();
        float mainBmpHeight = 1F * bt.getHeight();

        //最初的位置
        labelAttri.centerLeft_first = 1F * width / 2 - bt.getWidth() / 2;
        labelAttri.centerTop_first = 1F * height / 2 - bt.getHeight() / 2;

        labelAttri.srcPs = new float[]{
                0, 0,
                mainBmpWidth / 2, 0,
                mainBmpWidth, 0,
                mainBmpWidth, mainBmpHeight / 2,
                mainBmpWidth, mainBmpHeight,
                mainBmpWidth / 2, mainBmpHeight,
                0, mainBmpHeight,
                0, mainBmpHeight / 2,
                mainBmpWidth / 2, mainBmpHeight / 2
        };
        labelAttri.dstPs = new float[labelAttri.srcPs.length];
        //记录 Rect
        labelAttri.rect_img_widget.set(0, 0,
                bt.getWidth(), bt.getHeight());

        labelAttri_list.add(labelAttri);
        invalidate();
    }

    //rim边框
    public void setRimVisibility(boolean rim) {
        rimVisibility = rim;
        invalidate();
    }


    boolean openLog;

    public void debug(boolean openLog) {
        this.openLog = openLog;
    }

    private void log_(String str) {
        if (openLog)
            Log.i("LabelView", str);
    }


    private Rule[] rules = new Rule[9];

    public void addRule(Bitmap bt, int index, Rule rule) {
        rule.setBt(bt);
        rules[index] = rule;
    }

    /**
     * 0为00
     * 0---1---2
     * |       |
     * 7   8   3
     * |       |
     * 6---5---4
     * ps：使用之前必须使用 {@link #getMainBtRect(LabelAttri)}
     */
    private Matrix getHelperMatrix(LabelAttri item, int index) {
        float helperX = item.dstPs[index * 2];
        float helperY = item.dstPs[index * 2 + 1];

        rules[index].martix.reset();
        rules[index].martix.postTranslate(helperX - rules[index].srcRect.width() / 2,
                helperY - rules[index].srcRect.height() / 2);
        rules[index].martix.postRotate(item.ratote + item.ratoteHistory, helperX, helperY);
        rules[index].martix.mapRect(rules[index].dstRect, rules[index].srcRect);
        return rules[index].martix;
    }

    public interface OnDoubleTapListener {
        void onDoubleTap(int index);
    }

    OnDoubleTapListener mOnDoubleTapListener;

    public void addOnDoubleTapListener(OnDoubleTapListener onDoubleTapListener) {
        mOnDoubleTapListener = onDoubleTapListener;
    }

}
