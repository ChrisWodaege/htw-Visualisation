package htw.model;

public enum Feature {

    // #Car,#Manufacturer,#MPG,#Cylinders,#Displacement,#Horsepower,#Weight ,#Acceleration, #Year, #Origin

    MPG("Mpg", "Liter / 100km"),
    CYLINDERS("Zylinder", "Zylinder"),
    DISPLACEMENT("Hubraum", "Hubraum in cm^3"),
    PS("PS", "PS"),
    WEIGHT("Gewicht", "Gewicht in kg"),
    ACCELERATION("Beschleunigung", "Beschl. in m/s^2");

    private String name;
    private String description;

    Feature(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String toString()
    {
        return description;
    }

    public String getName()
    {
        return this.name;
    }
}
