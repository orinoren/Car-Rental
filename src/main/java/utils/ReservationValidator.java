package utils;

import exception.DataInvalidException;
import exception.DateRangeException;
import model.Reservation;

import java.time.LocalDate;

public enum ReservationValidator {
    INSTANCE;


    public void isReservationValid(Reservation reservation) throws DataInvalidException, DateRangeException {
        if (reservation == null) {
            throw new DataInvalidException("reservation must be declared");
        }
        isReservationDatesValid(reservation.getFromDate(), reservation.getToDate());
        isReservationPriceValid(reservation.getPrice());
    }

    public void isReservationDatesValid(LocalDate fromDate, LocalDate toDate) throws DateRangeException {
        if (fromDate == null || toDate == null) {
            throw new DataInvalidException((fromDate == null ? "from" : "to") + " date must be declared");
        }
        if (toDate.compareTo(fromDate) <= 0) {
            throw new DateRangeException("to date must be after from date");
        }
    }

    private void isReservationPriceValid(int price) throws DataInvalidException {
        if (price <= 0) {
            throw new DataInvalidException("price must be more then 0");
        }
    }
}
