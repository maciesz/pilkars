package main.gala.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import main.gala.common.GameSettings;

/**
 * Klasa służąca do rysowania tła w menu.
 *
 * Maciej Andrearczyk <maciej.andrearczyk@student.mimuw.edu.pl>
 */
public class BackgroundView extends View {

    private static Paint backgroundPaint;
    private static Paint linesPaint;
    private static int paperWhiteColor = Color.rgb(250, 240, 210);
    private static int lightBlueColor = Color.rgb(176, 224, 230);
    private int gridSize;

    static {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(paperWhiteColor);

        linesPaint = new Paint();
        linesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linesPaint.setStrokeWidth(4f);
        linesPaint.setColor(lightBlueColor);
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = canvas.getHeight();
        int width = canvas.getWidth();

        gridSize = ((width < height) ? (width) : (height)) / GameSettings.CANVAS_SEPARATOR;

        canvas.drawPaint(backgroundPaint);
        for (int i = 1; i <= canvas.getWidth(); i++) {
            canvas.drawLine(0, i * gridSize, canvas.getWidth(), i * gridSize, linesPaint);
        }
        for (int i = 1; i <= canvas.getHeight(); i++) {
            canvas.drawLine(i * gridSize, 0, i * gridSize, canvas.getHeight(), linesPaint);
        }
    }
}
