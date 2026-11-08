package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.enums.Terreno;
import java.time.Clock;
import java.time.LocalTime;
import java.util.List;

/**
 * Decide os terrenos pelo horário: durante o dia os três podem ser sorteados; à noite o Asfalto
 * Quente sai do sorteio, sobrando a Poça de Chuva e o Canteiro Central.
 */
public class ServicoTerrenoPadrao implements ServicoTerreno {

  private static final LocalTime INICIO_DIA = LocalTime.of(6, 0);
  private static final LocalTime FIM_DIA = LocalTime.of(18, 0);
  private static final List<Terreno> TERRENOS_DIA = List.of(Terreno.values());
  private static final List<Terreno> TERRENOS_NOITE =
      List.of(Terreno.POCA_CHUVA, Terreno.CANTEIRO_CENTRAL);

  private final Clock relogio;

  /** Cria o serviço usando o relógio informado como fonte do horário atual. */
  public ServicoTerrenoPadrao(Clock relogio) {
    this.relogio = relogio;
  }

  @Override
  public List<Terreno> terrenosDisponiveis() {
    if (ehDia()) {
      return TERRENOS_DIA;
    }
    return TERRENOS_NOITE;
  }

  private boolean ehDia() {
    LocalTime agora = LocalTime.now(relogio);
    return !agora.isBefore(INICIO_DIA) && agora.isBefore(FIM_DIA);
  }
}
