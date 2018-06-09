package htw.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import htw.model.Car;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class FileParser {

	private final double MILE = 0.621371;
	private final double GALLON = 0.264172;
	private final double CUBICINCH = 0.0610237;
	private final double POUND = 0.453592;
	public Car car;
	public ObservableList<Car> tableDatasetList = FXCollections.observableArrayList();

	public int tableListCounter = 1;

	public void parseFile() {
		String csvFile = "src/htw/resources/cars.csv";
		BufferedReader br = null;
		String line = "";

		try {

			br = new BufferedReader(new FileReader(csvFile));
			while ((line = br.readLine()) != null) {

				String[] data = line.split(",");

				if (data[0].startsWith("#") || data[2].equals("NA") || data[5].equals("NA")) {
					continue;
				} else {
					
					tableDatasetList.add(new Car(tableListCounter++, data[0], data[1], mpgToLiterPerHundredKm(Double.parseDouble(data[2])),
							Integer.parseInt(data[3]), cubicinchToCcm(Double.parseDouble(data[4])), Integer.parseInt(data[5]),
							poundToKiloGram(Double.parseDouble(data[6])), Double.parseDouble(data[7]), "19" + data[8], data[9], Color.BLACK));
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	// unitconversion

		public double mpgToLiterPerHundredKm(double mpg) {
			mpg = 235.21 / mpg;
			return mpg;
		}

		public double cubicinchToCcm(double displacement) {
			displacement = displacement / CUBICINCH;
			return displacement;
		}

		public double poundToKiloGram(double weight) {
			weight = weight * POUND;
			return weight;
		}

}
