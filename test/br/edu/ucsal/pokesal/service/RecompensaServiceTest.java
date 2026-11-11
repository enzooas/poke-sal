package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Testes da recompensa por vitória. */
public class RecompensaServiceTest {

  private static GeradorAleatorio sorteioFixo(int valor) {
    return new GeradorAleatorio() {
      @Override
      public int proximoInteiro(int limite) {
        return valor;
      }

      @Override
      public double proximoDecimal() {
        return 0.0;
      }
    };
  }

  @Test
  public void testSistemaRecompensa() {
    Treinador treinador = new Treinador("J", SalDex.BULBASAL, new Mochila(new ArrayList<>()));
    RecompensaService service = new RecompensaService(sorteioFixo(1));

    Itens premio = service.concederRecompensa(treinador);

    assertAll(
        () -> assertEquals(Itens.SUPERPOTION, premio),
        () -> assertTrue(treinador.getMochila().getItens().contains(Itens.SUPERPOTION)));
  }
  
  @Test
  public void testRecompensaComQuantidadeSorteiaVariasVezes() {
    GeradorAleatorio geradorMock = org.mockito.Mockito.mock(GeradorAleatorio.class);
    org.mockito.Mockito.when(geradorMock.proximoInteiro(3)).thenReturn(0, 1, 2);
    Treinador treinador = new Treinador("J", SalDex.BULBASAL, new Mochila(new ArrayList<>()));

    List<Itens> itens = new RecompensaService(geradorMock).concederRecompensa(treinador, 3);

    assertEquals(List.of(Itens.POTION, Itens.SUPERPOTION, Itens.ANTIDOTE), itens);
    assertEquals(3, treinador.getMochila().getItens().size());
    org.mockito.Mockito.verify(geradorMock, org.mockito.Mockito.times(3)).proximoInteiro(3);
  }
}
