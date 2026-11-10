package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import java.util.ArrayList;
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
}
