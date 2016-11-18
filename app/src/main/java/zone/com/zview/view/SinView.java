package zone.com.zview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.zone.view.base.SurfaceViewTemplate;

public class SinView extends SurfaceViewTemplate {

    private int x = 0;
    private int y = 0;
    private Path mPath;
    private Paint mPaint;

    public SinView(Context context) {
        super(context);
    }

    public SinView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SinView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mPath.moveTo(0, 400);
        super.surfaceCreated(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        super.surfaceChanged(holder, format, width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
    }
    @Override
    protected void initView() {
        mPath = new Path();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void drawRunalbe(Canvas mCanvas) {
        // SurfaceView背景
        mCanvas.drawColor(Color.WHITE);
        mCanvas.drawPath(mPath, mPaint);
        x += 1;
        y = (int) (100 * Math.sin(x * 2 * Math.PI / 180) + 400);//把屏幕当成坐标系 最小的单元是1像素 那么x+1 y就能顺着这个公式改变
        mPath.lineTo(x, y);//path连上
    }

}
