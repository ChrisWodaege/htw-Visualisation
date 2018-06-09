package htw.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Circle {

    private double radius;
    private Color color;

    public Circle() {
        this.radius = 1;
        this.color = Color.RED;
    }

    public Circle(double radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void drawCircle(GraphicsContext gc, double xPos, double yPos)
    {
        gc.setFill(color);
        gc.fillOval(xPos, yPos, radius, radius);
       

        /*
        from javafx documentation - fillOval

        x - the X coordinate of the upper left bound of the oval.
        y - the Y coordinate of the upper left bound of the oval.

        --> xPos or yPos is the same as radius
         */
    }
}
