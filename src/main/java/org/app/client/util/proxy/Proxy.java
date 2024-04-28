package org.app.client.util.proxy;

import com.profesorfalken.jpowershell.PowerShell;
import java.io.IOException;

public class Proxy {
  private static String endereco = "18.234.191.199";
  private static String porta = "3128";

  public static void conectarProxy(String resposta, String so){
    if (resposta.equalsIgnoreCase("S")) {
        comandos(so);
    }
}

  private static void comandos(String so) {
    if (so.toUpperCase().contains("win".toUpperCase())) {
      PowerShell.executeSingleCommand("netsh winhttp set proxy %s:%s".formatted(endereco, porta));
    } else if (so.contains("nux")) {
      try {
        Runtime.getRuntime().exec("export http_proxy=http://%s:%s".formatted(endereco, porta));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static void desconectarProxy(String so) {
    if (so.toUpperCase().contains("win".toUpperCase())) {
      PowerShell.executeSingleCommand("netsh winhttp reset proxy");
    } else if (so.contains("nux")) {
      try {
        Runtime.getRuntime().exec("unset http_proxy");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public static String getEndereco() {
    return endereco;
  }

  public static String getPorta() {
    return porta;
  }
}
