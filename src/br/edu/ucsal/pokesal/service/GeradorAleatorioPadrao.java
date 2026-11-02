package br.edu.ucsal.pokesal.service;

import java.util.Random;

/** Gerador de produção, baseado em java.util.Random. */
public class GeradorAleatorioPadrao implements GeradorAleatorio {

  private final Random random;

  /** Cria o gerador aleatorio com numeros randomizados. */
  public GeradorAleatorioPadrao(Random random) {
    this.random = random;
  }

  @Override
  public int proximoInteiro(int limite) {
    return random.nextInt(limite);
  }

  @Override
  public double proximoDecimal() {
    return random.nextDouble();
  }
}