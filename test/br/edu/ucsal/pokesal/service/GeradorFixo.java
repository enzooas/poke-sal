package br.edu.ucsal.pokesal.service;

/** Gerador de teste que devolve sempre os mesmos valores. */
public class GeradorFixo implements GeradorAleatorio {

  private final int inteiro;
  private final double decimal;

  /** Cria o gerador com os valores fixos que serão devolvidos. */
  public GeradorFixo(int inteiro, double decimal) {
    this.inteiro = inteiro;
    this.decimal = decimal;
  }

  @Override
  public int proximoInteiro(int limite) {
    return inteiro;
  }

  @Override
  public double proximoDecimal() {
    return decimal;
  }
}