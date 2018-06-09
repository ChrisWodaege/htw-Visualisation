package htw.controller;

import htw.model.Car;
import htw.model.DataPoint;
import htw.model.ScatterPlot;
import htw.util.FileParser;

import javafx.collections.ListChangeListener;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
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
import prefuse.data.query.NumberRangeModel;
import prefuse.render.AbstractShapeRenderer;
import prefuse.render.AxisRenderer;
import prefuse.render.Renderer;
import prefuse.render.RendererFactory;
import prefuse.render.ShapeRenderer;
import prefuse.util.ColorLib;
import prefuse.visual.VisualItem;
import prefuse.visual.VisualTable;
import prefuse.visual.expression.VisiblePredicate;
import prefuse.visual.sort.ItemSorter;

import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.NumberFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class Controller {

	// RED, GREEN, BLUE, YELLOW, TURKIS, PINK, ORANGE
	// private final int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00,
	// 0xff00ffff, 0xffff00ff, 0xffffa500};
	private final Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.TURQUOISE, Color.PINK,
			Color.ORANGE };
	private int cellCounter = 0;

	public enum Dataset {
		FIRST("Alter Datensatz"), SECOND("Neuer Datensatz");

		private final String name;

		private Dataset(String s) {
			name = s;
		}

		public String toString() {
			return this.name;
		}
	};
	
	private List<DataPoint> coordinatePoints;
	double[] currentCoorPoints;

	@FXML
	private BorderPane borderPane;

	@FXML
    private VBox box;


	private int canvasHeight = 600;
	private int canvasWidth = 800;

	private ObservableList<Car> tableChoosenDataList = FXCollections.observableArrayList();

	@FXML
	ComboBox<Dataset> dataSetChoose = new ComboBox();
	
	// upper table
	@FXML
	private TableView<Car> tableDataset = new TableView();
	@FXML
	private TableColumn<Car, Integer> columnDatasetNr;
	@FXML
	private TableColumn<Car, String> columnDatasetAuto;
	@FXML
	private TableColumn<Car, String> columnDatasetHersteller;
	@FXML
	private TableColumn<Car, String> columnDatasetJahr;
	@FXML
	private TableColumn<Car, String> columnDatasetLand;

	@FXML
	private Button buttonUp = new Button();

	@FXML
	private Button buttonDown = new Button();

	
	// lower table
	@FXML
	private TableView tableChoosenData = new TableView();
	@FXML
	private TableColumn<Car, Integer> columnChoosenDataNr;
	@FXML
	private TableColumn<Car, String> columnChoosenDataAuto;
	@FXML
	private TableColumn<Car, String> columnChoosenDataHersteller;
	@FXML
	private TableColumn<Car, String> columnChoosenDataJahr;
	@FXML
	private TableColumn<Car, String> columnChoosenDataLand;

	private FileParser fp;

	private int MAXITEMS = 4;



	private void initScatterplot()
    {
        ScatterPlot sp = new ScatterPlot();
        final JComponent display = sp.generateScatterplot();

        SwingNode node = new SwingNode();
        node.setContent(display);

        box.getChildren().add(node);
    }

	@FXML
	public void initialize() {
		dataSetChoose.setItems(FXCollections.observableArrayList(Dataset.values()));

		fp = new FileParser();

		buttonUp.setDisable(true);

		fillTable();

		initScatterplot();
		tableChoosenData.setRowFactory(tv -> {

            TableRow<Car> row = new TableRow<>();

            row.setOnMouseClicked(event -> {
                if (tableChoosenData.getSelectionModel().getSelectedItem() != null && event.getClickCount() == 2 ) {
                    Car tmpCar = tableChoosenDataList.get(getRowId());
                    showCarDetails(tmpCar);
                }
            });
            return row ;

		});

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Bedienungshinweis");
		alert.setHeaderText("Um die vollständigen Autodetails zu sehen, \nin der untern Liste das gewünschte Auto doppelklicken.");

		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				System.out.println("Pressed OK.");
			}
		});
	}

	@FXML
	public void chooseDataset() {
		
		switch (dataSetChoose.getValue()) {
		case FIRST:
			clearTables();
			fp.parseFile("cars");
			break;
		case SECOND:
			clearTables();
			fp.parseFile("cars2");	
			break;
		default:
			break;
		}
	}
	
	private void clearTables() {
		fp.tableListCounter = 1;
		fp.tableDatasetList.clear();
		tableChoosenDataList.clear();
	}
	
	public void fillTable() {

		// upper table
		columnDatasetNr.setCellValueFactory(new PropertyValueFactory<Car, Integer>("number"));
		columnDatasetAuto.setCellValueFactory(new PropertyValueFactory<Car, String>("car"));
		columnDatasetHersteller.setCellValueFactory(new PropertyValueFactory<Car, String>("manufacturer"));
		columnDatasetJahr.setCellValueFactory(new PropertyValueFactory<Car, String>("year"));
		columnDatasetLand.setCellValueFactory(new PropertyValueFactory<Car, String>("origin"));

		tableDataset.setItems(fp.tableDatasetList);

		// lower table
		columnChoosenDataNr.setCellValueFactory(new PropertyValueFactory<Car, Integer>("number"));
		columnChoosenDataAuto.setCellValueFactory(new PropertyValueFactory<Car, String>("car"));
		columnChoosenDataHersteller.setCellValueFactory(new PropertyValueFactory<Car, String>("manufacturer"));
		columnChoosenDataJahr.setCellValueFactory(new PropertyValueFactory<Car, String>("year"));
		columnChoosenDataLand.setCellValueFactory(new PropertyValueFactory<Car, String>("origin"));

		tableChoosenData.setItems(tableChoosenDataList);
	}
	

	@FXML
	public int getRowId() {
		int id = -1;
		if (tableDataset.getSelectionModel().getSelectedItem() != null) {
			id = tableDataset.getSelectionModel().getSelectedIndex();
		}
		if (tableChoosenData.getSelectionModel().getSelectedItem() != null) {
			id = tableChoosenData.getSelectionModel().getSelectedIndex();
		}
		return id;
	}

	@FXML
	public void buttonDownClick() {

		Car car = null;


		if (tableDataset.getSelectionModel().getSelectedItem() != null) {
			car = fp.tableDatasetList.get(getRowId());
			car.setColor(colors[cellCounter]);
			//fp.tableDatasetList.add(car);

			tableChoosenDataList.add(new Car(car.getNumber(), car.getCar(), car.getManufacturer(), car.getmPG(),
					car.getCylinders(), car.getDisplacement(), car.getPS(), car.getWeight(), car.getAcceleration(),
					car.getYear(), car.getOrigin(), colors[cellCounter]));


			fp.tableDatasetList.remove(getRowId());
		}

		if (cellCounter >= MAXITEMS)
			buttonDown.setDisable(true);

		cellCounter++;

		if(cellCounter > 0)
			buttonUp.setDisable(false);
	}

	@FXML
	public void buttonUpClick() {

		Car car = null;

		if (tableChoosenData.getSelectionModel().getSelectedItem() != null) {
			car = tableChoosenDataList.get(getRowId());
			fp.tableDatasetList.add(new Car(car.getNumber(), car.getCar(), car.getManufacturer(), car.getmPG(),
					car.getCylinders(), car.getDisplacement(), car.getPS(), car.getWeight(), car.getAcceleration(),
					car.getYear(), car.getOrigin(), colors[cellCounter]));
			tableChoosenDataList.remove(getRowId());


		}

		if (cellCounter < MAXITEMS)
			buttonDown.setDisable(false);

		cellCounter--;

		if(cellCounter == 0)
			buttonUp.setDisable(true);
	}


	private void showCarDetails(Car car){
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Auto Details");
		alert.setHeaderText("" + car.getYear() + " " + car.getManufacturer() + " " + car.getCar());

		alert.setContentText("Beschleunigung: "+String.format("%.2f", car.getAcceleration())+" m/s^2"
							+"\nLeistung: "+car.getPS()+" PS"
							+"\nVerbrauch: "+String.format("%.2f", car.getmPG())+" miles/gallon"
							+"\nGewicht: "+String.format("%.2f", car.getWeight())+" kg"
							+"\nHubraum: "+String.format("%.2f", car.getDisplacement())+" cm^3"
							+"\nZylinder: "+car.getCylinders());
		alert.showAndWait().ifPresent(rs -> {
			if (rs == ButtonType.OK) {
				System.out.println("Pressed OK.");
			}
		});
	}

}
