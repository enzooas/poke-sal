package br.edu.ucsal.pokesal.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Testes do histórico de modo normal e de torneio. */
public class HistoricoTest {

  private final Historico historico = new Historico();

  @Test
  public void testHistoricoNovoEstaVazio() {
    assertTrue(historico.estaVazio());
  }

  @Test
  public void testDerrotaZeraSequenciaEPreservaRecorde() {
    historico.registrarVitoriaNormal();
    historico.registrarVitoriaNormal();
    historico.registrarDerrotaNormal();
    historico.registrarVitoriaNormal();

    assertEquals(1, historico.getSequenciaAtual());
    assertEquals(2, historico.getMelhorSequencia());
    assertEquals(4, historico.getTotalJogosNormais());
    assertFalse(historico.estaVazio());
  }

  @Test
  public void testTorneioCampeaoNaoAlteraSequencia() {
    historico.registrarVitoriaNormal();
    historico.registrarTorneio(true, 2);

    assertEquals(1, historico.getSequenciaAtual());
    assertEquals(1, historico.getTitulos());
    assertEquals(2, historico.getVitoriasEmTorneio());
    assertEquals(0, historico.getDerrotasEmTorneio());
  }

  @Test
  public void testTorneioEliminadoContaDerrota() {
    historico.registrarTorneio(false, 1);

    assertEquals(0, historico.getTitulos());
    assertEquals(1, historico.getDerrotasEmTorneio());
    assertEquals(2, historico.getJogosEmTorneio());
    assertFalse(historico.estaVazio());
  }
}
