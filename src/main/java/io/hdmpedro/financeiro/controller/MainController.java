package io.hdmpedro.financeiro.controller;
import io.hdmpedro.financeiro.service.*;
import io.hdmpedro.financeiro.view.MainFrame;

import javax.swing.SwingUtilities;

public class MainController {
    private final TransacaoService transacaoService;
    private final CategoriaService categoriaService;
    private final ReservaService reservaService;
    private final FechamentoMesService fechamentoMesService;
    private final CalendarioService calendarioService;

    private final TransacaoController transacaoController;
    private final CategoriaController categoriaController;
    private final ReservaController reservaController;
    private final FechamentoMesController fechamentoMesController;

    private MainFrame mainFrame;

    public MainController() {
        this.transacaoService = new TransacaoService();
        this.categoriaService = new CategoriaService();
        this.reservaService = new ReservaService();
        this.fechamentoMesService = new FechamentoMesService(transacaoService, reservaService);
        this.calendarioService = new CalendarioService(transacaoService);

        this.transacaoController = new TransacaoController(transacaoService, categoriaService);
        this.categoriaController = new CategoriaController(categoriaService);
        this.reservaController = new ReservaController(reservaService);
        this.fechamentoMesController = new FechamentoMesController(
                fechamentoMesService, reservaService, transacaoService);
    }

    public void initializeApplication() {
        SwingUtilities.invokeLater(() -> {
            try {
                javax.swing.UIManager.setLookAndFeel(
                        new com.formdev.flatlaf.FlatLightLaf());
            } catch (Exception e) {
                e.printStackTrace();
            }

            mainFrame = new MainFrame(this);
            mainFrame.setVisible(true);
        });
    }

    public TransacaoController getTransactionController() {
        return transacaoController;
    }

    public CategoriaController getCategoryController() {
        return categoriaController;
    }

    public ReservaController getReserveController() {
        return reservaController;
    }

    public FechamentoMesController getMonthlyClosureController() {
        return fechamentoMesController;
    }

    public TransacaoService getTransactionService() {
        return transacaoService;
    }

    public CategoriaService getCategoryService() {
        return categoriaService;
    }

    public ReservaService getReserveService() {
        return reservaService;
    }

    public CalendarioService getCalendarService() {
        return calendarioService;
    }

    public FechamentoMesService getMonthlyClosureService() {
        return fechamentoMesService;
    }

    public void refreshAllViews() {
        if (mainFrame != null) {
            mainFrame.refreshAllPanels();
        }
    }
}