package org.app.client.util.captura;

import org.app.client.dao.controller.RegistroComponenteController;
import org.app.client.dao.entity.Componente;
import org.app.client.herancas.Ping;

public class CapturaRede implements Runnable{

    private RegistroComponenteController registroComponenteController;
    private Componente componente;

    public CapturaRede(RegistroComponenteController registroComponenteController, Componente componente) {
        this.registroComponenteController = registroComponenteController;
        this.componente = componente;
    }

    public void medirRede(){
        Ping fetchRede = new Ping();
        registroComponenteController.adicionarRegistro("Ping", fetchRede.getPing(), componente.getIdComponente());
        registroComponenteController.adicionarRegistro("Download", fetchRede.getDownload(), componente.getIdComponente());
        registroComponenteController.adicionarRegistro("Upload", fetchRede.getUpload(), componente.getIdComponente());
    }


    @Override
    public void run() {
        while(true){
            medirRede();
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


}