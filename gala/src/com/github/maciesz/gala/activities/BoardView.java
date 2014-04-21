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
    private static Paint grayPaint;
    private static Paint whitePaint;
    private static Paint bluePaint;
    private static Paint redPaint;
    private static Paint yellowPaint;

    private int gridSize;

    private AbstractManager manager;

    static {
        grayPaint = new Paint();
        grayPaint.setColor(Color.DKGRAY);

        whitePaint = new Paint();
        whitePaint.setColor(Color.WHITE);

        bluePaint = new Paint();
        bluePaint.setColor(Color.BLUE);

        redPaint = new Paint();
        redPaint.setColor(Color.RED);

        yellowPaint = new Paint();
        yellowPaint.setColor(Color.YELLOW);
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

        drawHistory(redPaint, canvas);
    }

    /**
     * Rysuje gołą planszę.
     *
     * @param canvas canvas
     */
    private void drawMap(Canvas canvas) {
        canvas.drawColor(Color.DKGRAY);
        for (int i = 1; i <= canvas.getWidth(); i++) {
            canvas.drawLine(0, i * gridSize, canvas.getWidth(), i * gridSize, whitePaint);
        }
        for (int i = 1; i <= canvas.getHeight(); i++) {
            canvas.drawLine(i * gridSize, 0, i * gridSize, canvas.getHeight(), whitePaint);
        }

        canvas.drawLine(4 * gridSize, 5 * gridSize, 4 * gridSize, 15 * gridSize, bluePaint);
        canvas.drawLine(12 * gridSize, 5 * gridSize, 12 * gridSize, 15 * gridSize, bluePaint);

        canvas.drawLine(7 * gridSize, 4 * gridSize, 7 * gridSize, 5 * gridSize, bluePaint);
        canvas.drawLine(9 * gridSize, 4 * gridSize, 9 * gridSize, 5 * gridSize, bluePaint);
        canvas.drawLine(7 * gridSize, 15 * gridSize, 7 * gridSize, 16 * gridSize, bluePaint);
        canvas.drawLine(9 * gridSize, 15 * gridSize, 9 * gridSize, 16 * gridSize, bluePaint);

        canvas.drawLine(4 * gridSize, 5 * gridSize, 12 * gridSize, 5 * gridSize, bluePaint);
        canvas.drawLine(4 * gridSize, 15 * gridSize, 12 * gridSize, 15 * gridSize, bluePaint);

        canvas.drawLine(7 * gridSize, 4 * gridSize, 9 * gridSize, 4 * gridSize, bluePaint);
        canvas.drawLine(7 * gridSize, 16 * gridSize, 9 * gridSize, 16 * gridSize, bluePaint);
        canvas.drawLine(7 * gridSize, 5 * gridSize, 9 * gridSize, 5 * gridSize, whitePaint);
        canvas.drawLine(7 * gridSize, 15 * gridSize, 9 * gridSize, 15 * gridSize, whitePaint);
    }

    /**
     * Metoda służąca do rysowania na mapie dotychczasowej historii ruchów.
     *
     * @param paint  obiekt paintera
     * @param canvas canvas
     */
    private void drawHistory(Paint paint, Canvas canvas) {
        float p = 8 * gridSize;
        float q = 10 * gridSize;
        canvas.drawCircle(p, q, 5, paint);
        for (Direction aHistory : history) {
            float nextp = p + aHistory.getX() * gridSize;
            float nextq = q + aHistory.getY() * gridSize;
            canvas.drawLine(p, q, nextp, nextq, paint);
            canvas.drawCircle(p, q, 3, yellowPaint);
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
