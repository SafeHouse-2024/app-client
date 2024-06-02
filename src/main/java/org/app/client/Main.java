package org.app.client;

import java.util.List;
import org.app.client.dao.controller.*;
import org.app.client.dao.entity.Componente;
import org.app.client.dao.entity.Computador;
import org.app.client.dao.entity.NomeProcesso;
import org.app.client.login.Login;
import com.github.britooo.looca.api.core.Looca;
import org.app.client.util.ExecutarPrograma;
import org.app.client.util.captura.Inicializacao;
import org.app.client.util.notificacoes.NotificacaoSlack;
import org.app.client.conexao.Conexao;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        String so = System.getProperty("os.name");
        String user = System.getProperty("user.name");

        Integer fkSistemaOperacional = 0;
        if (so.toUpperCase().contains("win".toUpperCase())) {
            fkSistemaOperacional = 1;
        } else if (so.contains("nux")) {
            fkSistemaOperacional = 2;
        }

        NomeProcessoController nomeProcessoController = new NomeProcessoController();
        List<NomeProcesso> processos = nomeProcessoController.listarProcessos(fkSistemaOperacional);
        Login login = new Login();
        String[] result = login.autenticar(fkSistemaOperacional).split("#");
        String codigoAcesso = result[0];
        String sudo = result[1];
        Looca looca = new Looca();
        Computador computador = Inicializacao.adicionarEstruturaMaquina(looca, codigoAcesso, fkSistemaOperacional);
        RegistroComponenteController registroComponenteController = new RegistroComponenteController();
        ComponenteController componenteController = new ComponenteController();
        UsoSistemaController usoSistemaController = new UsoSistemaController();

        List<Componente> componentes = componenteController.listarComponentes(computador.getIdComputador());

        try {
            NotificacaoSlack.EnviarNotificacaoSlack("Inicializando captura de dados para o computador " + computador.getIdComputador());
        } catch (Exception e) {
            e.printStackTrace();
        }

        while (true) {
            System.out.println("Iniciando captura de dados");

            try {
                ExecutarPrograma.executarPrograma(so, user, computador, processos, sudo);
                Inicializacao.capturarRegistros(registroComponenteController, componentes, looca);
                Inicializacao.registrarUso(usoSistemaController, looca.getSistema(), fkSistemaOperacional, computador);

                verificarProcessadorAltoUso();
                verificarMemoriaAltaUso();
                verificarDiscoBaixaDisponibilidade();

                NotificacaoSlack.EnviarNotificacaoSlack("Captura de dados realizada com sucesso para o computador " + computador.getIdComputador());
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    NotificacaoSlack.EnviarNotificacaoSlack("Erro ao capturar dados para o computador " + computador.getIdComputador() + ": " + e.getMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            Thread.sleep(5000);
        }
    }

    private static void verificarProcessadorAltoUso() {
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        String query =
                "SELECT DISTINCT(pc.idComputador), count(rc.valor) " +
                        "FROM RegistroComponente rc " +
                        "JOIN Componente c ON c.idComponente = rc.fkComponente " +
                        "JOIN Computador pc ON c.fkComputador = pc.IdComputador " +
                        "JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore " +
                        "WHERE dataRegistro >= NOW() - INTERVAL 30000 MINUTE " +
                        "AND c.nome LIKE 'Processador' " +
                        "AND rc.valor > 80 " +
                        "AND pc.idComputador = 7 " +
                        "GROUP BY pc.idComputador";

        try {
            List<ComputadorAlerta> alertas = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ComputadorAlerta.class));

            for (ComputadorAlerta alerta : alertas) {
                if (alerta.getCount() > 0) {
                    String message = "Alerta: O computador com ID " + alerta.getIdComputador() + " tem " + alerta.getCount() + " registros de valor de processador acima de 80.";
                    NotificacaoSlack.EnviarNotificacaoSlack(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void verificarMemoriaAltaUso() {
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        String query =
                "SELECT DISTINCT(pc.idComputador), count(rc.valor) " +
                        "FROM RegistroComponente rc " +
                        "JOIN Componente c ON c.idComponente = rc.fkComponente " +
                        "JOIN Computador pc ON c.fkComputador = pc.IdComputador " +
                        "JOIN DarkStore d ON d.idDarkStore = pc.fkDarkStore " +
                        "WHERE dataRegistro >= NOW() - INTERVAL 30000 MINUTE " +
                        "AND c.nome LIKE 'Memória' " +
                        "AND rc.valor > SUBSTRING_INDEX((SELECT ca.valor " +
                        "FROM CaracteristicaComponente ca " +
                        "JOIN Componente c ON c.idComponente = ca.fkComponente " +
                        "JOIN Computador pc ON pc.idComputador = c.fkComputador " +
                        "WHERE pc.idComputador = 7 " +
                        "AND c.nome LIKE 'Memória' " +
                        "AND ca.nome LIKE 'Memória Total'), ' ', 1) * 0.8 " +
                        "GROUP BY pc.idComputador";

        try {
            List<ComputadorAlerta> alertas = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ComputadorAlerta.class));

            for (ComputadorAlerta alerta : alertas) {
                if (alerta.getCount() > 0) {
                    String message = "Alerta: O computador com ID " + alerta.getIdComputador() + " tem " + alerta.getCount() + " registros de valor de memória acima de 80%.";
                    NotificacaoSlack.EnviarNotificacaoSlack(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void verificarDiscoBaixaDisponibilidade() {
        Conexao conexao = new Conexao();
        JdbcTemplate jdbcTemplate = conexao.getJdbcTemplate();

        String query =
                "SELECT DISTINCT(pc.idComputador) " +
                        "FROM Componente c " +
                        "JOIN Computador pc ON c.fkComputador = pc.IdComputador " +
                        "JOIN CaracteristicaComponente ca ON ca.fkComponente = c.idComponente " +
                        "WHERE c.nome LIKE 'Disco' " +
                        "AND (SUBSTRING_INDEX((SELECT ca.valor " +
                        "FROM CaracteristicaComponente ca " +
                        "JOIN Componente c ON c.idComponente = ca.fkComponente " +
                        "JOIN Computador pc ON pc.idComputador = c.fkComputador " +
                        "WHERE pc.idComputador = 7 " +
                        "AND c.nome LIKE 'Disco' " +
                        "AND ca.nome LIKE 'Memória Disponível'), ' ', 1)) < " +
                        "(SUBSTRING_INDEX((SELECT ca.valor " +
                        "FROM CaracteristicaComponente ca " +
                        "JOIN Componente c ON c.idComponente = ca.fkComponente " +
                        "JOIN Computador pc ON pc.idComputador = c.fkComputador " +
                        "WHERE pc.idComputador = 7 " +
                        "AND c.nome LIKE 'Disco' " +
                        "AND ca.nome LIKE 'Memória Total'), ' ', 1) * 0.2)";

        try {
            List<ComputadorAlerta> alertas = jdbcTemplate.query(query, new BeanPropertyRowMapper<>(ComputadorAlerta.class));

            for (ComputadorAlerta alerta : alertas) {
                String message = "Alerta: O computador com ID " + alerta.getIdComputador() + " tem disponibilidade de disco abaixo de 20%";
                NotificacaoSlack.EnviarNotificacaoSlack(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ComputadorAlerta {
        private int idComputador;
        private int count;

        public int getIdComputador() {
            return idComputador;
        }

        public void setIdComputador(int idComputador) {
            this.idComputador = idComputador;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}
