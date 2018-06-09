package htw.model;

import htw.util.CoordianteSystem;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Axis {

    private GraphicsContext gc = null;

    private DataPoint fromPoint;
    private DataPoint toPoint;

    private double axisAngle = 0;
    private int axisLength = 0;

    private double scaleMaxValue = 0;
    private int scaleGaps = 0;

    private Color axisColor = Color.BLACK;

    // defines the datapoint on the scale, the key is the listnumber of the car
    private Map<Integer, DataPoint> dataPoints = null;

    public Axis(GraphicsContext gc, DataPoint fromPoint, double scaleMaxValue, int scaleGaps, int axisLength, Color axisColor) {
        this.gc = gc;
        this.fromPoint = fromPoint;
        this.scaleMaxValue = scaleMaxValue;
        this.scaleGaps = scaleGaps;
        this.axisLength = axisLength;
        this.axisColor = axisColor;

        this.dataPoints = new HashMap<>();
    }

    public DataPoint getDataPointForKey(int carKey) { return this.dataPoints.get(carKey); }

    public void addDataPoint(int carKey, double featureValue, Color featureColor)
    {
        double factor = (double)axisLength / scaleMaxValue;
        double featureLength = factor * featureValue;

        //System.out.println("scaleMax: "+scaleMaxValue + "factor: "+factor + " axislength: "+axisLength) ;
        //System.out.println("featurelength: "+featureLength + " total length: "+axisLength);

        double xPos = Math.cos(axisAngle) * featureLength + fromPoint.getX();
        double yPos = Math.sin(axisAngle) * featureLength + fromPoint.getY();

        DataPoint featurePos = new DataPoint(xPos, yPos, featureColor);
        dataPoints.put(carKey, featurePos);
    }

    public void removeDataPoint(int carKey)
    {
        dataPoints.remove(carKey);
    }

    public void drawAxis()
    {
        gc.setLineWidth(2.0);
        gc.setStroke(axisColor);
        gc.strokeLine(fromPoint.getX(), fromPoint.getY(), toPoint.getX(), toPoint.getY());
        //gc.fillText("label", toPoint.getX()+10, toPoint.getY()+10);

        // draw scaling
        gc.setLineWidth(1.0);
        double singleGapDist = axisLength / scaleGaps;
        double singleGapValue = scaleMaxValue / scaleGaps;

        double tmpGapDist = singleGapDist;
        double tmpGapValue = singleGapValue;
        for(int i = 0; i < scaleGaps; ++i)
        {
            double xPos = Math.cos(axisAngle) * tmpGapDist + fromPoint.getX();
            double yPos = Math.sin(axisAngle) * tmpGapDist + fromPoint.getY();

            // draw scaling lines according to quadrants of coordinate system
            if(axisAngle < 1.6) {
                gc.strokeLine(xPos, yPos, xPos + 10, yPos + 10);
                gc.fillText(String.format("%.0f", tmpGapValue), xPos + 15, yPos + 15);
            }
            else if(axisAngle > 1.6 && axisAngle < 3.2) {
                gc.strokeLine(xPos, yPos, xPos, yPos + 10);
                gc.fillText(String.format("%.0f", tmpGapValue), xPos + 5, yPos + 15);
            }
            else if(axisAngle > 3.2 && axisAngle < 4.5) {
                gc.strokeLine(xPos, yPos, xPos - 10, yPos + 10);
                gc.fillText(String.format("%.0f", tmpGapValue), xPos - 25, yPos + 25);
            }
            else {
                gc.strokeLine(xPos, yPos, xPos + 10, yPos);
                gc.fillText(String.format("%.0f", tmpGapValue), xPos + 15, yPos + 5);
            }

            tmpGapDist += singleGapDist;
            tmpGapValue += singleGapValue;
        }

        // draw datapoints with corresponding color
        gc.setLineWidth(8.0);
        for(DataPoint dp : dataPoints.values())
        {
            gc.setStroke(dp.getColor());
            gc.strokeLine(dp.getX(), dp.getY(), dp.getX(), dp.getY());
        }
    }

    public DataPoint getFromPoint() {
        return fromPoint;
    }

    public void setFromPoint(DataPoint fromPoint) {
        this.fromPoint = fromPoint;
    }

    public DataPoint getToPoint() {
        return toPoint;
    }

    public void setToPoint(DataPoint toPoint) {
        this.toPoint = toPoint;
    }

    public double getAxisAngle() {
        return axisAngle;
    }

    public void setAxisAngle(double axisAngle) {
        this.axisAngle = axisAngle;
    }

    public String toString()
    {
        return "from " + fromPoint.toString() + " to " + toPoint.toString() + " and angle: " + axisAngle;
    }
}
