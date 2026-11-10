package br.edu.ucsal.pokesal.view;

import br.edu.ucsal.pokesal.service.AvisoTorneio;

/** Exibe no console o nome de cada fase do torneio. */
public class ConsoleAvisoTorneio implements AvisoTorneio {

  private static final int FINAL = 2;
  private static final int SEMIFINAL = 4;
  private static final int QUARTAS = 8;

  @Override
  public void inicioFase(int participantesNaFase) {
    String nome;
    if (participantesNaFase == FINAL) {
      nome = "Final";
    } else if (participantesNaFase == SEMIFINAL) {
      nome = "Semifinal";
    } else if (participantesNaFase == QUARTAS) {
      nome = "Quartas de Final";
    } else {
      nome = "Fase com " + participantesNaFase + " treinadores";
    }
    System.out.println("\n########## " + nome + " ##########");
  }
}
