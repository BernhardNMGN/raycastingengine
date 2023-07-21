package renderers.topdown;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import renderers.interfaces.HudRenderer;
import resources.Player;
import resources.map.GameMap;
import settings.Settings;

import java.text.NumberFormat;

public class HudRendererGeneral implements HudRenderer {

    private Color backGroundColor;
    private Color textColor;
    private Color boxFillColor;

    private double hudHeight;
    private double boxHeight;
    private double boxWidth;
    private double marginRatio;

    private double outerMargin;
    private double innerMargin;

    private Point2D drawCoords;
    private NumberFormat number;

    private int boxCounter;

    private GraphicsContext gc;


    public HudRendererGeneral(GraphicsContext gc) {
        this.gc = gc;
        setDefaultColors();
        this.hudHeight = Settings.DEFAULT_HUD_HEIGHT;
        this.boxHeight = Settings.DEFAULT_HUD_BOX_HEIGHT;
        this.boxWidth = Settings.DEFAULT_HUD_BOX_WIDTH;
        this.marginRatio = Settings.DEFAULT_HUD_MARGIN_RATIO;
        this.outerMargin = hudHeight * marginRatio;
        this.number = NumberFormat.getInstance();
    }

    @Override
    public void draw(Player player, GameMap map, Point2D cursor) {
        boxCounter = 0;
        drawCoords = new Point2D(0., Settings.VERTICAL_RESOLUTION - hudHeight);
        drawBackGround(gc);
        drawBoxes(gc, player, map, cursor);
    }

    private void drawBackGround(GraphicsContext gc) {
        gc.setFill(backGroundColor);
        gc.fillRect(drawCoords.getX(), drawCoords.getY(), Settings.HORIZONTAL_RESOLUTION, hudHeight);
        drawCoords = drawCoords.add(outerMargin, outerMargin);
    }

    private void drawBoxes(GraphicsContext gc, Player player, GameMap map, Point2D cursor) {
        drawPlayerBox(gc, player);
        drawCursorBox(gc, player, cursor);
        drawMapBox(gc, map);
    }

    private void drawCursorBox(GraphicsContext gc, Player player, Point2D cursor) {
        number.setMaximumFractionDigits(2);
        String cursorCoords = "Actual Cursor Position: " + number.format(cursor.getX()) + ", " + number.format(cursor.getY());
//        String scaledCursorCoords = "Scaled Cursor Position: " + number.format(player.getScaledCursor().getX()) + ", " + number.format(player.getScaledCursor().getY());
        drawTextBox(gc, cursorCoords);
    }

    private void drawMapBox(GraphicsContext gc, GameMap map) {
    }

    private void drawPlayerBox(GraphicsContext gc, Player player) {
        number.setMaximumFractionDigits(1);
        String playerCoords = "Player Position: " + number.format(player.getPlayerCoords().getX()) + ", " + number.format(player.getPlayerCoords().getY());
        number.setMaximumFractionDigits(2);
        String angle = "Player Angle: " + number.format(player.getPlayerDir()) + "\u00B0";
        drawTextBox(gc, playerCoords,  angle);
    }

    private void drawTextBox(GraphicsContext gc, String... text) {
        gc.setFill(boxFillColor);
        gc.setStroke(textColor);
        int nrOfLines = text.length;
        double textHeight = boxHeight / nrOfLines;
        double margin = textHeight * marginRatio;

        gc.fillRect(drawCoords.getX(), drawCoords.getY(), boxWidth, boxHeight);
        drawCoords = drawCoords.add(margin, margin);
        gc.setFill(textColor);
        for(String line : text) {
            gc.fillText(line, drawCoords.getX(), drawCoords.getY() + textHeight/2., boxWidth - margin*2);
            drawCoords = drawCoords.add(0., textHeight);
        }
        boxCounter++;
        drawCoords = setDrawCoords();
    }

    private Point2D setDrawCoords() {
        double x = boxCounter * (boxWidth + outerMargin) + outerMargin;
        double y = Settings.VERTICAL_RESOLUTION - hudHeight + outerMargin;
        return new Point2D(x, y);
    }

    @Override
    public void setDefaultColors() {
        this.backGroundColor = Color.CHOCOLATE;
        this.textColor = Color.DARKCYAN;
        this.boxFillColor = Color.BEIGE;
    }
}
