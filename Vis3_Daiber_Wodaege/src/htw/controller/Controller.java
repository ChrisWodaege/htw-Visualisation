package htw.controller;

import htw.model.Car;
import htw.model.DataPoint;
import htw.model.Feature;
import htw.util.CoordianteSystem;
import htw.util.FileParser;

import htw.util.StyleChangingRowFactory;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.scene.shape.Polygon;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Controller {

	Car car = new Car();
	// RED, GREEN, BLUE, YELLOW, TURKIS, PINK, ORANGE
	// private final int[] colors = {0xffff0000, 0xff00ff00, 0xff0000ff, 0xffffff00,
	// 0xff00ffff, 0xffff00ff, 0xffffa500};
	private final Color[] colors = { Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.TURQUOISE, Color.PINK,
			Color.ORANGE };
	private int cellCounter = 0;

	private List<DataPoint> coordinatePoints;
	double[] currentCoorPoints;

	@FXML
	private Canvas canvas;

	private GraphicsContext gc;

	private int canvasHeight = 600;
	private int canvasWidth = 800;

	private ObservableList<Car> tableChoosenDataList = FXCollections.observableArrayList();

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

	@FXML
	private CheckBox changeBezier = new CheckBox();

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

	private CoordianteSystem coordianteSystem;
	private int MAXITEMS = 4;

	@FXML
	public void initialize() {
		canvas.setHeight(canvasHeight);
		canvas.setWidth(canvasWidth);

		gc = canvas.getGraphicsContext2D();
		fp = new FileParser();
		fp.parseFile();




		buttonUp.setDisable(true);

		coordianteSystem = new CoordianteSystem(gc, canvasWidth / 2, canvasHeight / 2, 290, Color.BLACK);
		coordianteSystem.addAxis(Feature.ACCELERATION, 25, 5);
		coordianteSystem.addAxis(Feature.CYLINDERS, 10, 5);
		coordianteSystem.addAxis(Feature.DISPLACEMENT, 7500, 10);
		coordianteSystem.addAxis(Feature.WEIGHT, 2500, 5);
		coordianteSystem.addAxis(Feature.PS, 250, 5);
		coordianteSystem.addAxis(Feature.MPG, 30, 4);

		coordianteSystem.draw();

		fillTable();



/*
				tableChoosenDataList.addListener(new ListChangeListener<Car>() {

					@Override
					public void onChanged(Change<? extends Car> change) {

						String styleclass = "rowStyle_";
						styleclass += car.getColor().toString();

						System.out.println("bla");


						if (tableChoosenDataList.contains(row.getIndex())) {

							if (! row.getStyleClass().contains(styleclass)) {
								row.getStyleClass().add(styleclass);
							}
						} else {
							row.getStyleClass().removeAll(Collections.singleton((Object)styleclass));
						}
					}
				});
				return row;
			}
		});
		*/


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

			//setCellTextColor(car);

			fp.tableDatasetList.remove(getRowId());
		}

		if (cellCounter >= MAXITEMS)
			buttonDown.setDisable(true);


		addCarToCoordSys(car);

		cellCounter++;

		if(cellCounter > 0)
			buttonUp.setDisable(false);
	}

	private void setCellTextColor(Car car) {

		tableChoosenData.setRowFactory(new Callback<TableView<Car>, TableRow<Car>>() {
			@Override
			public TableRow<Car> call(TableView<Car> tableView) {
				final TableRow<Car> row = new TableRow<Car>() {
					@Override
					protected void updateItem(Car person, boolean empty){
						super.updateItem(person, empty);

						System.out.println("update item");


						getStyleClass().add("highlightedRow");

						/*
						if (tableChoosenDataList.contains(getIndex())) {

							System.out.println("1 contains index");

							if (! getStyleClass().contains("highlightedRow")) {

								System.out.println("1 add styleclass");

								getStyleClass().add("highlightedRow");
							}
						} else {
							getStyleClass().removeAll(Collections.singleton("highlightedRow"));
						}
						*/
					}
				};
				tableChoosenDataList.addListener(new ListChangeListener<Car>() {
					@Override
					public void onChanged(Change<? extends Car> change) {


						System.out.println("onchanged");


						row.getStyleClass().add("highlightedRow");

						/*
						if (tableChoosenDataList.contains(row.getIndex())) {

							System.out.println("contains index");

							if (! row.getStyleClass().contains("highlightedRow")) {

								System.out.println("add styleclass");


								row.getStyleClass().add("highlightedRow");
							}
						} else {
							row.getStyleClass().removeAll(Collections.singleton("highlightedRow"));
						}
						*/
					}
				});
				return row;
			}
		});
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

			//coloringCells(cellCounter);
		}

		if (cellCounter < MAXITEMS)
			buttonDown.setDisable(false);

		removeCarFromCoordSys(car);

		cellCounter--;

		if(cellCounter == 0)
			buttonUp.setDisable(true);
	}

	private void addCarToCoordSys(Car car) {

		coordianteSystem.addDataPointToAxis(Feature.WEIGHT, car.getNumber(), car.getWeight(), car.getColor());
		coordianteSystem.addDataPointToAxis(Feature.ACCELERATION, car.getNumber(), car.getAcceleration(),
				car.getColor());
		coordianteSystem.addDataPointToAxis(Feature.DISPLACEMENT, car.getNumber(), car.getDisplacement(),
				car.getColor());
		coordianteSystem.addDataPointToAxis(Feature.PS, car.getNumber(), car.getPS(), car.getColor());
		coordianteSystem.addDataPointToAxis(Feature.CYLINDERS, car.getNumber(), car.getCylinders(),
				car.getColor());
		coordianteSystem.addDataPointToAxis(Feature.MPG, car.getNumber(), car.getmPG(), car.getColor());

		coordianteSystem.draw();

		// redraw curves / lines of the car polygon
		int counter = 0;
		for(int i = 0; i <= cellCounter; i++) {
		//for (Car car1 : tableChoosenDataList) {
			Car tmpCar = tableChoosenDataList.get(i);
			System.out.println(tmpCar.getCar() + " | " + tmpCar.getNumber());
			addLinesToCoorSys(tmpCar.getNumber(), tmpCar.getColor());

		}

	}

	private void removeCarFromCoordSys(Car car) {
		coordianteSystem.removeDataPointFromAxis(Feature.WEIGHT, car.getNumber());
		coordianteSystem.removeDataPointFromAxis(Feature.ACCELERATION, car.getNumber());
		coordianteSystem.removeDataPointFromAxis(Feature.DISPLACEMENT, car.getNumber());
		coordianteSystem.removeDataPointFromAxis(Feature.PS, car.getNumber());
		coordianteSystem.removeDataPointFromAxis(Feature.CYLINDERS, car.getNumber());
		coordianteSystem.removeDataPointFromAxis(Feature.MPG, car.getNumber());

		coordianteSystem.draw();

		// redraw curves / lines of the car polygon
		for (Car tmpCar : tableChoosenDataList) {
			System.out.println(tmpCar.getCar() + " | " + tmpCar.getNumber());
			addLinesToCoorSys(tmpCar.getNumber(), tmpCar.getColor());

		}

	}

	private void addLinesToCoorSys(int carNum, Color color) {
		int index = 0;
		currentCoorPoints = new double[12];
		coordinatePoints = coordianteSystem.getDataPointsForCarKey(carNum);

		for (DataPoint dp : coordinatePoints) {

			currentCoorPoints[index++] = dp.getX();
			currentCoorPoints[index++] = dp.getY();
		}


		double r = color.getRed();
		double g = color.getGreen();
		double b = color.getBlue();
		double a = 0.2;

		gc.setFill(color.color(r, g, b, a));
		gc.setStroke(color);
		gc.setLineWidth(2);

		if(!changeBezier.isSelected()) {
			gc.strokePolygon(
					new double[]{currentCoorPoints[0], currentCoorPoints[2], currentCoorPoints[4], currentCoorPoints[6],
							currentCoorPoints[8], currentCoorPoints[10]},
					new double[]{currentCoorPoints[1], currentCoorPoints[3], currentCoorPoints[5], currentCoorPoints[7],
							currentCoorPoints[9], currentCoorPoints[11]},
					6);
			gc.fillPolygon(
					new double[]{currentCoorPoints[0], currentCoorPoints[2], currentCoorPoints[4], currentCoorPoints[6],
							currentCoorPoints[8], currentCoorPoints[10]},
					new double[]{currentCoorPoints[1], currentCoorPoints[3], currentCoorPoints[5], currentCoorPoints[7],
							currentCoorPoints[9], currentCoorPoints[11]},
					6);
		}
		else
		{
		    double mP05x = coordinatePoints.get(0).getX();
			double mP05y = coordinatePoints.get(0).getY();

			DataPoint dp1 = coordinatePoints.get(0);
			DataPoint dp2 = dp1;

			// the get the complete path, the connection between last and first point must be drawn
			coordinatePoints.add(coordinatePoints.get(0));

            gc.beginPath();
            gc.moveTo(mP05x, mP05y);

			for(DataPoint dp : coordinatePoints)
			{
				dp1 = dp2;
				dp2 = dp;

				double mP1x = dp1.getX();
				double mP1y = dp1.getY();

				double mP2x = dp2.getX();
				double mP2y = dp2.getY();

				double[] firstMidPoints = defineMidPoints(mP05x, mP05y, mP1x, mP1y);
				double[] secondMidPoints = defineMidPoints(mP1x, mP1y, mP2x, mP2y);

                // Curve
                gc.bezierCurveTo(firstMidPoints[0], firstMidPoints[1], firstMidPoints[0], firstMidPoints[1], secondMidPoints[0], secondMidPoints[1]);
                gc.setLineWidth(4);
                gc.setStroke(color);
                gc.stroke();

                // set fill
                gc.setFill(color.color(r, g, b, a));
                gc.fill();
			}


            gc.closePath();
		}

		gc.setFill(Color.BLACK);



	}

    public double[] defineMidPoints(double mP1x, double mP1y, double mP2x, double mP2y) {

        double[] mP = new double[2];

        mP[0] = (mP1x + mP2x) / 2;
        mP[1] = (mP1y + mP2y) / 2;

        return mP;

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

	@FXML
	public void coloringCells(int cellCounter) {
		// for (int i = 0; i < cellCounter; i++) {
		// tableChoosenData.setRowFactory(i);
		// System.out.println(i);
		// TableRow<Car> row = new TableRow();
		// row.setStyle("-fx-background-color:lightcoral");
		// }
	}

	/*
	 * 
	 * hier ist eine liste mit autoobjekten die sich in der unteren Liste befinden,
	 * die �ber werte kannst du �ber die getter abrufen
	 * 
	 */

}
