package br.edu.ucsal.pokesal.model.enums;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.edu.ucsal.pokesal.model.Pokesal;
import org.junit.jupiter.api.Test;

/** Testes das regras de uso e efeito dos itens de batalha. */
public class ItensTest {

  private static final int DANO_PEQUENO = 10;

  @Test
  public void testPotionNaoPodeSerUsadaComHpCheio() {
    Pokesal pokesal = new Pokesal(SalDex.BULBASAL);

    assertFalse(Itens.POTION.podeUsarEm(pokesal));
    assertFalse(Itens.SUPERPOTION.podeUsarEm(pokesal));
  }

  @Test
  public void testPotionPodeSerUsadaComHpAbaixoDoMaximo() {
    Pokesal pokesal = new Pokesal(SalDex.BULBASAL);
    pokesal.receberDano(DANO_PEQUENO);

    assertAll(
        () -> assertTrue(Itens.POTION.podeUsarEm(pokesal)),
        () -> assertTrue(Itens.SUPERPOTION.podeUsarEm(pokesal)));
  }

  @Test
  public void testAntidoteSoPodeSerUsadoComEnvenenado() {
    Pokesal pokesal = new Pokesal(SalDex.BULBASAL);
    assertFalse(Itens.ANTIDOTE.podeUsarEm(pokesal));

    pokesal.setStatus(Status.QUEIMADO);
    assertFalse(Itens.ANTIDOTE.podeUsarEm(pokesal));

    pokesal.setStatus(Status.ENVENENADO);
    assertTrue(Itens.ANTIDOTE.podeUsarEm(pokesal));
  }

  @Test
  public void testPotionCuraSemUltrapassarHpMaximo() {
    Pokesal pokesal = new Pokesal(SalDex.BULBASAL);
    pokesal.receberDano(DANO_PEQUENO);

    String mensagem = Itens.POTION.aplicarEm(pokesal);

    assertEquals(pokesal.getHpMax(), pokesal.getHpAtual());
    assertTrue(mensagem.contains("recuperou " + DANO_PEQUENO + " de HP"));
  }

  @Test
  public void testSuperPotionCuraMaisQuePotion() {
    Pokesal comPotion = new Pokesal(SalDex.TOTOSAL);
    Pokesal comSuper = new Pokesal(SalDex.TOTOSAL);
    comPotion.receberDano(comPotion.getHpMax() - 1);
    comSuper.receberDano(comSuper.getHpMax() - 1);

    Itens.POTION.aplicarEm(comPotion);
    Itens.SUPERPOTION.aplicarEm(comSuper);

    assertAll(
        () -> assertEquals(1 + Itens.POTION.getValorCura(), comPotion.getHpAtual()),
        () -> assertEquals(comSuper.getHpMax(), comSuper.getHpAtual()));
  }

  @Test
  public void testAntidoteVoltaStatusParaNormal() {
    Pokesal pokesal = new Pokesal(SalDex.BULBASAL);
    pokesal.setStatus(Status.ENVENENADO);

    String mensagem = Itens.ANTIDOTE.aplicarEm(pokesal);

    assertEquals(Status.NORMAL, pokesal.getStatus());
    assertTrue(mensagem.contains("curado"));
  }
}
