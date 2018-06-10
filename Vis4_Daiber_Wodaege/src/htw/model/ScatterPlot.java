package htw.model;

import javax.swing.*;
import javax.swing.Renderer;

import prefuse.Constants;
import prefuse.Display;
import prefuse.Visualization;
import prefuse.action.ActionList;
import prefuse.action.RepaintAction;
import prefuse.action.assignment.ColorAction;
import prefuse.action.assignment.DataShapeAction;
import prefuse.action.layout.AxisLabelLayout;
import prefuse.action.layout.AxisLayout;
import prefuse.controls.PanControl;
import prefuse.controls.ToolTipControl;
import prefuse.controls.ZoomControl;
import prefuse.controls.ZoomToFitControl;
import prefuse.data.Table;
import prefuse.data.io.DelimitedTextTableReader;
import prefuse.data.query.NumberRangeModel;
import prefuse.render.*;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import prefuse.visual.expression.VisiblePredicate;
import prefuse.visual.sort.ItemSorter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScatterPlot extends Display {

    private Visualization vis;
    private Display display;

    private AxisLayout x_axis;
    private AxisLayout y_axis;

    private AxisLabelLayout x_labels;
    private AxisLabelLayout y_labels;

    private Table table;

    public ScatterPlot() {

        vis = new Visualization();

        display = new Display(vis);

        //x_axis = new AxisLayout("data", "NBZ", Constants.X_AXIS, VisiblePredicate.TRUE);
        //y_axis = new AxisLayout("data", "test", Constants.Y_AXIS, VisiblePredicate.TRUE);


        // create actions to process the visual data
        x_axis = new AxisLayout("data", "PS", Constants.X_AXIS, VisiblePredicate.TRUE);
        y_axis = new AxisLayout("data", "Hubraum", Constants.Y_AXIS, VisiblePredicate.TRUE);

        x_labels = new AxisLabelLayout("xlab", x_axis);
        y_labels = new AxisLabelLayout("ylab", y_axis);

        initAxis();

        table = generateTable();
    }

    public void setXAxis(Feature feature)
    {
        int[] range = getFeatureRange(feature);

        // TODO set shown values ?

        x_axis = new AxisLayout("data", feature.getName(), Constants.X_AXIS, VisiblePredicate.TRUE);
        x_axis.setRangeModel(new NumberRangeModel(range[0], range[1], range[0], range[1]));
        vis.run("update");
    }

    public void setYAxis(Feature feature)
    {
        int[] range = getFeatureRange(feature);

        // TODO

        y_axis = new AxisLayout("data", feature.getName(), Constants.Y_AXIS, VisiblePredicate.TRUE);
        y_axis.setRangeModel(new NumberRangeModel(range[0], range[1], range[0], range[1]));

        vis.run("update");
    }

    public int[] getFeatureRange(Feature feature)
    {
        int[] range = {0, 0};

        switch(feature)
        {
            case ACCELERATION:
                range[1] = 25;
                break;
            case PS:
                range[1] = 250;
                break;
            case MPG:
                range[1] = 30;
                break;
            case WEIGHT:
                range[1] = 2500;
                break;
            case CYLINDERS:
                range[1] = 10;
                break;
            case DISPLACEMENT:
                range[1] = 7500;
                break;
        }

        return range;
    }

    public void setData(java.util.List<Car> cars)
    {
        table.addRows(cars.size());

        for(int i = 0; i < cars.size(); i++)
        {
            Car tmpCar = cars.get(i);

            table.set(i, 0, tmpCar.getNumber());
            table.set(i, 1, tmpCar.getCar());
            table.set(i, 2, tmpCar.getManufacturer());
            table.set(i, 3, tmpCar.getYear());
            table.set(i, 4, tmpCar.getOrigin());

            table.set(i, 5, tmpCar.getmPG());
            table.set(i, 6, tmpCar.getCylinders());
            table.set(i, 7, tmpCar.getDisplacement());
            table.set(i, 8, tmpCar.getPS());
            table.set(i, 9, tmpCar.getWeight());
            table.set(i, 10, tmpCar.getAcceleration());
        }

        //vis.run("draw");

        //vis.run("update");
    }

    public JComponent generateScatterplot()
    {
        initVisualization();
        initDisplay();

        final JComponent newDisplay = createVisualization(table);

        return newDisplay;
    }

    private JComponent createVisualization(Table data) {

        //vis.reset();
        // setup the visualized data
        VisualTable vt = vis.addTable("data", data);

        // add a new column containing a label string
        vt.addColumn("label", "CONCAT('PS: ', [PS], '; Hubraum: ', FORMAT([Hubraum],1))");

       // vt.addColumn("label", "CONCAT('NBZ: ', [NBZ], '; test: ', FORMAT([test],1))");


        // launching the visualization
        vis.run("draw");

        return display;
    }

    private void initVisualization()
    {
        ColorAction color = new ColorAction("data", VisualItem.STROKECOLOR,
                ColorLib.rgb(100, 100, 255));

        int[] palette = { Constants.SHAPE_STAR, Constants.SHAPE_ELLIPSE };
        DataShapeAction shape = new DataShapeAction("data", "Car", palette);

        ActionList draw = new ActionList();
        draw.add(x_axis);
        draw.add(y_axis);
        draw.add(x_labels);
        draw.add(y_labels);
        draw.add(color);
        draw.add(shape);
        draw.add(new RepaintAction());
        vis.putAction("draw", draw);

        ActionList update = new ActionList();
        update.add(x_axis);
        update.add(y_axis);
        update.add(x_labels);
        update.add(y_labels);
        update.add(new RepaintAction());
        vis.putAction("update", update);

        // set up renderers for the visual data
        vis.setRendererFactory(new RendererFactory() {
            AbstractShapeRenderer sr = new ShapeRenderer(7);
            prefuse.render.Renderer arY = new AxisRenderer(Constants.FAR_LEFT,
                    Constants.CENTER);
            prefuse.render.Renderer arX = new AxisRenderer(Constants.CENTER,
                    Constants.FAR_BOTTOM);

            public prefuse.render.Renderer getRenderer(VisualItem item) {
                return item.isInGroup("ylab") ? arY
                        : item.isInGroup("xlab") ? arX : sr;
            }
        });
    }


    private Table generateTable() {

        Table table = new Table();

        //GregorianCalendar cal = new GregorianCalendar();


        // set up table schema
        table.addColumn("Number", Integer.class);
        table.addColumn("Car", String.class);
        table.addColumn("Manufacturer", String.class);
        table.addColumn("Year", String.class);
        table.addColumn("Origin", String.class);

        table.addColumn("MPG", Double.class);
        table.addColumn("Zylinder", Integer.class);
        table.addColumn("Hubraum", Double.class);
        table.addColumn("PS", Integer.class);
        table.addColumn("Gewicht", Double.class);
        table.addColumn("Beschleunigung", Double.class);

        /*
        table.addRows(2);

        table.set(0, 0, 1);
        table.set(0, 1, "Chetta");
        table.set(0, 2, "Opel");
        table.set(0, 3, "1923");
        table.set(0, 4, "Germany");

        table.set(0, 5, 4.5);
        table.set(0, 6, 12);
        table.set(0, 7, 1324.3);
        table.set(0, 8, 123);
        table.set(0, 9, 1600.1);
        table.set(0, 10, 23.3);

        table.set(1, 0, 2);
        table.set(1, 1, "Amina");
        table.set(1, 2, "Yusu");
        table.set(1, 3, "1922");
        table.set(1, 4, "Germany");

        table.set(1, 5, 2.5);
        table.set(1, 6, 6);
        table.set(1, 7, 1000.3);
        table.set(1, 8, 143);
        table.set(1, 9, 800.1);
        table.set(1, 10, 43.3);

/*
        table.addColumn("Date", Date.class);
        table.addColumn("BMI", double.class);
        table.addColumn("NBZ", int.class);
        table.addColumn("Insult", String.class);
        table.addColumn("test", Double.class);


        table.addRows(3);

        cal.set(2007, 11, 23);
        table.set(0, 0, cal.getTime());
        table.set(0, 1, 21.0);
        table.set(0, 2, 236);
        table.set(0, 3, "F");
        table.set(0, 4, 1.2);

        cal.set(2008, 6, 22);
        table.set(1, 0, cal.getTime());
        table.set(1, 1, 35.8);
        table.set(1, 2, 400);
        table.set(1, 3, "F");
        table.set(1, 4, 10.2);

        cal.set(2009, 3, 8);
        table.set(2, 0, cal.getTime());
        table.set(2, 1, 28.8);
        table.set(2, 2, 309);
        table.set(2, 3, "T");
        table.set(2, 4, 100.2);

*/
        return table;
    }

    // called by generateScatterplot
    private void initDisplay()
    {
        // set up a display and controls
        display.setHighQuality(true);
        display.setSize(700, 450);

        display.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        // show data items in front of axis labels
        display.setItemSorter(new ItemSorter() {
            public int score(VisualItem item) {
                int score = super.score(item);
                if (item.isInGroup("data"))
                    score++;
                return score;
            }
        });

        // react on window resize
        display.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                vis.run("update");
            }
        });

        ToolTipControl ttc = new ToolTipControl("label");
        display.addControlListener(ttc);

        display.addControlListener(new PanControl());
        display.addControlListener(new ZoomControl());
        display.addControlListener(new ZoomToFitControl());
    }

    // called by contructor
    private void initAxis()
    {
        // use linear scale for y axis
        y_axis.setScale(Constants.LINEAR_SCALE);
        y_labels.setScale(Constants.LINEAR_SCALE);

        // use linear scale for x axis
        x_axis.setScale(Constants.LINEAR_SCALE);
        x_labels.setScale(Constants.LINEAR_SCALE);

        // use a special format for y axis labels
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);

        y_labels.setNumberFormat(nf);
        x_labels.setNumberFormat(nf);
    }
}
