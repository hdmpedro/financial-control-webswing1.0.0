package io.hdmpedro.financeiro.service;

import io.hdmpedro.financeiro.models.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ReservaController {
    private final List<Reserva> reservas = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Reserva addToReserve(BigDecimal amount, String description, LocalDate date) {
        Reserva reserva = new Reserva(amount, description, date);
        reserva.setId(idGenerator.getAndIncrement());
        reservas.add(reserva);
        return reserva;
    }

    public Reserva addMonthlyBalanceToReserve(BigDecimal amount, int month, int year) {
        String description = String.format("Saldo do mês %02d/%d", month, year);
        Reserva reserva = addToReserve(amount, description, LocalDate.now());
        reserva.setFromMonthlyBalance(true);
        return reserva;
    }

    public boolean removeFromReserve(Long id) {
        return reservas.removeIf(r -> r.getId().equals(id));
    }

    public BigDecimal getTotalReserve() {
        return reservas.stream()
                .map(Reserva::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Reserva> getAllReserves() {
        return new ArrayList<>(reservas);
    }

    public List<Reserva> getReservesByMonth(int month, int year) {
        return reservas.stream()
                .filter(r -> r.getDate().getMonth().getValue() == month &&
                        r.getDate().getYear() == year)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<Reserva> getMonthlyBalanceReserves() {
        return reservas.stream()
                .filter(Reserva::isFromMonthlyBalance)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public BigDecimal getReservesByYear(int year) {
        return reservas.stream()
                .filter(r -> r.getDate().getYear() == year)
                .map(Reserva::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
