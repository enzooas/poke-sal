package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;

/** Disputa uma batalha entre dois treinadores e informa o resultado. */
public interface FabricaDeBatalha {

  /** Disputa a batalha completa entre dois treinadores. */
  ResultadoBatalha disputar(Treinador primeiro, Treinador segundo);
}