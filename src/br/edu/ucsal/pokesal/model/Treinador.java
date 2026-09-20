package br.edu.ucsal.pokesal.model;

import br.edu.ucsal.pokesal.model.enums.SalDex;

/** Representa um treinador, com o PokéSal escolhido e a mochila de itens. */
public class Treinador {

  private final String nome;
  private final SalDex escolha;
  private final Mochila mochila;

  /** Cria o treinador com o nome, o PokéSal escolhido e a mochila informados. */
  public Treinador(String nome, SalDex escolha, Mochila mochila) {
    this.nome = nome;
    this.escolha = escolha;
    this.mochila = mochila;
  }

  public String getNome() {
    return nome;
  }

  public SalDex getEscolha() {
    return escolha;
  }

  public Mochila getMochila() {
    return mochila;
  }

  /** Instancia um novo PokéSal a partir da escolha registrada para este treinador. */
  public Pokesal criarPokesal() {
    return new Pokesal(escolha);
  }
}
