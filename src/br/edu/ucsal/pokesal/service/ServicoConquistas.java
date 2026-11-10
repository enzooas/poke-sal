package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Historico;
import br.edu.ucsal.pokesal.model.enums.Conquista;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

/** Atualiza o histórico do modo normal e concede conquistas por sequência de vitórias. */
public class ServicoConquistas {

  private final NotificadorConquista notificador;
  private final Set<Conquista> conquistasObtidas = EnumSet.noneOf(Conquista.class);

  /** Cria o serviço com o notificador que será avisado quando uma meta for atingida. */
  public ServicoConquistas(NotificadorConquista notificador) {
    this.notificador = notificador;
  }

  /**
   * Registra uma vitória normal e concede a conquista se a sequência atingiu uma meta
   * cujo título ainda não foi obtido nesta execução.
   */
  public void registrarVitoria(Historico historico) {
    historico.registrarVitoriaNormal();
    int sequencia = historico.getSequenciaAtual();
    Optional<Conquista> conquista = Conquista.daSequencia(sequencia);
    if (conquista.isPresent() && conquistasObtidas.add(conquista.get())) {
      notificador.conquistaObtida(conquista.get(), sequencia);
    }
  }

  /** Registra uma derrota normal, que zera a sequência atual e preserva o recorde. */
  public void registrarDerrota(Historico historico) {
    historico.registrarDerrotaNormal();
  }
}
