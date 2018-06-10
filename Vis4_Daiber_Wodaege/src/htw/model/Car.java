package htw.model;

import javafx.scene.paint.Color;

public class Car {
	private int number;
	private String car;
	private String manufacturer;
	private double mpg;
	private int cylinders;
	private double displacement;
	private int horsepower;
	private double weight;
	private double acceleration;
	private String year;
	private String origin;

	private Color color;

	public Car(int number, String car, String manufacturer, double mPG, int cylinders, double displacement,
			int horsepower, double weight, double acceleration, String year, String origin, Color color) {

		this.number = number;
		this.car = car;
		this.manufacturer = manufacturer;
		
		this.mpg = mPG;
		
		this.cylinders = cylinders;
		this.displacement = displacement;
		this.horsepower = horsepower;
		this.weight = weight;
		this.acceleration = acceleration;
		this.year = year;
		this.origin = origin;

		this.color = color;
	}
	
	public Car() {
		this.number = 0;
		this.car = "";
		this.manufacturer = "";

		this.mpg = 0;

		this.cylinders = 0;
		this.displacement = 0;
		this.horsepower = 0;
		this.weight = 0;
		this.acceleration = 0;
		this.year = "";
		this.origin = "";

		this.color = null;
	}
	
	// getter / setter
	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public int getCylinders() {
		return cylinders;
	}

	public void setCylinders(int cylinders) {
		this.cylinders = cylinders;
	}

	public double getDisplacement() {
		return displacement;
	}

	public void setDisplacement(int displacement) {
		this.displacement = displacement;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public double getmPG() {
		return mpg;
	}

	public void setmPG(double consumption) {
		this.mpg = mpg;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public int getPS() {
		return horsepower;
	}

	public void setPS(int horsepower) {
		this.horsepower = horsepower;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}
