package htw.util;

import htw.model.Axis;
import htw.model.DataPoint;
import htw.model.Feature;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;

public class CoordianteSystem {

    private int coordSysRadius = 0;
    private DataPoint coordSysCenter = null;
    private Color axisColor = null;
    private GraphicsContext gc = null;

    private Map<Feature, Axis> allAxis = null;
    private int numAxis = 0;
    private double axisAngle = 0;

    public CoordianteSystem(GraphicsContext gc, int canvasCenterX, int canvasCenterY, int coordSysRadius, Color axisColor)
    {
        this.coordSysRadius = coordSysRadius;
        this.gc = gc;
        this.coordSysCenter = new DataPoint(canvasCenterX, canvasCenterY, axisColor);
        this.axisColor = axisColor;

        this.allAxis = new LinkedHashMap<>();
    }

    public void addAxis(Feature feature, int axisMaxValue, int axisScaleGaps)
    {
        ++numAxis;
        axisAngle = 2 * Math.PI / numAxis;

        Axis newAxis = new Axis(gc, coordSysCenter, axisMaxValue, axisScaleGaps, coordSysRadius, axisColor);
        allAxis.put(feature, newAxis);

        int index = 0;
        for(Axis axis : allAxis.values())
        {
            axis.setToPoint(calcAxisToPoint(index));
            axis.setAxisAngle(index * axisAngle);
            ++index;
        }
    }

    public void addDataPointToAxis(Feature featureAxis, int carKey, double featureValue, Color featureColor)
    {
        allAxis.get(featureAxis).addDataPoint(carKey, featureValue, featureColor);
    }

    public void removeDataPointFromAxis(Feature featureAxis, int carKey)
    {
        allAxis.get(featureAxis).removeDataPoint(carKey);
    }

    public DataPoint calcAxisToPoint(int numAxis)
    {
        double tmpAngle = numAxis * axisAngle;

        double xPos = Math.cos(tmpAngle) * coordSysRadius + coordSysCenter.getX();
        double yPos = Math.sin(tmpAngle) * coordSysRadius + coordSysCenter.getY();

        return new DataPoint(xPos, yPos);
    }

    public void draw()
    {
        gc.clearRect(0,0,coordSysCenter.getX()*2, coordSysCenter.getY()*2);
        for(Feature feature : allAxis.keySet())
        {
            allAxis.get(feature).drawAxis();

            gc.fillText(feature.toString(), allAxis.get(feature).getToPoint().getX() + 25, allAxis.get(feature).getToPoint().getY() + 30);

            /* draw scaling lines according to quadrants of coordinate system
            if(axisAngle < 1.6)
            else if(axisAngle > 1.6 && axisAngle < 3.2)
                gc.fillText(feature.toString(), allAxis.get(feature).getToPoint().getX() + 500, allAxis.get(feature).getToPoint().getY() + 150);
            else if(axisAngle > 3.2 && axisAngle < 4.5)
                gc.fillText(feature.toString(), allAxis.get(feature).getToPoint().getX() - 250, allAxis.get(feature).getToPoint().getY() + 25);
            else
                gc.fillText(feature.toString(), allAxis.get(feature).getToPoint().getX() + 150, allAxis.get(feature).getToPoint().getY() + 5);

            //gc.fillText(feature.toString(), allAxis.get(feature).getToPoint().getX()+10, allAxis.get(feature).getToPoint().getY()+10);
            */
        }
    }

    public ArrayList<DataPoint> getDataPointsForCarKey(int carKey)
    {
        ArrayList<DataPoint> dataPoints = new ArrayList<>();

        for(Axis axis : allAxis.values()) { dataPoints.add(axis.getDataPointForKey(carKey)); }

        return dataPoints;
    }
}
