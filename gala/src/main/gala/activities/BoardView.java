package main.gala.activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import main.gala.common.Direction;
import main.gala.common.GameSettings;
import main.gala.core.AbstractManager;
import main.gala.exceptions.AmbiguousMoveException;

import java.util.ArrayList;
import java.util.List;

//TODO przerobić wszystko na animacje

/**
 * Klasa widoku odpowiedzialna za obsługę zdarzeń związanymi z interakcją użytkownika z planszą.
 * Służy do rysowania planszy na której rozgrywa się mecz.
 *
 * @author Maciej Andrearczyk
 */
public class BoardView extends View {

    private static Paint backgroundPaint;
    private static Paint linesPaint;
    private static Paint pencilPaint;
    private static Paint borderPaint;
    private static Paint topGoalPaint;
    private static Paint bottomGoalPaint;

    private static int paperWhiteColor = Color.rgb(250, 240, 210);
    private static int lightBlueColor = Color.rgb(176, 224, 230);
    private static int bottomPlayerColor = Color.rgb(189,183,107);
    private static int topPlayerColor = Color.rgb(0, 0, 112);
    private static int borderColor = Color.BLACK;

    private float currentX;
    private float currentY;
    private float startX;
    private float startY;

    private float screenHeight;
    private float screenWidth;
    private int boardHeight;
    private int boardWidth;
    private int goalWidth;

    private int gridSize;

    private List<Pair<Direction, Integer>> history = new ArrayList<>();


    private AbstractManager manager;

