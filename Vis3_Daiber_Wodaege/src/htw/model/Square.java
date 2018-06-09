package htw.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Square {

    private double sideLength;
    private Color color;

    public Square() {
        this.sideLength = 0;
        this.color = Color.RED;

    }

    public Square(double sideLength, Color color) {

        this.sideLength = sideLength;
        this.color = color;
    }

    public double getSideLength() {
        return sideLength;
    }

    public void setSideLength(float sideLength) {
        this.sideLength = sideLength;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void draw(GraphicsContext gc, double xPos, double yPos)
    {
        gc.setFill(color);
        gc.fillRect(xPos, yPos, sideLength, sideLength);
    }
}
