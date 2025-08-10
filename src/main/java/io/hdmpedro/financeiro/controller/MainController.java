package io.hdmpedro.financeiro.controller;
import io.hdmpedro.financeiro.service.*;
import io.hdmpedro.financeiro.view.MainFrame;

import javax.swing.SwingUtilities;

public class MainController {
    private final TransactionService transactionService;
    private final CategoryService categoryService;
    private final ReserveService reserveService;
    private final MonthlyClosureService monthlyClosureService;
    private final CalendarService calendarService;

    private final TransactionController transactionController;
    private final CategoryController categoryController;
    private final ReserveController reserveController;
    private final MonthlyClosureController monthlyClosureController;

    private MainFrame mainFrame;

    public MainController() {
        this.transactionService = new TransactionService();
        this.categoryService = new CategoryService();
        this.reserveService = new ReserveService();
        this.monthlyClosureService = new MonthlyClosureService(transactionService, reserveService);
        this.calendarService = new CalendarService(transactionService);

        this.transactionController = new TransactionController(transactionService, categoryService);
        this.categoryController = new CategoryController(categoryService);
        this.reserveController = new ReserveController(reserveService);
        this.monthlyClosureController = new MonthlyClosureController(
                monthlyClosureService, reserveService, transactionService);
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

    public TransactionController getTransactionController() {
        return transactionController;
    }

    public CategoryController getCategoryController() {
        return categoryController;
    }

    public ReserveController getReserveController() {
        return reserveController;
    }

    public MonthlyClosureController getMonthlyClosureController() {
        return monthlyClosureController;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public ReserveService getReserveService() {
        return reserveService;
    }

    public CalendarService getCalendarService() {
        return calendarService;
    }

    public MonthlyClosureService getMonthlyClosureService() {
        return monthlyClosureService;
    }

    public void refreshAllViews() {
        if (mainFrame != null) {
            mainFrame.refreshAllPanels();
        }
    }
}