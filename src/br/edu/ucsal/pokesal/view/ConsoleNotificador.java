package br.edu.ucsal.pokesal.view;

import br.edu.ucsal.pokesal.model.enums.Conquista;
import br.edu.ucsal.pokesal.service.NotificadorConquista;

/** Exibe no console as conquistas por sequência de vitórias. */
public class ConsoleNotificador implements NotificadorConquista {

  @Override
  public void conquistaObtida(Conquista conquista, int sequencia) {
    System.out.println("\nConquista obtida: " + conquista.getTitulo()
        + " por ganhar " + sequencia + " vitórias seguidas");
  }
}
