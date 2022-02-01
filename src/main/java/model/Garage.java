package model;

import java.util.ArrayList;
import java.util.List;

public enum Garage {
    INSTANCE;

    private final List<Car> carsInTheGarage = new ArrayList<>();

    public List<Car> getCarsInTheGarage() {
        return carsInTheGarage;
    }

}
