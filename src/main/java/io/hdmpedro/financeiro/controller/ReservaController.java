package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.Reserva;
import io.hdmpedro.financeiro.service.ReservaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    public Reserva addToReserve(BigDecimal amount, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }

        return reservaService.addToReserve(amount, description.trim(), LocalDate.now());
    }

    public void removeFromReserve(Long id) {
        if (!reservaService.removeFromReserve(id)) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }
    }

    public BigDecimal getTotalReserve() {
        return reservaService.getTotalReserve();
    }

    public List<Reserva> getAllReserves() {
        return reservaService.getAllReserves();
    }

    public List<Reserva> getReservesByMonth(int month, int year) {
        return reservaService.getReservesByMonth(month, year);
    }
}
