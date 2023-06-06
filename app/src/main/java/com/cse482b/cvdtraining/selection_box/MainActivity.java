package com.cse482b.cvdtraining.selection_box;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.cse482b.cvdtraining.R;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    private CanvasView canvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selection_box);

        canvasView = findViewById(R.id.canvas);
        canvasView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                canvasView.setStartPoint((int) event.getX(), (int) event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                canvasView.setEndPoint((int) event.getX(), (int) event.getY());
                canvasView.invalidate(); // Redraw the canvas to update the box position
                break;
        }
        return true;
    }
}
