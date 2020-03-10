package com.coronovirus_stranding.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CoronovirusView extends View {

    private static final long SCANNER_ROTATING_SPEED = 1000;
    private static final long SCANNER_ANGLE = 30;
    private static final long DOT_LIFE_TIME = 3000;
    private static final int DOT_COUNT = 10;
    private static final int DOT_ALPHA = 200;

    protected Paint paint;
    protected Random random;
    protected List<Dot> dots;
    private Paint text;
    private Paint text2;
    private String textRadar = "SCAN";
    private String textRadar2 = "";

    private boolean isLoading;
    private boolean isAttached;

    public CoronovirusView(Context context) {
        super(context);
        init();
    }

    public CoronovirusView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CoronovirusView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dots = new ArrayList<>();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        long seed = System.currentTimeMillis();
        log("seed = " + seed);
        random = new Random(seed);
        text = new Paint();
        text.setColor(Color.GRAY);
        text.setTextSize(55f);
        text.setAntiAlias(true);
        text.setTextAlign(Paint.Align.CENTER);
        text2 = new Paint();
        text2.setColor(Color.BLACK);
        text2.setTextSize(35f);
        text2.setAntiAlias(true);
        text2.setTextAlign(Paint.Align.CENTER);
        isLoading = true;
    }

    protected void log(String msg) {
        Log.e(System.currentTimeMillis() + " - " + getClass().getSimpleName(), msg);
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        log("Toast \"" + msg + "\"");
    }

    protected float dpToPx(int dp) {
        return getContext().getResources().getDisplayMetrics().density * dp;
    }

    protected double getDistance(float x1, float y1, float x2, float y2) {
        return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        postInvalidate();
        isLoading = loading;
        dots.clear();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int viewWidth = getWidth();
        int viewHeight = getHeight();

        if ((viewWidth <= 0) || (viewHeight <= 0)) {
            return;
        }

        long frameTime = System.currentTimeMillis();
        float centerX = viewWidth * 0.5f;
        float centerY = viewHeight * 0.5f;

        float radius3 = Math.min(centerX, centerY);
        float radius2 = radius3 * 0.78f;
        float radius1 = radius3 * 0.56f;

        paint.setAlpha(90);
        canvas.drawCircle(centerX, centerY, radius1, paint);
        canvas.drawCircle(centerX, centerY, radius2, paint);
        canvas.drawCircle(centerX, centerY, radius3, paint);
        canvas.drawText(textRadar, centerX, centerY + 10, text);
        canvas.drawText(textRadar2, centerX, centerY + 45, text2);

        if (isLoading) {
            this.textRadar = "Scanning";
            this.textRadar2= "";
            float currentState = (frameTime % SCANNER_ROTATING_SPEED) / ((float) SCANNER_ROTATING_SPEED);
            canvas.drawArc(centerX - radius3, centerY - radius3,
                    centerX + radius3, centerY + radius3,
                    currentState * 360, SCANNER_ANGLE, true, paint);

        } else {
            float dotRadius = dpToPx(3);
            float dotDistance = radius3 - dotRadius;

            for (int i = dots.size() - 1; i >= 0; i--) {
                if ((dots.get(i).getShowTime() + DOT_LIFE_TIME) < frameTime) {
                    dots.remove(i);
                }
            }

            long delay = DOT_LIFE_TIME / DOT_COUNT;
            int addedCount = 0;
            while (dots.size() < DOT_COUNT) {
                float nextX = random.nextFloat();
                float nextY = random.nextFloat();
                if (getDistance(centerX, centerY,
                        centerX - radius3 + (nextX * 2 * radius3),
                        centerY - radius3 + (nextY * 2 * radius3)) < dotDistance) {
                    Dot dot = new Dot();
                    dot.setShowTime(frameTime + addedCount * delay);
                    dot.setX(nextX);
                    dot.setY(nextY);
                    dots.add(dot);
                    addedCount++;
                }
            }

            for (Dot dot : dots) {
                if (dot.getShowTime() <= frameTime) {
                    float alphaState = (frameTime - dot.getShowTime()) / ((float) DOT_LIFE_TIME);
                    if (alphaState < 0.2) {
                        paint.setAlpha((int) (DOT_ALPHA * alphaState * 5));
                    } else if (alphaState > 0.8) {
                        paint.setAlpha((int) (DOT_ALPHA * (1 - alphaState) * 5));
                    } else {
                        paint.setAlpha(DOT_ALPHA);
                    }
                    canvas.drawCircle(centerX - radius3 + (dot.getX() * 2 * radius3),
                            centerY - radius3 + (dot.getY() * 2 * radius3),
                            dotRadius,
                            paint);
                }
            }
        }
        if (isAttached) {
            postInvalidate();
        }
    }

    public void setTextRadar(String textRadar) {
        text.setColor(Color.BLACK);
        this.textRadar = textRadar+ "km";
        this.textRadar2 = "from Coronavirus";
        this.invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isAttached = true;
        postInvalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        isAttached = false;
        super.onDetachedFromWindow();
    }

    private static class Dot {
        private float x;
        private float y;
        private long showTime;

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public long getShowTime() {
            return showTime;
        }

        public void setShowTime(long showTime) {
            this.showTime = showTime;
        }
    }
}