package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.ResultadoTorneio;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Testes do chaveamento do torneio, com as batalhas substituídas por mocks. */
@ExtendWith(MockitoExtension.class)
public class TorneioTest {

  @Mock
  private FabricaDeBatalha fabrica;
  @Mock
  private AvisoTorneio aviso;

  private Treinador treinador(String nome) {
    return new Treinador(nome, SalDex.CHARSAL, new Mochila(new ArrayList<>()));
  }

  private ResultadoBatalha vitoria(String vencedor, String perdedor) {
    return new ResultadoBatalha(vencedor, perdedor, 3, "CharSal", "CharSal");
  }

  @Test
  public void testChaveamentoTorneioAvancaVencedores() {
    Treinador t1 = treinador("T1");
    Treinador t2 = treinador("T2");
    Treinador t3 = treinador("T3");
    Treinador t4 = treinador("T4");
    // Valores exatos: o par de cada confronto É o que se quer provar.
    when(fabrica.disputar(t1, t2)).thenReturn(vitoria("T2", "T1"));
    when(fabrica.disputar(t3, t4)).thenReturn(vitoria("T3", "T4"));
    when(fabrica.disputar(t2, t3)).thenReturn(vitoria("T3", "T2"));

    ResultadoTorneio resultado =
        new Torneio(List.of(t1, t2, t3, t4), fabrica, aviso).disputar();

    assertSame(t3, resultado.getCampeao());
    assertEquals(3, resultado.getBatalhas().size());
    assertEquals(2, resultado.vitoriasDe("T3"));
    InOrder ordem = inOrder(aviso, fabrica);
    ordem.verify(aviso).inicioFase(4);
    ordem.verify(fabrica).disputar(t1, t2);
    ordem.verify(fabrica).disputar(t3, t4);
    ordem.verify(aviso).inicioFase(2);
    ordem.verify(fabrica).disputar(t2, t3);
    verify(fabrica, times(3)).disputar(any(Treinador.class), any(Treinador.class));
    verify(fabrica, never()).disputar(t1, t4);
    verify(aviso, never()).inicioFase(8);
  }

  @Test
  public void testTorneioRejeitaQuantidadeInvalidaDeParticipantes() {
    List<Treinador> tres = List.of(treinador("T1"), treinador("T2"), treinador("T3"));

    assertThrows(IllegalArgumentException.class, () -> new Torneio(tres, fabrica, aviso));

    verifyNoInteractions(fabrica);
  }
}
