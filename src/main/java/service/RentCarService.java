package service;
import model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface RentCarService {

    /**
     * Sets the number of cars in the garage.
     */
    void setNumberOfCars(int numOfCars);

    /**
     * Try to add a reservation to the system.
     * Reservation will be added successfully only if during the whole time frame from its fromDate to its toDate there is
     * a free car in the garage.
     * @param reservation reservation to add
     * @return true if added reservation successfully. False otherwise.
     */
    boolean makeReservation(Reservation reservation);

    /**
     * Cancels the reservation with the given id.
     * @param reservationId id of reservation to cancel
     */
    void cancelReservation(String reservationId);

    /**
     * Get the reservation with the given id.
     * @param reservationId id of reservation to fetch.
     * @return Reservation with the given id or null if no reservation with that id exists.
     */
    Reservation getReservation(String reservationId);

    /**
     * Return the number of available cars on the given date.
     * @param dateToCheck date to check number of available cars
     * @return number of available cars on the given date.
     */
    int getNumberOfAvailableCars(LocalDate dateToCheck);

    /**
     * Get the price of all reservations that start on or after the given from date AND end on or before the given to date
     * (if a reservation starts before the given from date or ends after the given to date don't count it)
     * @return the sum of prices of all reservations that start and end during the given timeframe
     */
    int getPriceOfReservations(LocalDate from, LocalDate to);

    /**
     * Gets all the reservations that start on or after the given from date AND end on or before the given to date
     * sorted by date in an ASCENDING order.
     */
    List<Reservation> getAllReservationsSortedByDate(LocalDate from, LocalDate to);

}
