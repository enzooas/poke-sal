package br.edu.ucsal.pokesal.model.enums;

/** Condições de status que um PokéSal pode sofrer durante a batalha. */
public enum Status {
  NORMAL(0.0, 0.0, 0.0, 0),
  QUEIMADO(0.0625, 0.0, 0.5, 3),
  ENVENENADO(0.0625, 0.0625, 0.0, 0),
  PARALISADO(0.0, 0.0, 0.5, 3);

  private final double danoPorTurno;
  private final double incrementoDano;
  private final double redutorAtributo;
  private final int duracaoTurnos;

  Status(double danoPorTurno, double incrementoDano, double redutorAtributo, int duracaoTurnos) {
    this.danoPorTurno = danoPorTurno;
    this.incrementoDano = incrementoDano;
    this.redutorAtributo = redutorAtributo;
    this.duracaoTurnos = duracaoTurnos;
  }

  public double getDanoPorTurno() {
    return danoPorTurno;
  }

  public double getRedutorAtributo() {
    return redutorAtributo;
  }

  public int getDuracaoTurnos() {
    return duracaoTurnos;
  }

  /** Percentual do HP máximo perdido no turno, dado quantos turnos já se passaram. */
  public double danoNoTurno(int turnosPassados) {
    return danoPorTurno + incrementoDano * turnosPassados;
  }

  /** Indica se o status some sozinho após certo número de turnos. */
  public boolean expira() {
    return duracaoTurnos > 0;
  }
}
