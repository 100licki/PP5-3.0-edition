package pl.project.voucherstore.sales.ordering;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryReservationRepository implements ReservationRepository {

    private final ConcurrentHashMap<String, Reservation> reservations;

    public InMemoryReservationRepository() {
        this.reservations = new ConcurrentHashMap<String, Reservation>();
    }

    @Override
    public Reservation save(Reservation reservation) {
        reservations.put(reservation.getId(), reservation);
        return reservation;
    }

    @Override
    public Optional<Reservation> loadById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }
}
