package com.coronovirus_stranding.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class CircleView extends View {

    private static final String COLOR_HEX = "#F7E7D2";
    private final Paint drawPaint;
    private final Paint drawPaint1;
    private final Paint drawPaint2;
    private float size;
    private final Paint text;
    private final Paint text2;
    private String textRadar = "SCAN";
    private String textRadar2 = "";

    public CircleView(Context context, final AttributeSet attributeSet) {
        super(context, attributeSet);
        drawPaint = new Paint();
        drawPaint.setColor(Color.parseColor(COLOR_HEX));
        drawPaint1 = new Paint();
        drawPaint1.setColor(Color.parseColor("#EECD9E"));
        drawPaint2 = new Paint();
        drawPaint2.setColor(Color.parseColor("#E8BF7F"));
        text = new Paint();
        text.setColor(Color.BLACK);
        text.setTextSize(55f);
        text.setAntiAlias(true);
        text.setTextAlign(Paint.Align.CENTER);

        text2 = new Paint();
        text2.setColor(Color.BLACK);
        text2.setTextSize(35f);
        text2.setAntiAlias(true);
        text2.setTextAlign(Paint.Align.CENTER);
        Rect bounds = new Rect();
        text.getTextBounds(textRadar2, 0, textRadar2.length(), bounds);
        setOnMeasureCallback();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(size, size, size, drawPaint2);
        canvas.drawCircle(size, size, size - 50, drawPaint1);
        canvas.drawCircle(size, size, size - 100, drawPaint);
        canvas.drawText(textRadar, size, size + 20, text);
        canvas.drawText(textRadar2, size, size + 55, text2);
    }

    public void setTextRadar(String textRadar,String colorText, String color1, String color2, String color3) {
        this.textRadar = textRadar;
        if (!TextUtils.isEmpty(textRadar)){
            this.textRadar2 ="from coronavirus";
        }else {
            this.textRadar2 ="";
        }
        text.setColor(Color.parseColor(colorText));
        text2.setColor(Color.parseColor(colorText));
        drawPaint2.setColor(Color.parseColor(color1));
        drawPaint1.setColor(Color.parseColor(color2));
        drawPaint.setColor(Color.parseColor(color3));
        this.invalidate();
    }

    private void setOnMeasureCallback() {
        ViewTreeObserver vto = getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                removeOnGlobalLayoutListener(this);
                size = getMeasuredWidth() / 2;
            }
        });
    }

    private void removeOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener listener) {
        getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }
}
