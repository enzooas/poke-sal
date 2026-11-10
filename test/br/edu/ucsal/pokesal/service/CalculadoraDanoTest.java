package br.edu.ucsal.pokesal.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import org.junit.jupiter.api.Test;

/** Testes do cálculo de dano, com terreno e crítico informados diretamente. */
public class CalculadoraDanoTest {

  private final CalculadoraDano calculadora = new CalculadoraDano();

  @Test
  public void testEfeitoTerrenoEstacionamentoUcsal() {
    Pokesal atacante = new Pokesal(SalDex.CHARSAL);
    Pokesal defensor = new Pokesal(SalDex.CHARSAL);

    int semBonus = calculadora.calcular(atacante, defensor, Terreno.CANTEIRO_CENTRAL, false);
    int comAsfalto = calculadora.calcular(atacante, defensor, Terreno.ASFALTO_QUENTE, false);
    int comPoca = calculadora.calcular(atacante, defensor, Terreno.POCA_CHUVA, false);

    assertAll(
        () -> assertEquals(9, semBonus, "Sem bônus: 52 - 43 = 9"),
        () -> assertEquals(10, comAsfalto, "Asfalto Quente: 9 x 1,15 = 10,35 truncado"),
        () -> assertEquals(9, comPoca, "Poça de Chuva não bonifica Fogo"));
  }

  @Test
  public void testCalculoDanoBoundaryValues() {
    Pokesal alvo = new Pokesal(SalDex.BULBASAL);
    int hpMaximo = alvo.getHpMax();

    alvo.receberDano(hpMaximo + 1000);
    assertEquals(0, alvo.getHpAtual(), "HP nunca fica negativo");
    assertTrue(alvo.estaDerrotado());

    alvo.curar(hpMaximo + 1000);
    assertEquals(hpMaximo, alvo.getHpAtual(), "Cura não ultrapassa o HP máximo");

    Pokesal bulbasal = new Pokesal(SalDex.BULBASAL);
    Pokesal squirtsal = new Pokesal(SalDex.SQUIRTSAL);
    Pokesal charsal = new Pokesal(SalDex.CHARSAL);
    Terreno neutro = Terreno.POCA_CHUVA;

    assertAll(
        () -> assertEquals(2,
            calculadora.calcular(bulbasal, squirtsal, neutro, false),
            "ATK < DEF: piso 1 x 2,0 (Planta vence Água)"),
        () -> assertEquals(1,
            calculadora.calcular(bulbasal, bulbasal, neutro, false),
            "ATK = DEF: dano 0 sobe ao piso 1"),
        () -> assertEquals(6,
            calculadora.calcular(charsal, bulbasal, neutro, false),
            "ATK = DEF + 3: 3 x 2,0"),
        () -> assertEquals(1,
            calculadora.calcular(charsal, squirtsal, neutro, false),
            "Piso 1 x 0,5 = 0,5 trunca para 0 e volta ao piso 1"));
  }

  @Test
  public void testDanoCritico() {
    Pokesal atacante = new Pokesal(SalDex.CHARSAL);
    Pokesal defensor = new Pokesal(SalDex.CHARSAL);

    int normal = calculadora.calcular(atacante, defensor, Terreno.CANTEIRO_CENTRAL, false);
    int critico = calculadora.calcular(atacante, defensor, Terreno.CANTEIRO_CENTRAL, true);

    assertAll(
        () -> assertEquals(9, normal),
        () -> assertEquals(13, critico, "9 x 1,5 = 13,5 truncado"));
  }
}