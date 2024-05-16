package org.app.client.herancas.herancaController;

import org.app.client.conexao.Conexao;
import org.app.client.dao.controller.CaracteristicaComponenteController;

public class DiscoController extends CaracteristicaComponenteController {
    Conexao conexao = new Conexao();

    @Override
    public void atualizarValorCaracteristica(String valor, Integer idCaracteristica) {
        super.atualizarValorCaracteristica(valor, idCaracteristica);
    }
}
