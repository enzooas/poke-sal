package br.edu.ucsal.pokesal.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/** Testes da validação de precisão do golpe (limites de 0 a 100). */
public class GolpeTest {

  @Test
  public void testPrecisaoNosLimitesValidos() {
    assertEquals(0, new Golpe(0).getPrecisao());
    assertEquals(100, new Golpe(100).getPrecisao());
  }

  @Test
  public void testPrecisaoAbaixoDeZeroLancaExcecao() {
    assertThrows(IllegalArgumentException.class, () -> new Golpe(-1),
        "A precisão não pode ser menor que 0");
  }

  @Test
  public void testPrecisaoAcimaDeCemLancaExcecao() {
    assertThrows(IllegalArgumentException.class, () -> new Golpe(101),
        "A precisão não pode ser maior que 100");
  }
}
