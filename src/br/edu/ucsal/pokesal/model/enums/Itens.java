package br.edu.ucsal.pokesal.model.enums;

import br.edu.ucsal.pokesal.model.Pokesal;

/** Itens de batalha disponíveis na mochila do treinador. */
public enum Itens {
  POTION("Potion", 20),
  SUPERPOTION("SuperPotion", 50),
  ANTIDOTE("Antidote", 0);

  private final String nome;
  private final int valorCura;

  Itens(String nome, int valorCura) {
    this.nome = nome;
    this.valorCura = valorCura;
  }

  public String getNome() {
    return nome;
  }

  public int getValorCura() {
    return valorCura;
  }

  /** Indica se o item faz sentido para a condição atual do PokéSal. */
  public boolean podeUsarEm(Pokesal pokesal) {
    if (this == ANTIDOTE) {
      return pokesal.getStatus() == Status.ENVENENADO;
    }
    return pokesal.getHpAtual() < pokesal.getHpMax();
  }

  /** Aplica o efeito do item e devolve a descrição do que aconteceu. */
  public String aplicarEm(Pokesal pokesal) {
    if (this == ANTIDOTE) {
      pokesal.setStatus(Status.NORMAL);
      return pokesal.getNomeP() + " foi curado do envenenamento!";
    }
    int curado = pokesal.curar(valorCura);
    return pokesal.getNomeP() + " recuperou " + curado + " de HP! HP atual: "
        + pokesal.getHpAtual() + "/" + pokesal.getHpMax();
  }
}
