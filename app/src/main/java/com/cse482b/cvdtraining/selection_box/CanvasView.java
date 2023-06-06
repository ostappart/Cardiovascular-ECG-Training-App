package com.cse482b.cvdtraining.selection_box;

import static java.lang.Double.parseDouble;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class CanvasView extends View {

    private int startX, startY, endX, endY;
    private Paint paint;
    private TextView textView;

    public CanvasView(Context context) {
        super(context);
        init();
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);

        textView = new TextView(getContext());
        textView.setTextSize(18);
        textView.setTextColor(Color.RED);
        textView.setVisibility(INVISIBLE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int left = Math.min(startX, endX);
        int top = Math.min(startY, endY);
        int right = Math.max(startX, endX);
        int bottom = Math.max(startY, endY);

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        double rleft = left * 100.0 / getWidth();
        rleft = parseDouble(df.format(rleft));
        double rtop = top * 100.0 / getHeight();
        rtop = parseDouble(df.format(rtop));
        double rright = right * 100.0 / getWidth();
        rright = parseDouble(df.format(rright));
        double rbottom = bottom * 100.0 / getHeight();
        rbottom = parseDouble(df.format(rbottom));


        canvas.drawRect(left, top, right, bottom, paint);

        String text = "(" + left + ", " + top + ") - (" + right + ", " + bottom + ")\n" +
                "(" + rleft + "%, " + rtop + "%) - (" + rright + "%, " + rbottom + "%)";
        textView.setText(text);
        textView.measure(0, 0);
        int textViewWidth = textView.getMeasuredWidth();
        int textViewHeight = textView.getMeasuredHeight();
        int canvasTextX = getWidth() / 2;
        int canvasTextY = getHeight() / 8;
        textView.setX(canvasTextX - textViewWidth / 2);
        textView.setY(canvasTextY - textViewHeight / 2);
        ((ViewGroup) getParent()).addView(textView);
        textView.setVisibility(VISIBLE);
    }

    public void setStartPoint(int x, int y) {
        startX = x;
        startY = y;
        ((ViewGroup) getParent()).removeView(textView);
        textView.setVisibility(INVISIBLE);
    }

    public void setEndPoint(int x, int y) {
        endX = x;
        endY = y;
        ((ViewGroup) getParent()).removeView(textView);
        textView.setVisibility(INVISIBLE);
    }

}
