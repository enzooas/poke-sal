package br.edu.ucsal.pokesal.model;

import br.edu.ucsal.pokesal.model.enums.Itens;

/** Decisão tomada por um treinador no seu turno: atacar ou usar um item. */
public class Acao {

  private final Itens item;

  private Acao(Itens item) {
    this.item = item;
  }

  /** Cria uma ação de ataque. */
  public static Acao atacar() {
    return new Acao(null);
  }

  /** Cria uma ação de uso de item. */
  public static Acao usarItem(Itens item) {
    return new Acao(item);
  }

  /** Confere se a ação escolhida é um ataque. */
  public boolean ehAtaque() {
    return item == null;
  }

  public Itens getItem() {
    return item;
  }
}