package br.edu.ucsal.pokesal.service;

public interface RegistroBatalha {

  void inicioBatalha(String descricao);

  void ataque(String atacante, String defensor);

  void dano(String defensor, int dano, int hpAtual, int hpMax);

  void efeitoStatus(String mensagem);

  void efeitoTerreno(String mensagem);

  void usoItem(String mensagem);

  void acertoCritico();

  void derrotado(String nome);
}
