package model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Car {

    private final String id;

    private final List<Reservation> carReservations = new ArrayList<>();

    public Car() {
        this.id = UUID.randomUUID().toString();
    }

    public List<Reservation> getCarReservations() {
        return carReservations;
    }


}
