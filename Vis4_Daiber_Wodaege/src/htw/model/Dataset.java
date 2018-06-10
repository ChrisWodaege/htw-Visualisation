package htw.model;

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