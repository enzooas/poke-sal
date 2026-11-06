package br.edu.ucsal.pokesal.model;

import java.util.List;

/** Resultado de um torneio: o campeão e todas as batalhas disputadas. */
public class ResultadoTorneio {

  private final Treinador campeao;
  private final List<ResultadoBatalha> batalhas;

  /** Cria o resultado com o campeão e as batalhas, na ordem em que ocorreram. */
  public ResultadoTorneio(Treinador campeao, List<ResultadoBatalha> batalhas) {
    this.campeao = campeao;
    this.batalhas = List.copyOf(batalhas);
  }

  public Treinador getCampeao() {
    return campeao;
  }

  public List<ResultadoBatalha> getBatalhas() {
    return batalhas;
  }

  /** Conta quantas batalhas o treinador informado venceu no torneio. */
  public int vitoriasDe(String nomeTreinador) {
    int vitorias = 0;
    for (ResultadoBatalha batalha : batalhas) {
      if (batalha.venceu(nomeTreinador)) {
        vitorias++;
      }
    }
    return vitorias;
  }
}
