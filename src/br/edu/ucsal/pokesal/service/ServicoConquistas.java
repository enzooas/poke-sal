package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Historico;
import java.util.List;

/** Atualiza o histórico do modo normal e dispara conquistas por sequência de vitórias. */
public class ServicoConquistas {

  private static final List<Integer> MARCOS = List.of(3, 5, 10);

  private final NotificadorConquista notificador;

  /** Cria o serviço com o notificador que será avisado nos marcos. */
  public ServicoConquistas(NotificadorConquista notificador) {
    this.notificador = notificador;
  }

  /** Registra uma vitória normal e avisa se a sequência atingiu um marco. */
  public void registrarVitoria(Historico historico) {
    historico.registrarVitoriaNormal();
    if (MARCOS.contains(historico.getSequenciaAtual())) {
      notificador.conquistaDesbloqueada(historico.getSequenciaAtual());
    }
  }

  /** Registra uma derrota normal, que zera a sequência atual. */
  public void registrarDerrota(Historico historico) {
    historico.registrarDerrotaNormal();
  }
}
