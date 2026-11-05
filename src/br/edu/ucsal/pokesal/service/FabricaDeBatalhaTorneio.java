package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.repository.RepositorioResultados;

public class FabricaDeBatalhaTorneio implements FabricaDeBatalha {

  private final Treinador jogador;
  private final EscolhaDeAcao escolhaJogador;
  private final GeradorAleatorio gerador;
  private final RegistroBatalha registroJogador;
  private final RegistroBatalha registroSilencioso;
  private final RepositorioResultados repositorio;

  public FabricaDeBatalhaTorneio(Treinador jogador, EscolhaDeAcao escolhaJogador, GeradorAleatorio gerador,
		RegistroBatalha registroJogador, RegistroBatalha registroSilencioso, RepositorioResultados repositorio) {
	this.jogador = jogador;
	this.escolhaJogador = escolhaJogador;
	this.gerador = gerador;
	this.registroJogador = registroJogador;
	this.registroSilencioso = registroSilencioso;
	this.repositorio = repositorio;
  }

  @Override
  public ResultadoBatalha disputar(Treinador primeiro, Treinador segundo) {
    boolean envolveJogador = primeiro == jogador || segundo == jogador;
    RegistroBatalha registro = envolveJogador ? registroJogador : registroSilencioso;
    EscolhaDeAcao escolhaPrimeiro = primeiro == jogador ? escolhaJogador : new CpuEscolha();
    EscolhaDeAcao escolhaSegundo = segundo == jogador ? escolhaJogador : new CpuEscolha();

    BatalhaService batalha = new BatalhaService(primeiro, segundo, gerador, new CalculadoraDano(),
        new Iniciativa(gerador), escolhaPrimeiro, escolhaSegundo, registro, repositorio);
    return batalha.iniciarBatalha();
  }
}
