package br.edu.ucsal.pokesal.model;

/** Acumula o desempenho do jogador durante a execução do programa. */
public class Historico {

  private int totalJogos;
  private int vitorias;
  private int derrotas;

  /** Registra uma vitória e soma uma batalha ao total. */
  public void registrarVitoria() {
    totalJogos++;
    vitorias++;
  }

  /** Registra uma derrota e soma uma batalha ao total. */
  public void registrarDerrota() {
    totalJogos++;
    derrotas++;
  }

  /** Indica se nenhuma batalha foi registrada ainda. */
  public boolean estaVazio() {
    return totalJogos == 0;
  }

  public int getTotalJogos() {
    return totalJogos;
  }

  public int getVitorias() {
    return vitorias;
  }

  public int getDerrotas() {
    return derrotas;
  }
}