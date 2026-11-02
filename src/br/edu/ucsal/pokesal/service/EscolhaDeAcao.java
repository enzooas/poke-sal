package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Acao;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;

/** Decide a ação de um treinador no turno, seja ele humano ou automático. */
public interface EscolhaDeAcao {

  /** Escolhe a ação do turno. */
  Acao escolher(Treinador treinador, Pokesal pokesal);
}