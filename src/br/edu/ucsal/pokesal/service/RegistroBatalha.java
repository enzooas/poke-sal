package br.edu.ucsal.pokesal.service;

/** Recebe os eventos da batalha. */
public interface RegistroBatalha {

  /** Anuncia o confronto e o terreno sorteado. */
  void inicioBatalha(String confronto, String terreno);

  /** Anuncia o início de uma rodada. */
  void inicioRodada(int numero);

  /** Anuncia quem age primeiro no turno e o motivo (velocidade ou sorteio). */
  void ordemDoTurno(String quemComeca, int velocidadeJogador, int velocidadeCpu, boolean sorteio);

  /** Registra o HP de um PokéSal ao final do turno, depois de status e terreno. */
  void fimDeTurno(String nome, int hpAtual, int hpMax);

  /** Registra que um PokéSal atacou outro. */
  void ataque(String atacante, String defensor);

  /** Registra que o golpe errou o alvo pela precisão e não causou dano. */
  void golpeErrou(String atacante, String defensor);

  /** Registra que o defensor bloqueou o golpe e contra-atacou. */
  void contraAtaque(String defensor, String atacante);

  /** Registra que o golpe foi um acerto crítico. */
  void acertoCritico();

  /** Registra o dano causado e o HP restante do defensor. */
  void dano(String defensor, int dano, int hpAtual, int hpMax);

  /** Registra um efeito de status (aplicação, dano, recuperação). */
  void efeitoStatus(String mensagem);

  /** Registra um efeito de terreno. */
  void efeitoTerreno(String mensagem);

  /** Registra o uso de um item. */
  void usoItem(String mensagem);

  /** Registra que um PokéSal foi derrotado. */
  void derrotado(String nome);

  /** Registra uma falha ocorrida fora da batalha, como no salvamento do resultado. */
  void erro(String mensagem);
}