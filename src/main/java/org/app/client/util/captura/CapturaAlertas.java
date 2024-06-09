package org.app.client.util.captura;

import org.app.client.dao.controller.AlertaController;
import org.app.client.dao.entity.Computador;
import org.checkerframework.checker.units.qual.A;

public class CapturaAlertas {

    private static final AlertaController alertaController = new AlertaController();

    public static void iniciarCapturaAlertas(Computador computador){
        alertaController.getAllAlertasCPU(computador);
        alertaController.getAllAlertasRAM(computador);
        alertaController.getAllAlertasDisco(computador);
        alertaController.getAllAlertasRede(computador);
    }
}
