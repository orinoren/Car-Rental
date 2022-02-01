package service;

import exception.DataInvalidException;
import exception.DateRangeException;
import model.Car;
import model.Garage;
import model.Reservation;
import utils.ReservationValidator;

import java.time.LocalDate;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class RentCarServiceImpl implements RentCarService {

    private final Garage garage;
    private final ReservationValidator reservationValidator;

    public RentCarServiceImpl(Garage garage, ReservationValidator reservationValidator) {
        this.garage = garage;
        this.reservationValidator = reservationValidator;
    }

    @Override
    public void setNumberOfCars(int numOfCars) throws DataInvalidException {
        if (numOfCars <= 0) {
            throw new DataInvalidException("number of cars must be above 0");
        }
        List<Car> carsInTheGarage = getCarsInTheGarage();
        if (numOfCars < carsInTheGarage.size()) {
            throw new DataInvalidException("number of cars must be above the current number of cars in the garage");
        }
        for (int i = carsInTheGarage.size(); i < numOfCars; i++) {
            carsInTheGarage.add(new Car());
        }
    }


    @Override
    public boolean makeReservation(Reservation newReservation) throws DataInvalidException, DateRangeException {
        reservationValidator.isReservationValid(newReservation);

        List<Car> carsInTheGarage = getCarsInTheGarage();

        for (Car car : carsInTheGarage) {
            List<Reservation> carReservations = car.getCarReservations();
            if (carReservations.isEmpty()) {
                car.getCarReservations().add(newReservation);
                return true;
            }
            boolean reservationApproved = isReservationApproved(carReservations, newReservation);
            if (reservationApproved) {
                car.getCarReservations().add(newReservation);
                return true;
            }
        }
        return false;
    }


    @Override
    public void cancelReservation(String reservationId) {
        Reservation reservation = getReservation(reservationId);
        if (reservation != null) {
            List<Car> carsInTheGarage = getCarsInTheGarage();
            for (Car car : carsInTheGarage) {
                Iterator<Reservation> iterator = car.getCarReservations().iterator();
                while (iterator.hasNext()) {
                    Reservation reserve = iterator.next();
                    if (reserve.getReservationId().equals(reservationId)) {
                        iterator.remove();
                        return;
                    }
                }
            }
        }
    }

    @Override
    public Reservation getReservation(String reservationId) throws DataInvalidException {
        if (reservationId == null) {
            throw new DataInvalidException("reservation id must be declared");
        }
        List<Car> carsInTheGarage = getCarsInTheGarage();
        for (Car car : carsInTheGarage) {
            List<Reservation> carReservations = car.getCarReservations();
            for (Reservation reservation : carReservations) {
                if (reservation.getReservationId().equals(reservationId)) {
                    return reservation;
                }
            }
        }
        return null;
    }

    @Override
    public int getNumberOfAvailableCars(LocalDate dateToCheck) throws DataInvalidException {
        if (dateToCheck == null) {
            throw new DataInvalidException("date must be declared");
        }
        List<Car> carsInTheGarage = getCarsInTheGarage();
        int availableCars = carsInTheGarage.size();
        for (Car car : carsInTheGarage) {
            boolean carAvailable = true;
            for (int i = 0; carAvailable && i < car.getCarReservations().size(); i++) {
                Reservation carReservation = car.getCarReservations().get(i);
                LocalDate carReservationFromDate = carReservation.getFromDate();
                LocalDate carReservationToDate = carReservation.getToDate();
                if (isDateIsBetweenOrEqual(dateToCheck, carReservationFromDate, carReservationToDate)) {
                    availableCars--;
                    carAvailable = false;
                }
            }
        }
        return availableCars;
    }

    @Override
    public int getPriceOfReservations(LocalDate from, LocalDate to) throws DateRangeException, DataInvalidException {
        return getAllReservationsSortedByDate(from, to)
                .stream()
                .reduce(0, (totalPrice, r) -> totalPrice + r.getPrice(), Integer::sum);

    }

    @Override
    public List<Reservation> getAllReservationsSortedByDate(LocalDate from, LocalDate to) throws DateRangeException, DataInvalidException {
        reservationValidator.isReservationDatesValid(from, to);
        List<Reservation> allReservations = new ArrayList<>();
        List<Car> carsInTheGarage = getCarsInTheGarage();
        for (Car car : carsInTheGarage) {
            allReservations.addAll(car.getCarReservations());

        }
        return allReservations.stream()
                .filter((reservation) -> reservation.getFromDate().compareTo(from) >= 0 && reservation.getToDate().compareTo(to) <= 0)
                .sorted(Comparator.comparing(Reservation::getFromDate))
                .collect(Collectors.toList());
    }

    private boolean isReservationApproved(List<Reservation> carReservations, Reservation newReservation) {
        boolean reservationApproved = true;
        for (int i = 0; reservationApproved && i < carReservations.size(); i++) {
            Reservation existCarReservation = carReservations.get(i);
            LocalDate newReservationFromDate = newReservation.getFromDate();
            LocalDate newReservationToDate = newReservation.getToDate();
            if (isDatesAreBetweenOrEqual(newReservationFromDate, newReservationToDate, existCarReservation.getFromDate(), existCarReservation.getToDate())) {
                reservationApproved = false;
            }
        }
        return reservationApproved;
    }


    private boolean isDatesAreBetweenOrEqual(LocalDate fromDateToCheck, LocalDate toDateToCheck, LocalDate fromDate, LocalDate toDate) {
        return isDateIsBetweenOrEqual(fromDateToCheck, fromDate, toDate) ||
                isDateIsBetweenOrEqual(toDateToCheck, fromDate, toDate);
    }

    private boolean isDateIsBetweenOrEqual(LocalDate dateToCheck, LocalDate fromDate, LocalDate toDate) {
        return dateToCheck.compareTo(fromDate) >= 0 && dateToCheck.compareTo(toDate) <= 0;
    }

    private List<Car> getCarsInTheGarage() {
        return garage.getCarsInTheGarage();
    }
}
