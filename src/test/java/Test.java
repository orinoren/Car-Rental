import exception.DataInvalidException;
import exception.DateRangeException;
import model.Garage;
import model.Reservation;
import service.RentCarService;
import service.RentCarServiceImpl;
import utils.ReservationValidator;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Test {

    private static final LocalDate firstDayOfMonth = createDate(1);
    private static final LocalDate lastDayOfMonth = createDate(31);

    static RentCarService initRentCarService() {
        return new RentCarServiceImpl(Garage.INSTANCE, ReservationValidator.INSTANCE);
    }


    /**
     * This is an example use of the rent car manager, and you should verify that your implementation
     * doesn't fail on any of the assertions, but we will run additional testing to verify the implementation
     * of the whole api so make sure you implemented correctly all the methods of the rent car interface.
     */
    public static void main(String[] args) {

        RentCarService rentCarService = initRentCarService();

        // In this setting there are 2 cars in the garage, but it may get a different number in other tests
        rentCarService.setNumberOfCars(2);

        LocalDate fromDay = createDate(2);
        LocalDate toDay = createDate(3);

        boolean reservationSuccess = rentCarService.makeReservation(new Reservation(fromDay, toDay, 5));
        // reservationSuccess should be true
        assertTrue(reservationSuccess);

        Reservation secondReservation = new Reservation(fromDay, toDay, 10);
        reservationSuccess = rentCarService.makeReservation(secondReservation);
        //  reservationSuccess should be true
        assertTrue(reservationSuccess);

        int totalReservationPrice = rentCarService.getPriceOfReservations(firstDayOfMonth, lastDayOfMonth);
        assertTrue(totalReservationPrice == 15);

        reservationSuccess = rentCarService.makeReservation(new Reservation(fromDay, toDay, 25));
        // reservationSuccess should be false since there is no more cars on those dates
        assertFalse(reservationSuccess);

        rentCarService.cancelReservation(secondReservation.getReservationId());

        reservationSuccess = rentCarService.makeReservation(new Reservation(fromDay, toDay, 25));
        // reservationSuccess should be true since we canceled one reservation and there should be a car on that date
        assertTrue(reservationSuccess);

        totalReservationPrice = rentCarService.getPriceOfReservations(firstDayOfMonth, lastDayOfMonth);
        assertTrue(totalReservationPrice == 30);

        fromDay = createDate(10);
        toDay = createDate(15);

        reservationSuccess = rentCarService.makeReservation(new Reservation(fromDay, toDay, 8));
        // reservationSuccess should be true
        assertTrue(reservationSuccess);

        totalReservationPrice = rentCarService.getPriceOfReservations(firstDayOfMonth, lastDayOfMonth);
        assertTrue(totalReservationPrice == 38);

        int numberAvailableCars = rentCarService.getNumberOfAvailableCars(createDate(14));
        assertTrue(numberAvailableCars == 1);

        try {
            rentCarService.setNumberOfCars(0);
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("number of cars must be above 0"));
        }

        try {
            rentCarService.setNumberOfCars(1);
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("number of cars must be above the current number of cars in the garage"));
        }

        try {
            rentCarService.makeReservation(null);
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("reservation must be declared"));
        }

        try {
            rentCarService.makeReservation(new Reservation(null, toDay, 10));
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("from date must be declared"));
        }

        try {
            rentCarService.makeReservation(new Reservation(fromDay, null, 10));
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("to date must be declared"));
        }

        try {
            rentCarService.makeReservation(new Reservation(toDay, fromDay, 10));
        } catch (RuntimeException e) {
            assertTrue(e instanceof DateRangeException && e.getMessage().equalsIgnoreCase("to date must be after from date"));
        }

        try {
            rentCarService.makeReservation(new Reservation(fromDay, toDay, -1));
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("price must be more then 0"));
        }

        try {
            rentCarService.makeReservation(new Reservation(fromDay, toDay, 0));
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("price must be more then 0"));
        }

        try {
            rentCarService.cancelReservation(null);
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("reservation id must be declared"));
        }

        int allReservationSizeBeforeCancel = rentCarService.getAllReservationsSortedByDate(LocalDate.MIN, LocalDate.MAX).size();
        rentCarService.cancelReservation("not exist id");
        int allReservationSizeAfterCancel = rentCarService.getAllReservationsSortedByDate(LocalDate.MIN, LocalDate.MAX).size();
        assertTrue(allReservationSizeBeforeCancel == allReservationSizeAfterCancel);

        List<Reservation> allReservationsSortedByDate = rentCarService.getAllReservationsSortedByDate(LocalDate.MIN, LocalDate.MAX);
        List<Reservation> reservations = new ArrayList<>(allReservationsSortedByDate);
        reservations.sort(Comparator.comparing(Reservation::getFromDate));
        assertTrue(allReservationsSortedByDate.equals(reservations));

        try {
            rentCarService.getReservation(null);
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equalsIgnoreCase("reservation id must be declared"));
        }

        Reservation notExist = rentCarService.getReservation("not exist");
        assertTrue(notExist == null);

        try {
            rentCarService.getNumberOfAvailableCars(null);
        } catch (RuntimeException e) {
            assertTrue(e instanceof DataInvalidException && e.getMessage().equals("date must be declared"));
        }
    }


    private static LocalDate createDate(int day) {
        return LocalDate.of(2021, Month.DECEMBER, day);
    }

    private static void assertFalse(boolean conditionToCheck) {
        assertTrue(!conditionToCheck);
    }

    private static void assertTrue(boolean conditionToCheck) {
        if (!conditionToCheck) {
            throw new RuntimeException("Assertion failed");
        }
    }
}
