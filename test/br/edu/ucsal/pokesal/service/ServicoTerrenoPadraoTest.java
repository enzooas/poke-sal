package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.edu.ucsal.pokesal.model.enums.Terreno;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Testes dos terrenos liberados pelo horário.  */
@ExtendWith(MockitoExtension.class)
public class ServicoTerrenoPadraoTest {

  @Mock
  private Clock relogio;

  @InjectMocks
  private ServicoTerrenoPadrao servico;

  private static Instant horario(int hora, int minuto) {
    return LocalDateTime.of(2026, 11, 5, hora, minuto).toInstant(ZoneOffset.UTC);
  }

  @Test
  public void testAsfaltoQuenteApenasDuranteoDia() {
    when(relogio.getZone()).thenReturn(ZoneOffset.UTC);
    // retornos em sequência: meio-dia na primeira consulta, 22h na segunda
    when(relogio.instant()).thenReturn(horario(12, 0), horario(22, 0));

    assertTrue(servico.terrenosDisponiveis().contains(Terreno.ASFALTO_QUENTE),
        "Ao meio-dia o Asfalto Quente pode ser sorteado");
    assertFalse(servico.terrenosDisponiveis().contains(Terreno.ASFALTO_QUENTE),
        "Às 22h o Asfalto Quente fica fora do sorteio");
    verify(relogio, times(2)).instant();
    verify(relogio, atLeastOnce()).getZone();
  }
}
