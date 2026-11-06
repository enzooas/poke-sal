package br.edu.ucsal.pokesal.view;

import br.edu.ucsal.pokesal.service.NotificadorConquista;

/** Exibe no console as conquistas por sequência de vitórias. */
public class ConsoleNotificador implements NotificadorConquista {

  @Override
  public void conquistaDesbloqueada(int sequencia) {
    System.out.println("\n*** CONQUISTA: " + sequencia + " vitórias seguidas! ***");
  }
}
