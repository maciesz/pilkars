package main.gala.activities;

import android.app.Activity;
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
import main.gala.common.StaticContent;
import main.gala.utils.Converter;
import main.gala.core.AbstractManager;
import main.gala.enums.GameState;
import main.gala.exceptions.AmbiguousMoveException;

import java.util.*;

/**
 * Klasa widoku odpowiedzialna za obsługę zdarzeń związanymi z interakcją użytkownika z planszą.
 * Służy do rysowania planszy na której rozgrywa się mecz.
 *
 * @author Maciej Andrearczyk
 */
public class BoardView extends View {

    /**
     * Struktura reprezentująca czwórkę liczb do rysowania linii.
     */
    private class LineParameters {
        public float x1;
        public float x2;
        public float y1;
        public float y2;

        private LineParameters(float x1, float y1, float x2, float y2) {
            this.y2 = y2;
            this.y1 = y1;
            this.x2 = x2;
            this.x1 = x1;
        }

    }

    private boolean isGameFinished;
    private BoardActivity parentActivity;
    private GameState gameState;
    /**
     * Liczba mówiąca na ile kratek jest podzielona plansza (licząc po szerokości).
     */
    private int canvasSeparator;

    private static Paint backgroundPaint;
    private static Paint linesPaint;
    private static Paint pencilPaint;
    private static Paint borderPaint;
    private static Paint topGoalPaint;
    private static Paint bottomGoalPaint;
    private static Paint currentPointPaint;

    private static int paperWhiteColor = Color.rgb(250, 240, 210);
    private static int lightBlueColor = Color.rgb(176, 224, 230);
    private int bottomPlayerColor;
    private int topPlayerColor;
    private static int borderColor = Color.BLACK;
    private static int currentPointColor = Color.RED;

    private float currentX;
    private float currentY;
    private float startX;
    private float startY;

    private float screenHeight;
    private float screenWidth;
    private int boardHeight;
    private int boardWidth;
    private int goalWidth;

    private float circleSize = 6f;
    private static float DEFAULT_CIRCLE_SIZE = 6f;
    private static float DEFAULT_LINES_PAINT_SIZE = 4f;
    private static float DEFAULT_BORDER_PAINT_SIZE = 5f;
    private static float DEFAULT_PENCIL_PAINT_SIZE = 6f;
    private static float DEFAULT_CANVAS_HEIGHT = 1920f;

    private int gridSize;

    private Deque<Pair<Direction, Integer>> aiPlayerMoves;
    private String lastPlayer;

    private List<Pair<Direction, Integer>> history = new ArrayList<>();

    private AbstractManager manager;

    static {
        backgroundPaint = new Paint();
        backgroundPaint.setColor(paperWhiteColor);

        linesPaint = new Paint();
        linesPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        linesPaint.setStrokeWidth(DEFAULT_LINES_PAINT_SIZE);
        linesPaint.setColor(lightBlueColor);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        borderPaint.setStrokeWidth(DEFAULT_BORDER_PAINT_SIZE);
        borderPaint.setColor(borderColor);

        pencilPaint = new Paint();
        pencilPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        pencilPaint.setAntiAlias(true);
        pencilPaint.setStrokeWidth(DEFAULT_PENCIL_PAINT_SIZE);

        topGoalPaint = new Paint();

        bottomGoalPaint = new Paint();

        currentPointPaint = new Paint();
        currentPointPaint.setColor(currentPointColor);
    }

    public BoardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        isGameFinished = false;
        pencilPaint.setColor(bottomPlayerColor);
        screenHeight = context.getResources().getDisplayMetrics().heightPixels;
        screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        aiPlayerMoves = new LinkedList<>();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (manager.isUserEnabled() &&
                !isGameFinished &&
                aiPlayerMoves.isEmpty()) {
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
                            final Direction convertedDirection = Converter.cuseMVConversion(direction);
                            if (manager.isMoveLegal(convertedDirection)) {
                                history.add(new Pair(direction, pencilPaint.getColor()));
                                invalidate();
                                manager.executeSingleMove(convertedDirection);
                            }
                        } catch (AmbiguousMoveException x) {
                            Log.d(BoardView.class.getCanonicalName(), "Ambiguous move exception!");
                        }
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
        setPaintToolsSizes(canvas);
        int width = canvas.getWidth();
        canvasSeparator = boardWidth + 2;

        if (boardHeight - boardWidth > 2) {
            canvasSeparator += 2;
        }

        gridSize = width / canvasSeparator;

