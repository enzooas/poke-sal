package br.edu.ucsal.pokesal.service;

public class RegistroSilencioso implements RegistroBatalha {

  @Override
  public void inicioBatalha(String confronto, String terreno) {
  	// intencionalmente vazio
  }

  @Override
  public void inicioRodada(int numero) {
	// intencionalmente vazio
  }

  @Override
  public void ordemDoTurno(String quemComeca, int velocidadeJogador, int velocidadeCpu, boolean sorteio) {
	// intencionalmente vazio
  }

  @Override
  public void fimDeTurno(String nome, int hpAtual, int hpMax) {
	// intencionalmente vazio
  }

  @Override
  public void ataque(String atacante, String defensor) {
	// intencionalmente vazio
  }

  @Override
  public void acertoCritico() {
	// intencionalmente vazio
  }

  @Override
  public void dano(String defensor, int dano, int hpAtual, int hpMax) {
	// intencionalmente vazio
  }

  @Override
  public void efeitoStatus(String mensagem) {
	// intencionalmente vazio
  }

  @Override
  public void efeitoTerreno(String mensagem) {
	// intencionalmente vazio
  }

  @Override
  public void usoItem(String mensagem) {
	// intencionalmente vazio
  }

  @Override
  public void derrotado(String nome) {
	// intencionalmente vazio
  }

  @Override
  public void erro(String mensagem) {
	// intencionalmente vazio	
  }
}

