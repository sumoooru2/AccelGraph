package jp.ac.titech.itpro.sdl.accelgraph;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class GraphView extends View {

    private final static String TAG = "GraphView";
    private final static float Ymax = 20;
    private final static int NDATA_INIT = 256;

    private int ndata = NDATA_INIT;
    private float[] vs = new float[NDATA_INIT],
            vsw = new float[NDATA_INIT],
            vsl = new float[NDATA_INIT];
    private int idx = 0;
    private int x0, y0, ewidth;
    private int dw = 5, dh = 1;

    private final Paint paint = new Paint(), paintw = new Paint(), paintl = new Paint();

    public GraphView(Context context) {
        this(context, null);
    }

    public GraphView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GraphView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.i(TAG, "onSizeChanged: w=" + w + " h=" + h);
        ndata = w / dw;
        x0 = (w - dw * ndata) / 2;
        y0 = h / 2;
        ewidth = x0 + dw * (ndata - 1);

        if (y0 / Ymax >= dh + 1)
            dh = (int) (y0 / Ymax);
        if (ndata > vs.length) {
            idx = 0;
            vs = new float[ndata];
            vsw = new float[ndata];
            vsl = new float[ndata];
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // grid lines
        paint.setColor(Color.argb(75, 255, 255, 255));
        paint.setStrokeWidth(1);
        int h = canvas.getHeight();
        for (int y = y0; y < h; y += dh * 5)
            canvas.drawLine(x0, y, ewidth, y, paint);
        for (int y = y0; y > 0; y -= dh * 5)
            canvas.drawLine(x0, y, ewidth, y, paint);
        for (int x = x0; x < dw * ndata; x += dw * 5)
            canvas.drawLine(x, 0, x, h, paint);

        // graph
        paint.setColor(Color.YELLOW);
        paint.setStrokeWidth(2);
        paintw.setColor(Color.GREEN);
        paintw.setStrokeWidth(2);
        paintl.setColor(Color.BLUE);
        paintl.setStrokeWidth(2);
        for (int i = 0; i < ndata - 1; i++) {
            int j = (idx + i) % ndata;
            int x1 = x0 + dw * i;
            int x2 = x0 + dw * (i + 1);
            int y1 = (int) (y0 + dh * vs[j]);
            int y2 = (int) (y0 + dh * vs[(j + 1) % ndata]);
            int y1w = (int) (y0 + dh * vsw[j]);
            int y2w = (int) (y0 + dh * vsw[(j + 1) % ndata]);
            int y1l = (int) (y0 + dh * vsl[j]);
            int y2l = (int) (y0 + dh * vsl[(j + 1) % ndata]);
            canvas.drawLine(x1, y1, x2, y2, paint);
            canvas.drawLine(x1, y1w, x2, y2w, paintw);
            canvas.drawLine(x1, y1l, x2, y2l, paintl);
        }

        // y0 line
        paint.setColor(Color.CYAN);
        canvas.drawLine(0, y0, ewidth, y0, paint);
    }

    public void addData(float val, boolean invalidate) {
        vs[idx] = val;
        idx = (idx + 1) % ndata;
        if (invalidate)
            invalidate();
    }
    public void addDataWM(float val, boolean invalidate) {
        vsw[idx] = val;
//        idx = (idx + 1) % ndata;
//        if (invalidate)
//            invalidate();
    }
    public void addDataL(float val, boolean invalidate) {
        vsl[idx] = val / 32;
//        idx = (idx + 1) % ndata;
//        if (invalidate)
//            invalidate();
    }

}
