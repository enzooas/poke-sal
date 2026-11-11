package br.edu.ucsal.pokesal.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import br.edu.ucsal.pokesal.model.Historico;
import br.edu.ucsal.pokesal.model.enums.Conquista;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Testes da conquista por sequência de vitórias (requisito autoral 2). */
@ExtendWith(MockitoExtension.class)
public class ServicoConquistasTest {

  @Mock
  private NotificadorConquista notificador;

  @InjectMocks
  private ServicoConquistas servico;

  private final Historico historico = new Historico();

  private void vencer(int vezes) {
    for (int i = 0; i < vezes; i++) {
      servico.registrarVitoria(historico);
    }
  }

  @Test
  public void testConquistaConcedidaNaMetaDeVitoriasSeguidas() {
    vencer(5);

    InOrder ordem = inOrder(notificador);
    ordem.verify(notificador).conquistaObtida(Conquista.TREINADOR_EXEMPLAR, 3);
    ordem.verify(notificador).conquistaObtida(Conquista.SUPER_TREINADOR, 5);
    verify(notificador, times(2)).conquistaObtida(any(Conquista.class), anyInt());
    verify(notificador, never()).conquistaObtida(eq(Conquista.REI_DO_JOGO), anyInt());
  }

  @Test
  public void testDerrotaZeraSequenciaSemConceder() {
    vencer(2);
    servico.registrarDerrota(historico);
    vencer(2);

    verifyNoInteractions(notificador);
  }

  @Test
  public void testTituloConcedidoApenasUmaVezPorExecucao() {
    vencer(3);
    servico.registrarDerrota(historico);
    vencer(3);

    verify(notificador, times(1)).conquistaObtida(Conquista.TREINADOR_EXEMPLAR, 3);
  }
}
