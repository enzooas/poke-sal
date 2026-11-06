package br.edu.ucsal.pokesal.service;

/** Avisa o jogador quando ele atinge um marco de vitórias seguidas. */
public interface NotificadorConquista {

  /** Informa que uma conquista foi desbloqueada. */
  void conquistaDesbloqueada(int sequencia);
}