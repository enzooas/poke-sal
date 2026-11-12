package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.edu.ucsal.pokesal.model.Acao;
import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import br.edu.ucsal.pokesal.repository.RepositorioResultados;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Testes da BatalhaService isolada de todos os colaboradores.  */
@ExtendWith(MockitoExtension.class)
public class BatalhaServiceTest {

  @Mock
  private GeradorAleatorio gerador;
  @Mock
  private CalculadoraDano calculadora;
  @Mock
  private Iniciativa iniciativa;
  @Mock
  private EscolhaDeAcao escolhaJogador;
  @Mock
  private EscolhaDeAcao escolhaCpu;
  @Mock
  private RegistroBatalha registro;
  @Mock
  private RepositorioResultados repositorio;
  @Mock
  private ServicoTerreno servicoTerreno;

  private Treinador treinador(String nome, SalDex salDex, Itens... itens) {
    return new Treinador(nome, salDex, new Mochila(new ArrayList<>(List.of(itens))));
  }

  /** Monta a batalha com terreno fixo; o sorteio do terreno e o do acerto sempre dão 0. */
  private BatalhaService criarBatalha(Treinador jogador, Treinador cpu, Terreno terreno) {
    when(servicoTerreno.terrenosDisponiveis()).thenReturn(List.of(terreno));
    when(gerador.proximoInteiro(anyInt())).thenReturn(0);
    return new BatalhaService(jogador, cpu, gerador, calculadora, iniciativa,
        escolhaJogador, escolhaCpu, registro, repositorio, servicoTerreno);
  }

  /**
   * Batalha em que o jogador (CharSal) vence a CPU (BulbaSal) em 2 rodadas: dano 10, 10 e 100,
   * sem crítico, contra-ataque nem status.
   */
  private BatalhaService criarBatalhaVencidaPeloJogador() {
    final BatalhaService batalha = criarBatalha(
        treinador("Jogador", SalDex.CHARSAL), treinador("CPU", SalDex.BULBASAL),
        Terreno.POCA_CHUVA);
    when(iniciativa.primeiroAtacaAntes(any(Pokesal.class), any(Pokesal.class))).thenReturn(true);
    when(escolhaJogador.escolher(any(Treinador.class), any(Pokesal.class)))
        .thenReturn(Acao.atacar());
    when(escolhaCpu.escolher(any(Treinador.class), any(Pokesal.class)))
        .thenReturn(Acao.atacar());
    when(calculadora.calcular(any(Pokesal.class), any(Pokesal.class), any(Terreno.class),
        anyBoolean())).thenReturn(10, 10, 100);
    when(gerador.proximoDecimal()).thenReturn(0.5);
    return batalha;
  }

  @Test
  public void testSequenciaDeEventosNoTurno() {
    Treinador jogador = treinador("Jogador", SalDex.CHARSAL);
    Treinador cpu = treinador("CPU", SalDex.BULBASAL, Itens.POTION);
    final BatalhaService batalha = criarBatalha(jogador, cpu, Terreno.CANTEIRO_CENTRAL);

    // Sequências: rodada 1 o jogador começa; na rodada 2 a CPU começa e usa Potion.
    when(iniciativa.primeiroAtacaAntes(any(Pokesal.class), any(Pokesal.class)))
        .thenReturn(true, false);
    when(escolhaJogador.escolher(any(Treinador.class), any(Pokesal.class)))
        .thenReturn(Acao.atacar());
    when(escolhaCpu.escolher(any(Treinador.class), any(Pokesal.class)))
        .thenReturn(Acao.atacar(), Acao.usarItem(Itens.POTION));
    when(calculadora.calcular(any(Pokesal.class), any(Pokesal.class), any(Terreno.class),
        anyBoolean())).thenReturn(5, 5, 100);
    // Ordem dos sorteios: contra-ataque, crítico, status. Só o 3º (status) é sorteado como 0,1.
    when(gerador.proximoDecimal()).thenReturn(0.5, 0.5, 0.1, 0.5);

    batalha.iniciarBatalha();

    InOrder ordem = inOrder(registro);
    ordem.verify(registro).ataque("CharSal (Jogador)", "BulbaSal (CPU)");
    ordem.verify(registro).dano("BulbaSal (CPU)", 5, 40, 45);
    ordem.verify(registro).efeitoStatus("BulbaSal (CPU) foi queimado!");
    ordem.verify(registro).efeitoTerreno(anyString());
    ordem.verify(registro).usoItem(anyString());
    verify(registro, atLeastOnce()).fimDeTurno(anyString(), anyInt(), anyInt());
  }

  @Test
  public void testResultadoSalvoAoFimDaBatalha() {
    BatalhaService batalha = criarBatalhaVencidaPeloJogador();

    final ResultadoBatalha devolvido = batalha.iniciarBatalha();

    ArgumentCaptor<ResultadoBatalha> captor = ArgumentCaptor.forClass(ResultadoBatalha.class);
    verify(repositorio, times(1)).salvar(captor.capture());
    ResultadoBatalha salvo = captor.getValue();
    assertEquals("Jogador", salvo.getVencedor());
    assertEquals("CPU", salvo.getPerdedor());
    assertEquals(2, salvo.getTurnos());
    assertEquals("CharSal", salvo.getPokesalVencedor());
    assertEquals("BulbaSal", salvo.getPokesalPerdedor());
    assertEquals(salvo, devolvido);
    verify(registro, times(2)).inicioRodada(anyInt());
  }

  @Test
  public void testFalhaNoRepositorioNaoInvalidaBatalha() {
    BatalhaService batalha = criarBatalhaVencidaPeloJogador();
    doThrow(new IllegalStateException("disco cheio"))
        .when(repositorio).salvar(any(ResultadoBatalha.class));

    ResultadoBatalha resultado = batalha.iniciarBatalha();

    assertTrue(resultado.venceu("Jogador"));
    verify(registro).erro("Falha ao salvar o resultado da batalha: disco cheio");
  }

  @Test
  public void testRepositorioNaoChamadoSeBatalhaNaoTerminou() {
    BatalhaService batalha = criarBatalha(
        treinador("Jogador", SalDex.CHARSAL), treinador("CPU", SalDex.BULBASAL),
        Terreno.POCA_CHUVA);
    when(iniciativa.primeiroAtacaAntes(any(Pokesal.class), any(Pokesal.class))).thenReturn(true);
    when(escolhaJogador.escolher(any(Treinador.class), any(Pokesal.class)))
        .thenThrow(new IllegalStateException("entrada encerrada"));

    assertThrows(IllegalStateException.class, batalha::iniciarBatalha);

    verify(repositorio, never()).salvar(any(ResultadoBatalha.class));
    verify(registro, never()).erro(anyString());
  }
}
