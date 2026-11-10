package br.edu.ucsal.pokesal.model;

/** Golpe usado por um PokéSal em batalha, com a chance de acertar o alvo (0 a 100). */
public final class Golpe {

  private static final int PRECISAO_MINIMA = 0;
  private static final int PRECISAO_MAXIMA = 100;

  private final int precisao;

  /** Cria o golpe validando que a precisão está entre 0 e 100. */
  public Golpe(int precisao) {
    if (precisao < PRECISAO_MINIMA || precisao > PRECISAO_MAXIMA) {
      throw new IllegalArgumentException(
          "A precisão do golpe deve estar entre " + PRECISAO_MINIMA + " e " + PRECISAO_MAXIMA
              + ", mas foi " + precisao + ".");
    }
    this.precisao = precisao;
  }

  public int getPrecisao() {
    return precisao;
  }
}
