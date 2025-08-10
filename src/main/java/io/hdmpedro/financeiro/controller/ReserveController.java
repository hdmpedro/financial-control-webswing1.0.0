package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.Reserve;
import io.hdmpedro.financeiro.service.ReserveService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReserveController {
    private final ReserveService reserveService;

    public ReserveController(ReserveService reserveService) {
        this.reserveService = reserveService;
    }

    public Reserve addToReserve(BigDecimal amount, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }

        return reserveService.addToReserve(amount, description.trim(), LocalDate.now());
    }

    public void removeFromReserve(Long id) {
        if (!reserveService.removeFromReserve(id)) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }
    }

    public BigDecimal getTotalReserve() {
        return reserveService.getTotalReserve();
    }

    public List<Reserve> getAllReserves() {
        return reserveService.getAllReserves();
    }

    public List<Reserve> getReservesByMonth(int month, int year) {
        return reserveService.getReservesByMonth(month, year);
    }
}
