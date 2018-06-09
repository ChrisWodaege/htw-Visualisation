package htw.model;

public enum Feature {

    // #Car,#Manufacturer,#MPG,#Cylinders,#Displacement,#Horsepower,#Weight ,#Acceleration, #Year, #Origin

    MPG("Liter / 100km"),
    CYLINDERS("Zylinder"),
    DISPLACEMENT("Hubraum in cm^3"),
    PS("PS"),
    WEIGHT("Gewicht in kg"),
    ACCELERATION("Beschl. in m/s^2");

    private String description;

    Feature(String description) {
        this.description = description;
    }

    public String toString()
    {
        return description;
    }
}
