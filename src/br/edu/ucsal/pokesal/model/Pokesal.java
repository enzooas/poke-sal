package br.edu.ucsal.pokesal.model;

import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.model.enums.Status;
import br.edu.ucsal.pokesal.model.enums.TipoElemental;

/** PokéSal em batalha, com atributos vindos da SalDex e HP/status mutáveis. */
public class Pokesal {

  private final String nomeP;
  private final TipoElemental tipoElemental;
  private final int hpMax;
  private int hpAtual;
  private final int atk;
  private final int def;
  private final int spd;
  private Status status;

  /** Cria o PokéSal a partir da SalDex, com HP cheio e status normal. */
  public Pokesal(SalDex saldex) {
    this.nomeP = saldex.getNome();
    this.tipoElemental = saldex.getTipoElemental();
    this.hpMax = saldex.getHpMax();
    this.hpAtual = this.hpMax;
    this.atk = saldex.getAtk();
    this.def = saldex.getDef();
    this.spd = saldex.getSpd();
    this.status = Status.NORMAL;
  }

  public TipoElemental getTipoElemental() {
    return tipoElemental;
  }

  public String getNomeP() {
    return nomeP;
  }

  public int getHpMax() {
    return hpMax;
  }

  public int getAtk() {
    return atk;
  }

  public int getHpAtual() {
    return hpAtual;
  }

  public void setHpAtual(int hpAtual) {
    this.hpAtual = hpAtual;
  }

  public int getDef() {
    return def;
  }

  public int getSpd() {
    return spd;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  /** Recupera HP sem ultrapassar o máximo e devolve quanto foi efetivamente curado. */
  public int curar(int valorCura) {
    int hpAntes = hpAtual;
    if (hpAtual + valorCura > hpMax) {
      hpAtual = hpMax;
    } else {
      hpAtual += valorCura;
    }
    return hpAtual - hpAntes;
  }

  /** Aplica dano sem deixar o HP negativo e devolve quanto foi efetivamente perdido. */
  public int receberDano(int dano) {
    int hpAntes = hpAtual;
    if (hpAtual - dano < 0) {
      hpAtual = 0;
    } else {
      hpAtual -= dano;
    }
    return hpAntes - hpAtual;
  }

  /** Indica se o PokéSal está sem HP. */
  public boolean estaDerrotado() {
    return hpAtual == 0;
  }
}
