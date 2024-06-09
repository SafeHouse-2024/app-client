package org.app.client.util.captura;

import java.time.LocalDateTime;

public class AtualizarAlerta {

    private LocalDateTime data;

    public AtualizarAlerta() {
    }

    public AtualizarAlerta(LocalDateTime data) {
        this.data = data;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "AtualizarAlerta{" +
                "data=" + data +
                '}';
    }
}
