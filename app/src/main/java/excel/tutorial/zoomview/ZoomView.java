package excel.tutorial.zoomview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;

public class ZoomView extends SurfaceView {

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private Mode mode = Mode.NONE;

    private static final float minScale = 1.0f;
    private static final float maxScale = 5.0f;

    private float scale = 1.0f;

    private float startX = 0.0f;
    private float startY = 0.0f;

    private float dx = 0.0f;
    private float dy = 0.0f;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;

    private ScaleGestureDetector SGD;
    private float lastScaleFactor = 1.0f;

    public ZoomView(Context context) {
        super(context);
        init(context);
    }

    public ZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ZoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOnTouchListener(new MyTouchListeners());
        SGD = new ScaleGestureDetector(context, new ScaleListener());
    }

    private class MyTouchListeners implements View.OnTouchListener {

        MyTouchListeners() {
            super();
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (scale > minScale) {
                        mode = Mode.DRAG;
                        startX = motionEvent.getX() - prevDx;
                        startY = motionEvent.getY() - prevDy;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == Mode.DRAG) {
                        dx = motionEvent.getX() - startX;
                        dy = motionEvent.getY() - startY;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = Mode.ZOOM;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = Mode.NONE;
                    break;
                case MotionEvent.ACTION_UP:
                    mode = Mode.NONE;
                    prevDx = dx;
                    prevDy = dy;
                    break;
            }
            SGD.onTouchEvent(motionEvent);

            if ((mode == Mode.DRAG && scale >= minScale) || mode == Mode.ZOOM) {
                getParent().requestDisallowInterceptTouchEvent(true);
                float maxDx = getWidth() * scale;
                float maxDy = getHeight() * scale;
                dx = Math.min(Math.max(dx, -maxDx), 0);
                dy = Math.min(Math.max(dy, -maxDy), 0);
                applyScaleAndTranslation();
            }

            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleDetector) {
            float scaleFactor = scaleDetector.getScaleFactor();
            if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
                float prevScale = scale;
                scale *= scaleFactor;
                scale = Math.max(minScale, Math.min(scale, maxScale));
                lastScaleFactor = scaleFactor;
                float adjustedScaleFactor = scale / prevScale;
                float focusX = scaleDetector.getFocusX();
                float focusY = scaleDetector.getFocusY();
                dx += (dx - focusX) * (adjustedScaleFactor - 1.0f);
                dy += (dy - focusY) * (adjustedScaleFactor - 1.0f);
            } else {
                lastScaleFactor = 0;
            }
            return true;
        }
    }

    private void applyScaleAndTranslation() {
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setPivotX(0f);
        this.setPivotY(0f);
        this.setTranslationX(dx);
        this.setTranslationY(dy);
    }
}
