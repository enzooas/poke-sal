package br.edu.ucsal.pokesal.service;

/** Recebe os avisos de andamento do torneio. */
public interface AvisoTorneio {

  /** Anuncia o início de uma fase. */
  void inicioFase(int participantesNaFase);
}
