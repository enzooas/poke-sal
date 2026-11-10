package br.edu.ucsal.pokesal.service;

/** Registro que ignora todos os eventos, usado em batalhas automáticas e em testes. */
public class RegistroSilencioso implements RegistroBatalha {

  @Override
  public void inicioBatalha(String confronto, String terreno) {
  // no-op
  }

  @Override
  public void inicioRodada(int numero) {
  // no-op
  }

  @Override
  public void ordemDoTurno(String quemComeca, int velocidadeJogador,
      int velocidadeCpu, boolean sorteio) {
  // no-op
  }

  @Override
  public void fimDeTurno(String nome, int hpAtual, int hpMax) {
  // no-op
  }

  @Override
  public void ataque(String atacante, String defensor) {
  // no-op
  }

  @Override
  public void golpeErrou(String atacante, String defensor) {
  // no-op
  }

  @Override
  public void contraAtaque(String defensor, String atacante) {
  // no-op
  }

  @Override
  public void acertoCritico() {
  // no-op
  }

  @Override
  public void dano(String defensor, int dano, int hpAtual, int hpMax) {
  // no-op
  }

  @Override
  public void efeitoStatus(String mensagem) {
  // no-op
  }

  @Override
  public void efeitoTerreno(String mensagem) {
  // no-op
  }

  @Override
  public void usoItem(String mensagem) {
  // no-op
  }

  @Override
  public void derrotado(String nome) {
  // no-op
  }

  @Override
  public void erro(String mensagem) {
  // no-op
  }
}

