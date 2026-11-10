package br.edu.ucsal.pokesal.model;

/** Acumula o desempenho do jogador durante a execução do programa. */
public class Historico {

  private int vitoriasNormais;
  private int derrotasNormais;
  private int sequenciaAtual;
  private int melhorSequencia;
  private int torneiosDisputados;
  private int titulos;
  private int vitoriasEmTorneio;
  private int derrotasEmTorneio;

  /** Registra vitória no modo normal e aumenta a sequência de vitórias seguidas. */
  public void registrarVitoriaNormal() {
    vitoriasNormais++;
    sequenciaAtual++;
    if (sequenciaAtual > melhorSequencia) {
      melhorSequencia = sequenciaAtual;
    }
  }

  /** Registra derrota no modo normal e zera a sequência atual. */
  public void registrarDerrotaNormal() {
    derrotasNormais++;
    sequenciaAtual = 0;
  }

  /** Registra um torneio disputado, sem alterar a sequência de vitórias do modo normal. */
  public void registrarTorneio(boolean campeao, int vitorias) {
    torneiosDisputados++;
    vitoriasEmTorneio += vitorias;
    if (campeao) {
      titulos++;
    } else {
      derrotasEmTorneio++;
    }
  }

  /** Indica se nenhuma batalha foi registrada ainda. */
  public boolean estaVazio() {
    return getTotalJogosNormais() == 0 && torneiosDisputados == 0;
  }

  public int getTotalJogosNormais() {
    return vitoriasNormais + derrotasNormais;
  }

  public int getVitoriasNormais() {
    return vitoriasNormais;
  }

  public int getDerrotasNormais() {
    return derrotasNormais;
  }

  public int getSequenciaAtual() {
    return sequenciaAtual;
  }

  public int getMelhorSequencia() {
    return melhorSequencia;
  }

  public int getTorneiosDisputados() {
    return torneiosDisputados;
  }

  public int getTitulos() {
    return titulos;
  }

  public int getJogosEmTorneio() {
    return vitoriasEmTorneio + derrotasEmTorneio;
  }

  public int getVitoriasEmTorneio() {
    return vitoriasEmTorneio;
  }

  public int getDerrotasEmTorneio() {
    return derrotasEmTorneio;
  }
}