    static {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(paperWhiteColor);

        linesPaint = new Paint();
        linesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linesPaint.setStrokeWidth(4f);
        linesPaint.setColor(lightBlueColor);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        borderPaint.setStrokeWidth(5f);
        borderPaint.setColor(borderColor);

        pencilPaint = new Paint();
        pencilPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pencilPaint.setAntiAlias(true);
        pencilPaint.setStrokeWidth(5f);
        pencilPaint.setColor(bottomPlayerColor);

        topGoalPaint = new Paint();
        topGoalPaint.setColor(topPlayerColor);

        bottomGoalPaint = new Paint();
        bottomGoalPaint.setColor(bottomPlayerColor);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
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
                                history.add(new Pair(direction, pencilPaint.getColor()));
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
        float limiter = (screenHeight < screenWidth) ? screenHeight : screenWidth;
        limiter /= 10;
        return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1)) >= limiter;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int height = canvas.getHeight();
        int width = canvas.getWidth();

        gridSize = ((width < height) ? (width) : (height)) / GameSettings.CANVAS_SEPARATOR;

        drawMap(canvas);
        drawHistory(canvas);
        drawMessage(canvas);
    }

    /**
     * Rysuje gołą planszę.
     *
     * @param canvas canvas
     */
    private void drawMap(Canvas canvas) {
        drawNotebookPage(canvas);
        drawBoardBorders(canvas);
        drawMessage(canvas);
    }

    /**
     * Rysuje kontury zeszytu.
     *
     * @param canvas canvas
     */
    private void drawNotebookPage(Canvas canvas) {
        canvas.drawPaint(backgroundPaint);
        for (int i = 1; i <= canvas.getWidth(); i++) {
            canvas.drawLine(0, i * gridSize, canvas.getWidth(), i * gridSize, linesPaint);
        }
        for (int i = 1; i <= canvas.getHeight(); i++) {
            canvas.drawLine(i * gridSize, 0, i * gridSize, canvas.getHeight(), linesPaint);
        }
    }

    /**
     * Rysuje kontury planszy.
     *
     * @param canvas canvas
     */
    private void drawBoardBorders(Canvas canvas) {
        canvas.drawLine(1 * gridSize, 3 * gridSize, 1 * gridSize, 13 * gridSize, borderPaint); //lewa pionowa
        canvas.drawLine(9 * gridSize, 3 * gridSize, 9 * gridSize, 13 * gridSize, borderPaint); //prawa pionowa

        canvas.drawLine(4 * gridSize, 2 * gridSize, 4 * gridSize, 3 * gridSize, borderPaint); //lewa mala pionowa gorna
        canvas.drawLine(6 * gridSize, 2 * gridSize, 6 * gridSize, 3 * gridSize, borderPaint); //prawa mala pionowa gorna
        canvas.drawLine(4 * gridSize, 13 * gridSize, 4 * gridSize, 14 * gridSize, borderPaint); //lewa mala pionowa dolna
        canvas.drawLine(6 * gridSize, 13 * gridSize, 6 * gridSize, 14 * gridSize, borderPaint); //prawa mala pionowa dolna

        canvas.drawLine(4 * gridSize, 2 * gridSize, 6 * gridSize, 2 * gridSize, borderPaint); //pozioma gorna bramkowa
        canvas.drawLine(4 * gridSize, 14 * gridSize, 6 * gridSize, 14 * gridSize, borderPaint); //pozioma dolna bramkowa

        canvas.drawLine(1 * gridSize, 3 * gridSize, 4 * gridSize, 3 * gridSize, borderPaint); //pozioma gorna lewa
        canvas.drawLine(6 * gridSize, 3 * gridSize, 9 * gridSize, 3 * gridSize, borderPaint); //pozioma gorna prawa

        canvas.drawLine(1 * gridSize, 13 * gridSize, 4 * gridSize, 13 * gridSize, borderPaint); //pozioma dolna lewa
        canvas.drawLine(6 * gridSize, 13 * gridSize, 9 * gridSize, 13 * gridSize, borderPaint); //pozioma dolna prawa

        canvas.drawCircle(5 * gridSize, 8 * gridSize, 6, borderPaint); //punkt środkowy


        topGoalPaint.setAlpha(100);
        canvas.drawRect(4 * gridSize, 2 * gridSize, 6 * gridSize, 3 * gridSize, topGoalPaint);
        topGoalPaint.setAlpha(255);

        bottomGoalPaint.setAlpha(100);
        canvas.drawRect(4 * gridSize, 13 * gridSize, 6 * gridSize, 14 * gridSize, bottomGoalPaint);
    }

    /**
     * Rysuje informacje który gracz ma aktualnie ruch.
     *
     * @param canvas canvas
     */
    private void drawMessage(Canvas canvas) {
        float tx, ty;
        if (pencilPaint.getColor() == topPlayerColor) {
            tx = 4f;
            ty = 1f;
        } else {
            tx = 4f;
            ty = 15f;
        }
        pencilPaint.setTextSize(gridSize * 0.75f);
        canvas.drawText("move!", tx * gridSize, ty * gridSize, pencilPaint);
    }

    /**
     * Metoda służąca do rysowania na mapie dotychczasowej historii ruchów.
     *
     * @param canvas canvas
     */
    private void drawHistory(Canvas canvas) {
        float p = 5 * gridSize;
        float q = 8 * gridSize;
        int pencilColor = pencilPaint.getColor();

        for (Pair<Direction, Integer> pair : history) {
            float nextp = p + pair.first.getX() * gridSize;
            float nextq = q + pair.first.getY() * gridSize;
            pencilPaint.setColor(pair.second);
            canvas.drawLine(p, q, nextp, nextq, pencilPaint);
            p = nextp;
            q = nextq;
        }

        pencilPaint.setColor(pencilColor);
    }

    /**
     * Metoda wywoływana przez managera z zewnątrz informująca,
     * by zmienić ustawienia dotyczące gracza.
     */
    public void changePlayer() {
        int newColor = (pencilPaint.getColor() == topPlayerColor) ? bottomPlayerColor : topPlayerColor;
        pencilPaint.setColor(newColor);
    }

    /**
     * Ustawia managera nadrzędnego.
     *
     * @param manager manager
     */
    public void setManager(AbstractManager manager) {
        this.manager = manager;
    }

    /**
     * Ustawia szerokość bramki.
     *
     * @param goalWidth szerokość bramki
     */
    public void setGoalWidth(int goalWidth) {
        this.goalWidth = goalWidth;
    }

    /**
     * Ustawia szerokość boiska.
     *
     * @param boardWidth szerokość boiska
     */
    public void setBoardWidth(int boardWidth) {
        this.boardWidth = boardWidth;
    }

    /**
     * Ustawia wysokość boiska.
     *
     * @param boardHeight wysokość boiska
     */
    public void setBoardHeight(int boardHeight) {
        this.boardHeight = boardHeight;
    }
}
