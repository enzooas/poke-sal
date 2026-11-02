package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Acao;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;

/** Escolha automática: a cpu sempre ataca. */
public class CpuEscolha implements EscolhaDeAcao {

  @Override
  public Acao escolher(Treinador treinador, Pokesal pokesal) {
    return Acao.atacar();
  }
}