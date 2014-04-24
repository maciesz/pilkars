package com.github.maciesz.gala.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.github.maciesz.gala.common.Direction;
import com.github.maciesz.gala.common.GameSettings;
import com.github.maciesz.gala.core.AbstractManager;
import com.github.maciesz.gala.exceptions.AmbiguousMoveException;

import java.util.ArrayList;
import java.util.List;

/**
 * Klasa widoku odpowiedzialna za obsługę zdarzeń związanymi z interakcją użytkownika z planszą.
 * Służy do rysowania planszy na której rozgrywa się mecz.
 *
 * @author Maciej Andrearczyk
 */
public class BoardView extends View {

    private float currentX;
    private float currentY;
    private float startX;
    private float startY;

    private float boardHeight;
    private float boardWidth;

    private List<Direction> history = new ArrayList<Direction>();
    private static Paint backgroundPaint;
    private static Paint linesPaint;
    private static Paint pencilPaint;
    private static Paint middlePointPaint;
    private static Paint pointPaint;

    private int gridSize;

    private AbstractManager manager;

    static {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.rgb(250,240,210));

        linesPaint = new Paint();
        linesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linesPaint.setStrokeWidth((float) 5);
        linesPaint.setColor(Color.rgb(176,224,230));

        pencilPaint = new Paint();
        pencilPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pencilPaint.setAntiAlias(true);
        pencilPaint.setStrokeWidth((float) 5);
        pencilPaint.setColor(Color.BLACK);

        middlePointPaint = pencilPaint;
//        middlePointPaint = new Paint();
//        middlePointPaint.setColor(Color.RED);

        pointPaint = pencilPaint;
//        pointPaint = new Paint();
//        pointPaint.setColor(Color.YELLOW);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("BoardView", "contructor2");
        boardHeight = context.getResources().getDisplayMetrics().heightPixels;
        boardWidth = context.getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (manager.isUserEnabled()) {
            float endX;
            float endY;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    endX = event.getX();
                    endY = event.getY();
                    if (isMoveEnoughLong(startX, startY, endX, endY)) {
                        currentX = endX - startX;
                        currentY = endY - startY;
                        try {
                            Direction direction = new Direction(currentX, currentY);
                            if (manager.isMoveLegal(direction)) {
                                manager.executeSingleMove(direction);
                                history.add(direction);
                            }
                            invalidate();
                        } catch (AmbiguousMoveException x) {
                            Log.d(BoardView.class.getCanonicalName(), "Ambiguous move exception!");
                        }
//                    Log.d(BoardView.class.getCanonicalName(), "onTouchEvent() DIFFERENCE" + " " +
//                            currentX + ", " + currentY);
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * Metoda sprawdzająca, czy ruch gracza po planszy był wystarczająco długi.
     * Wystarczająco - 10% minimum z wysokości i szerokości planszy
     *
     * @param x1 współrzędna x startu
     * @param y1 współrzędna y startu
     * @param x2 współrzędna x końca
     * @param y2 współrzędna y końca
     * @return prawda, jeżeli ruch był wystarczający długi
     */
    private boolean isMoveEnoughLong(float x1, float y1, float x2, float y2) {
        float limiter = (boardHeight < boardWidth) ? boardHeight : boardWidth;
        limiter /= 10;
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) >= limiter;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = canvas.getHeight();
        int width = canvas.getWidth();

        gridSize = ((width < height) ? (width) : (height)) / GameSettings.CANVAS_SEPARATOR;
        Log.d("BoardView", String.valueOf(gridSize));
        drawMap(canvas);

        drawHistory(canvas);
    }

    /**
     * Rysuje gołą planszę.
     *
     * @param canvas canvas
     */
    private void drawMap(Canvas canvas) {
        canvas.drawPaint(backgroundPaint);
        for (int i = 1; i <= canvas.getWidth(); i++) {
            canvas.drawLine(0, i * gridSize, canvas.getWidth(), i * gridSize, linesPaint);
        }
        for (int i = 1; i <= canvas.getHeight(); i++) {
            canvas.drawLine(i * gridSize, 0, i * gridSize, canvas.getHeight(), linesPaint);
        }

        canvas.drawLine(3 * gridSize, 5 * gridSize, 3 * gridSize, 15 * gridSize, pencilPaint); //lewa pionowa
        canvas.drawLine(11 * gridSize, 5 * gridSize, 11 * gridSize, 15 * gridSize, pencilPaint); //prawa pionowa

        canvas.drawLine(6 * gridSize, 4 * gridSize, 6 * gridSize, 5 * gridSize, pencilPaint); //lewa mala pionowa gorna
        canvas.drawLine(8 * gridSize, 4 * gridSize, 8 * gridSize, 5 * gridSize, pencilPaint); //prawa mala pionowa gorna
        canvas.drawLine(6 * gridSize, 15 * gridSize, 6 * gridSize, 16 * gridSize, pencilPaint);
        canvas.drawLine(8 * gridSize, 15 * gridSize, 8 * gridSize, 16 * gridSize, pencilPaint);

        canvas.drawLine(3 * gridSize, 5 * gridSize, 11 * gridSize, 5 * gridSize, pencilPaint);
        canvas.drawLine(3 * gridSize, 15 * gridSize, 11 * gridSize, 15 * gridSize, pencilPaint);

        canvas.drawLine(6 * gridSize, 4 * gridSize, 8 * gridSize, 4 * gridSize, pencilPaint);
        canvas.drawLine(6 * gridSize, 16 * gridSize, 8 * gridSize, 16 * gridSize, pencilPaint);
        canvas.drawLine(6 * gridSize, 5 * gridSize, 8 * gridSize, 5 * gridSize, linesPaint);
        canvas.drawLine(6 * gridSize, 15 * gridSize, 8 * gridSize, 15 * gridSize, linesPaint);
    }

    /**
     * Metoda służąca do rysowania na mapie dotychczasowej historii ruchów.
     *
     * @param canvas canvas
     */
    private void drawHistory(Canvas canvas) {
        float p = 7 * gridSize;
        float q = 10 * gridSize;
        canvas.drawCircle(p, q, 7, linesPaint);
        for (Direction aHistory : history) {
            float nextp = p + aHistory.getX() * gridSize;
            float nextq = q + aHistory.getY() * gridSize;
            canvas.drawLine(p, q, nextp, nextq, pencilPaint);
            canvas.drawCircle(p, q, 6, pointPaint);
            p = nextp;
            q = nextq;
        }
    }

    /**
     * Ustawia managera nadrzędnego.
     *
     * @param manager manager
     */
    public void setManager(AbstractManager manager) {
        this.manager = manager;
    }
}