        drawMap(canvas);
        drawHistory(canvas);
        drawMessage(canvas);
        resolveGameState();
    }

    /**
     * Ustawia grubości linii, okręgów.
     *
     * @param canvas canvas
     */
    private void setPaintToolsSizes(Canvas canvas) {
        int height = canvas.getHeight();

        float diff = height/DEFAULT_CANVAS_HEIGHT;
        borderPaint.setStrokeWidth(diff * DEFAULT_BORDER_PAINT_SIZE);
        linesPaint.setStrokeWidth(diff * DEFAULT_LINES_PAINT_SIZE);
        pencilPaint.setStrokeWidth(diff * DEFAULT_PENCIL_PAINT_SIZE);
        circleSize = diff * DEFAULT_CIRCLE_SIZE;
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
        int widthDistance = (canvasSeparator - boardWidth) / 2;
        List<LineParameters> lines = new LinkedList<>();

        lines.add(new LineParameters(widthDistance * gridSize, 3 * gridSize,
                widthDistance * gridSize, (3 + boardHeight) * gridSize)); //lewa pionowa
        lines.add(new LineParameters((widthDistance + boardWidth) * gridSize, 3 * gridSize,
                (widthDistance + boardWidth) * gridSize, (3 + boardHeight) * gridSize)); //prawa pionowa

        lines.add(new LineParameters((widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize,
                2 * gridSize, (widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize, 3 * gridSize)); //lewa mala pionowa gorna
        lines.add(new LineParameters((widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize,
                2 * gridSize, (widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize, 3 * gridSize)); //prawa mala pionowa gorna
        lines.add(new LineParameters((widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize, (3 + boardHeight) * gridSize,
                (widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize, (4 + boardHeight) * gridSize)); //lewa mala pionowa dolna
        lines.add(new LineParameters((widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize, (3 + boardHeight) * gridSize,
                (widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize, (4 + boardHeight) * gridSize)); //prawa mala pionowa dolna

        lines.add(new LineParameters((widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize, 2 * gridSize,
                (widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize, 2 * gridSize)); //pozioma gorna bramkowa
        lines.add(new LineParameters((widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize, (4 + boardHeight) * gridSize,
                (widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize, (4 + boardHeight) * gridSize)); //pozioma dolna bramkowa

        lines.add(new LineParameters(widthDistance * gridSize, 3 * gridSize,
                (widthDistance + (boardWidth - goalWidth) / 2) * gridSize, 3 * gridSize)); //pozioma gorna lewa
        lines.add(new LineParameters(((widthDistance + (boardWidth + goalWidth) / 2)) * gridSize, 3 * gridSize,
                (boardWidth + widthDistance) * gridSize, 3 * gridSize)); //pozioma gorna prawa

        lines.add(new LineParameters(widthDistance * gridSize, (3 + boardHeight) * gridSize,
                (widthDistance + (boardWidth - goalWidth) / 2) * gridSize, (3 + boardHeight) * gridSize)); //pozioma dolna lewa
        lines.add(new LineParameters((widthDistance + (boardWidth + goalWidth) / 2) * gridSize,
                (3 + boardHeight) * gridSize, (boardWidth + widthDistance) * gridSize, (3 + boardHeight) * gridSize)); //pozioma dolna prawa

        for (LineParameters lp : lines) {
            canvas.drawLine(lp.x1, lp.y1, lp.x2, lp.y2, borderPaint);
        }
        canvas.drawCircle((widthDistance + boardWidth / 2) * gridSize, (6 + boardHeight) / 2 * gridSize, circleSize, borderPaint); //punkt środkowy

        topGoalPaint.setAlpha(100);
        canvas.drawRect((widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize, 2 * gridSize, (widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize, 3 * gridSize, topGoalPaint);
        topGoalPaint.setAlpha(255);

        bottomGoalPaint.setAlpha(100);
        canvas.drawRect((widthDistance + boardWidth / 2 - goalWidth / 2) * gridSize, (3 + boardHeight) * gridSize, (widthDistance + boardWidth / 2 + goalWidth / 2) * gridSize, (4 + boardHeight) * gridSize, bottomGoalPaint);
    }

    /**
     * Rysuje informacje który gracz ma aktualnie ruch.
     *
     * @param canvas canvas
     */
    private void drawMessage(Canvas canvas) {
        if (aiPlayerMoves.isEmpty()) {
            float tx, ty;
            if (pencilPaint.getColor() == topPlayerColor) {
                tx = (boardWidth / 2f) + (canvasSeparator - boardWidth) / 2 - 1;
                ty = 1.6f;
            } else {
                tx = (boardWidth / 2f) + (canvasSeparator - boardWidth) / 2 - 1;
                ty = 4.7f + boardHeight;
            }
            pencilPaint.setTextSize(gridSize * 0.75f);
            canvas.drawText("move!", tx * gridSize, ty * gridSize, pencilPaint);
        }
    }

    /**
     * Metoda służąca do rysowania na mapie dotychczasowej historii ruchów.
     *
     * @param canvas canvas
     */
    private void drawHistory(final Canvas canvas) {
        float p = ((canvasSeparator - boardWidth) / 2 + boardWidth / 2) * gridSize;
        float q = (6 + boardHeight) / 2 * gridSize;
        int pencilColor = pencilPaint.getColor();

        for (int i = 0; i < history.size(); ++i) {
            Pair<Direction, Integer> pair = history.get(i);
            float p2 = p + pair.first.getX() * gridSize;
            float q2 = q + pair.first.getY() * gridSize;

            pencilPaint.setColor(pair.second);
            canvas.drawLine(p, q, p2, q2, new Paint(pencilPaint));
            p = p2;
            q = q2;
        }

        if (!aiPlayerMoves.isEmpty()) {
            Pair<Direction, Integer> pair = aiPlayerMoves.pollFirst();
            float p2 = p + pair.first.getX() * gridSize;
            float q2 = q + pair.first.getY() * gridSize;

            pencilPaint.setColor(pair.second);
            history.add(pair);
            canvas.drawLine(p, q, p2, q2, new Paint(pencilPaint));
            canvas.drawCircle(p2, q2, circleSize, currentPointPaint);
            postInvalidateDelayed(StaticContent.animationDelay);
        }

        canvas.drawCircle(p, q, circleSize, currentPointPaint);
        pencilPaint.setColor(pencilColor);
    }

    /**
     * Metoda wywoływana przez managera z zewnątrz informująca,
     * by zmienić ustawienia dotyczące gracza.
     */
    public void changePlayer() {
        int newColor = (pencilPaint.getColor() == topPlayerColor) ? bottomPlayerColor : topPlayerColor;
        pencilPaint.setColor(newColor);
        //
        String s = (pencilPaint.getColor() == topPlayerColor) ?
                StaticContent.TOP_PLAYER : StaticContent.BOTTOM_PLAYER;
        Log.d(BoardView.class.getCanonicalName(), "player changed - " + s);
    }

    /**
     * Rysuje żądaną sekwencje ruchów na planszy.
     *
     * @param moveSequence sekwencje ruchów
     */
    public void drawSequence(Collection<Direction> moveSequence) {
        for (Direction direction : moveSequence) {
            aiPlayerMoves.add(new Pair<>(direction, topPlayerColor));
        }
        invalidate();
    }

    /**
     * Ustawia aktualny stan gry.
     *
     * @param gameState
     */
    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        lastPlayer = (pencilPaint.getColor() == topPlayerColor) ?
                StaticContent.TOP_PLAYER : StaticContent.BOTTOM_PLAYER;

        Log.d(BoardView.class.getCanonicalName(), "game state - " + gameState + "| current player - " + lastPlayer);
    }

    /**
     * Analizuje stan gry.
     */
    public void resolveGameState() {
        if (aiPlayerMoves.isEmpty() && !isGameFinished) { //unikamy błędu pokazania dialogu przed zakończeniem animacji
            if (gameState == GameState.DEFEATED || gameState == GameState.BLOCKED || gameState == GameState.VICTORIOUS) {
                isGameFinished = true;
                parentActivity.showEndGameDialog(gameState, lastPlayer);
            }
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

    /**
     * Ustawia aktywność - rodzica,
     *
     * @param parentActivity rodzic
     */
    public void setParentActivity(BoardActivity parentActivity) {
        this.parentActivity = parentActivity;
    }

    /**
     * Czyści wartości stałych związane z grą.
     */
    public void clearGameProgress() {
        history = new ArrayList<>();
        gameState = GameState.OBLIGATORY_MOVE;
    }

    /**
     * Ustawia informację, czy aktualna rozgrywka zostala juz zakonczona.
     *
     * @param isGameFinished informacja o tym, czy gra juz zostala zakonczona
     */
    public void setGameFinished(boolean isGameFinished) {
        this.isGameFinished = isGameFinished;
    }

    /**
     * Przywraca widok do stanu początkowego.
     */
    public void reset() {
        setGameFinished(false);
        pencilPaint.setColor(bottomPlayerColor);
        clearGameProgress();
        invalidate();
    }

    /**
     * Ustawia kolor krechy dla dolnego gracza.
     *
     * @param bottomPlayerColor kolor krechy
     */
    public void setBottomPlayerColor(int bottomPlayerColor) {
        this.bottomPlayerColor = bottomPlayerColor;
        bottomGoalPaint.setColor(bottomPlayerColor);
    }

    /**
     * Ustawia kolor krechy dla górnego gracza.
     *
     * @param topPlayerColor kolor krechy
     */
    public void setTopPlayerColor(int topPlayerColor) {
        this.topPlayerColor = topPlayerColor;
        topGoalPaint.setColor(topPlayerColor);
    }
}
