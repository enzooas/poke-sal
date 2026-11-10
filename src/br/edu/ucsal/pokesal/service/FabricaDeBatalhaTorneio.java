package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.repository.RepositorioResultados;

/**
 * Disputa as batalhas do torneio: o jogador usa a escolha manual e as demais batalhas
 * são automáticas e silenciosas. */
public class FabricaDeBatalhaTorneio implements FabricaDeBatalha {

  private final Treinador jogador;
  private final EscolhaDeAcao escolhaJogador;
  private final GeradorAleatorio gerador;
  private final RegistroBatalha registroJogador;
  private final RegistroBatalha registroSilencioso;
  private final RepositorioResultados repositorio;
  private final ServicoTerreno terreno;

  /** Cria a fábrica com os colaboradores usados em cada batalha. */
  public FabricaDeBatalhaTorneio(Treinador jogador, EscolhaDeAcao escolhaJogador,
        GeradorAleatorio gerador, RegistroBatalha registroJogador,
        RegistroBatalha registroSilencioso, RepositorioResultados repositorio,
        ServicoTerreno terreno) {
    this.jogador = jogador;
    this.escolhaJogador = escolhaJogador;
    this.gerador = gerador;
    this.registroJogador = registroJogador;
    this.registroSilencioso = registroSilencioso;
    this.repositorio = repositorio;
    this.terreno = terreno;
  }

  @Override
  public ResultadoBatalha disputar(Treinador primeiro, Treinador segundo) {
    boolean envolveJogador = primeiro == jogador || segundo == jogador;
    RegistroBatalha registro = envolveJogador ? registroJogador : registroSilencioso;
    EscolhaDeAcao escolhaPrimeiro = primeiro == jogador ? escolhaJogador : new CpuEscolha();
    EscolhaDeAcao escolhaSegundo = segundo == jogador ? escolhaJogador : new CpuEscolha();

    BatalhaService batalha = new BatalhaService(primeiro, segundo, gerador, new CalculadoraDano(),
        new Iniciativa(gerador), escolhaPrimeiro, escolhaSegundo, registro, repositorio, terreno);
    return batalha.iniciarBatalha();
  }
}
