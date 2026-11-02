package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.model.enums.Status;
import org.junit.jupiter.api.Test;

/** Testes da ordem de ataque definida pela velocidade. */
public class IniciativaTest {

  /** Gerador que sempre devolve o mesmo inteiro, usado no sorteio de empate. */
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
  public void testOrdemDeAtaquePorVelocidade() {
    Iniciativa iniciativa = new Iniciativa(sorteioFixo(0));
    Pokesal rapido = new Pokesal(SalDex.CHARSAL);
    Pokesal lento = new Pokesal(SalDex.BULBASAL);

    assertAll(
        () -> assertTrue(iniciativa.primeiroAtacaAntes(rapido, lento)),
        () -> assertFalse(iniciativa.primeiroAtacaAntes(lento, rapido)));
  }

  @Test
  public void testParalisiaReduzVelocidadeeInverteOrdem() {
    Iniciativa iniciativa = new Iniciativa(sorteioFixo(0));
    Pokesal rapido = new Pokesal(SalDex.CHARSAL);
    Pokesal lento = new Pokesal(SalDex.BULBASAL);
    rapido.setStatus(Status.PARALISADO);

    assertAll(
        () -> assertEquals(32, iniciativa.velocidadeEfetiva(rapido)),
        () -> assertFalse(iniciativa.primeiroAtacaAntes(rapido, lento)));
  }

  @Test
  public void testEmpateDeVelocidadeUsaSorteio() {
    Pokesal a = new Pokesal(SalDex.CHARSAL);
    Pokesal b = new Pokesal(SalDex.CYNDASAL);

    assertAll(
        () -> assertTrue(new Iniciativa(sorteioFixo(0)).primeiroAtacaAntes(a, b)),
        () -> assertFalse(new Iniciativa(sorteioFixo(1)).primeiroAtacaAntes(a, b)));
  }
}