package br.edu.ucsal.pokesal.model.enums;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/** Testes da matriz de vantagens entre tipos elementais. */
public class TipoElementalTest {

  private static final double TOLERANCIA = 0.0001;

  @Test
  public void testVantagemElemental() {
    assertAll(
        () -> assertEquals(2.0,
          TipoElemental.FOGO.multiplicadorContra(TipoElemental.PLANTA), TOLERANCIA),
        () -> assertEquals(0.5,
          TipoElemental.FOGO.multiplicadorContra(TipoElemental.AGUA), TOLERANCIA),
        () -> assertEquals(1.0,
          TipoElemental.FOGO.multiplicadorContra(TipoElemental.FOGO), TOLERANCIA),
        () -> assertEquals(2.0,
          TipoElemental.AGUA.multiplicadorContra(TipoElemental.FOGO), TOLERANCIA),
        () -> assertEquals(0.5,
          TipoElemental.AGUA.multiplicadorContra(TipoElemental.PLANTA), TOLERANCIA),
        () -> assertEquals(1.0,
          TipoElemental.AGUA.multiplicadorContra(TipoElemental.AGUA), TOLERANCIA),
        () -> assertEquals(2.0,
          TipoElemental.PLANTA.multiplicadorContra(TipoElemental.AGUA), TOLERANCIA),
        () -> assertEquals(0.5,
          TipoElemental.PLANTA.multiplicadorContra(TipoElemental.FOGO), TOLERANCIA),
        () -> assertEquals(1.0,
          TipoElemental.PLANTA.multiplicadorContra(TipoElemental.PLANTA), TOLERANCIA));
  }
}
