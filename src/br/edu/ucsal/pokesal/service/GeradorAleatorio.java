package br.edu.ucsal.pokesal.service;

/** Fonte de aleatoriedade do jogo. */
public interface GeradorAleatorio {

  /** Sorteia um número inteiro dentro de um intervalo. */
  int proximoInteiro(int limite);

  /** Sorteia um número decimal para decidir eventos com chance de ocorrer. */
  double proximoDecimal();
}
