package br.edu.ucsal.pokesal.model.enums;

/** Itens de batalha disponíveis na mochila do treinador. */
public enum Itens {
  POTION("Potion", 20, Status.NORMAL),
  SUPERPOTION("SuperPotion", 50, Status.NORMAL),
  ANTIDOTE("Antidote", 0, Status.NORMAL);

  private final String nome;
  private final int valorCura;
  private final Status statusCura;

  Itens(String nome, int valorCura, Status statusCura) {
    this.nome = nome;
    this.valorCura = valorCura;
    this.statusCura = statusCura;
  }

  public String getNome() {
    return nome;
  }

  public int getValorCura() {
    return valorCura;
  }

  public Status getStatusCura() {
    return statusCura;
  }
}
