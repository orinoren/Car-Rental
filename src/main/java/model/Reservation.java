package model;

import java.io.InputStream;
import java.lang.module.FindException;
import java.net.http.HttpClient;
import java.security.AlgorithmParameterGenerator;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.UUID;


public class Reservation {

    private final String reservationId;
    private final LocalDate fromDate;
    private final LocalDate toDate;
    private final int price;

    public Reservation(LocalDate fromDate, LocalDate toDate, int price) {
        this.reservationId = UUID.randomUUID().toString();
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.price = price;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public int getPrice() {
        return price;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void printReservationDetails() {
        System.out.println("Reservation ID: " + reservationId + ". Reservation period: " + fromDate + " - " + toDate + ". Reservation price: " + price);
    }
}
