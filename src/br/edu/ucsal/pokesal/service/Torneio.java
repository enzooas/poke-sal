package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;
import java.util.ArrayList;
import java.util.List;

/** Torneio eliminatório entre 4 ou 8 treinadores. */
public class Torneio {

  private static final int MINIMO_PARTICIPANTES = 4;
  private static final int MAXIMO_PARTICIPANTES = 8;

  private final List<Treinador> participantes;
  private final FabricaDeBatalha fabrica;

  /** Cria o torneio com os participantes na ordem do chaveamento. */
  public Torneio(List<Treinador> participantes, FabricaDeBatalha fabrica) {
    int total = participantes.size();
    if (total != MINIMO_PARTICIPANTES && total != MAXIMO_PARTICIPANTES) {
      throw new IllegalArgumentException("O torneio exige 4 ou 8 treinadores,"
          + " mas recebeu " + total);
    }
    this.participantes = new ArrayList<>(participantes);
    this.fabrica = fabrica;
  }

  /** Disputa todas as fases e devolve o campeão. */
  public Treinador disputar() {
    List<Treinador> fase = new ArrayList<>(participantes);
    while (fase.size() > 1) {
      fase = disputarFase(fase);
    }
    return fase.get(0);
  }

  private List<Treinador> disputarFase(List<Treinador> fase) {
    List<Treinador> vencedores = new ArrayList<>();
    for (int i = 0; i < fase.size(); i += 2) {
      Treinador a = fase.get(i);
      Treinador b = fase.get(i + 1);
      ResultadoBatalha resultado = fabrica.disputar(a, b);
      vencedores.add(resultado.venceu(a.getNome()) ? a : b);
    }
    return vencedores;
  }
}