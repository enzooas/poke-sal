package br.edu.ucsal.pokesal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.edu.ucsal.pokesal.model.Acao;
import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import br.edu.ucsal.pokesal.repository.RepositorioResultados;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Testes da fábrica de batalhas do torneio: quem usa a escolha manual e qual registro é usado. */
@ExtendWith(MockitoExtension.class)
public class FabricaDeBatalhaTorneioTest {

  @Mock
  private GeradorAleatorio gerador;
  @Mock
  private EscolhaDeAcao escolhaJogador;
  @Mock
  private RegistroBatalha registroJogador;
  @Mock
  private RegistroBatalha registroSilencioso;
  @Mock
  private RepositorioResultados repositorio;
  @Mock
  private ServicoTerreno servicoTerreno;

  private Treinador jogador;
  private Treinador cpu;
  private FabricaDeBatalhaTorneio fabrica;

  /** Cria o jogador, a CPU e a fábrica antes de cada teste. */
  @BeforeEach
  public void montar() {
    jogador = new Treinador("Jogador", SalDex.CHARSAL, new Mochila(new ArrayList<>()));
    cpu = new Treinador("CPU 1", SalDex.BULBASAL, new Mochila(new ArrayList<>()));
    fabrica = new FabricaDeBatalhaTorneio(jogador, escolhaJogador, gerador, registroJogador,
        registroSilencioso, repositorio, servicoTerreno);
  }

  /** Terreno único (Canteiro), sempre acerta, sem crítico, contra-ataque nem status. */
  private void gerarSorteiosNeutros() {
    when(servicoTerreno.terrenosDisponiveis()).thenReturn(List.of(Terreno.CANTEIRO_CENTRAL));
    when(gerador.proximoInteiro(anyInt())).thenReturn(0);
    when(gerador.proximoDecimal()).thenReturn(0.5);
  }

  @Test
  public void testBatalhaDoJogadorUsaEscolhaManualeRegistroDoJogador() {
    gerarSorteiosNeutros();
    when(escolhaJogador.escolher(any(Treinador.class), any(Pokesal.class)))
        .thenReturn(Acao.atacar());

    ResultadoBatalha resultado = fabrica.disputar(jogador, cpu);

    assertTrue(resultado.venceu("Jogador"));
    verify(escolhaJogador, org.mockito.Mockito.atLeastOnce())
        .escolher(any(Treinador.class), any(Pokesal.class));
    verify(registroJogador, org.mockito.Mockito.atLeastOnce()).inicioRodada(anyInt());
    verify(registroSilencioso, never()).inicioRodada(anyInt());
    verify(repositorio).salvar(resultado);
  }

  @Test
  public void testBatalhaEntreCpusUsaRegistroSilencioso() {
    gerarSorteiosNeutros();
    Treinador cpuA = new Treinador("CPU A", SalDex.CHARSAL, new Mochila(new ArrayList<>()));

    ResultadoBatalha resultado = fabrica.disputar(cpuA, cpu);

    assertEquals("CPU A", resultado.getVencedor());
    verify(registroSilencioso, org.mockito.Mockito.atLeastOnce()).inicioRodada(anyInt());
    verify(registroJogador, never()).inicioRodada(anyInt());
    verify(escolhaJogador, never()).escolher(any(Treinador.class), any(Pokesal.class));
    verify(repositorio).salvar(resultado);
  }

  @Test
  public void testJogadorComoSegundoParticipanteTambemUsaEscolhaManual() {
    gerarSorteiosNeutros();
    when(escolhaJogador.escolher(any(Treinador.class), any(Pokesal.class)))
        .thenReturn(Acao.atacar());

    ResultadoBatalha resultado = fabrica.disputar(cpu, jogador);

    assertTrue(resultado.venceu("Jogador"));
    verify(registroJogador, org.mockito.Mockito.atLeastOnce()).inicioRodada(anyInt());
    verify(registroSilencioso, never()).inicioRodada(anyInt());
  }
}
