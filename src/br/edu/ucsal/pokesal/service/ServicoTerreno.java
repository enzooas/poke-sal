package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.enums.Terreno;
import java.util.List;

/** Informa quais terrenos podem ser sorteados nas condições atuais da batalha. */
public interface ServicoTerreno {

  /** Devolve os terrenos disponíveis para o sorteio agora. A lista nunca é vazia. */
  List<Terreno> terrenosDisponiveis();
}
