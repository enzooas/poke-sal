package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.enums.Conquista;

/** Avisa o jogador quando ele obtém uma conquista por sequência de vitórias+. */
public interface NotificadorConquista {

  /** Informa que uma conquista foi obtida. */
  void conquistaObtida(Conquista conquista, int sequencia);
}
