package io.hdmpedro.financeiro.controller;


import io.hdmpedro.financeiro.models.Reserva;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class ReservaController {
    private final io.hdmpedro.financeiro.service.ReservaController reservaController;

    public ReservaController(io.hdmpedro.financeiro.service.ReservaController reservaController) {
        this.reservaController = reservaController;
    }

    public Reserva addToReserve(BigDecimal amount, String description) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor deve ser maior que zero");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Descrição é obrigatória");
        }

        return reservaController.addToReserve(amount, description.trim(), LocalDate.now());
    }

    public void removeFromReserve(Long id) {
        if (!reservaController.removeFromReserve(id)) {
            throw new IllegalArgumentException("Reserva não encontrada");
        }
    }

    public BigDecimal getTotalReserve() {
        return reservaController.getTotalReserve();
    }

    public List<Reserva> getAllReserves() {
        return reservaController.getAllReserves();
    }

    public List<Reserva> getReservesByMonth(int month, int year) {
        return reservaController.getReservesByMonth(month, year);
    }
}
