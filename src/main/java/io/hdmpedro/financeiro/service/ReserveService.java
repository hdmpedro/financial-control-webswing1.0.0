package io.hdmpedro.financeiro.service;

import io.hdmpedro.financeiro.models.Reserve;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class ReserveService {
    private final List<Reserve> reserves = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Reserve addToReserve(BigDecimal amount, String description, LocalDate date) {
        Reserve reserve = new Reserve(amount, description, date);
        reserve.setId(idGenerator.getAndIncrement());
        reserves.add(reserve);
        return reserve;
    }

    public Reserve addMonthlyBalanceToReserve(BigDecimal amount, int month, int year) {
        String description = String.format("Saldo do mês %02d/%d", month, year);
        Reserve reserve = addToReserve(amount, description, LocalDate.now());
        reserve.setFromMonthlyBalance(true);
        return reserve;
    }

    public boolean removeFromReserve(Long id) {
        return reserves.removeIf(r -> r.getId().equals(id));
    }

    public BigDecimal getTotalReserve() {
        return reserves.stream()
                .map(Reserve::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Reserve> getAllReserves() {
        return new ArrayList<>(reserves);
    }

    public List<Reserve> getReservesByMonth(int month, int year) {
        return reserves.stream()
                .filter(r -> r.getDate().getMonth().getValue() == month &&
                        r.getDate().getYear() == year)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public List<Reserve> getMonthlyBalanceReserves() {
        return reserves.stream()
                .filter(Reserve::isFromMonthlyBalance)
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    public BigDecimal getReservesByYear(int year) {
        return reserves.stream()
                .filter(r -> r.getDate().getYear() == year)
                .map(Reserve::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
