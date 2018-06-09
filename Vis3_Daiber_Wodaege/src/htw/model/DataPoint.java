package htw.model;

import javafx.scene.paint.Color;

public class DataPoint {

    private double x = 0;
    private double y = 0;

    private Color color = Color.BLACK;


    public DataPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public DataPoint(double x, double y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String toString()
    {
        return "x: " + x + " y: " + y;
    }
}
