package br.edu.ucsal.pokesal.model.enums;

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
}
