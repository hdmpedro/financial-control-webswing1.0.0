package io.hdmpedro.financeiro;


import io.hdmpedro.financeiro.controller.MainController;
import io.hdmpedro.financeiro.util.ColorTheme;
import org.webswing.toolkit.api.WebswingUtil;

public class FinancialControlApplication {

   public static void main(String[] args) {
       System.setProperty("java.awt.headless", "false");

       if (WebswingUtil.isWebswing()) {
           System.out.println("Iniciando aplicação em ambiente WebSwing...");
       } else {
           System.out.println("Iniciando aplicação em ambiente desktop...");
       }

       try {
           javax.swing.UIManager.setLookAndFeel(
                   new com.formdev.flatlaf.FlatLightLaf());
       } catch (Exception e) {
           System.err.println("Erro ao definir Look and Feel: " + e.getMessage());
       }

       javax.swing.SwingUtilities.invokeLater(() -> {
           try {
               MainController mainController = new MainController();
               mainController.initializeApplication();

               System.out.println("Aplicação iniciada com sucesso!");

               if (WebswingUtil.isWebswing()) {
                   System.out.println("Acesse a aplicação em: http://localhost:8080/financial-control");
               }

           } catch (Exception e) {
               System.err.println("Erro ao iniciar aplicação: " + e.getMessage());
               e.printStackTrace();
            }
        });
    }
}