package br.edu.ucsal.pokesal.model.enums;

/** Terrenos de batalha com o bônus de dano e a cura de fim de turno de cada um. */
public enum Terreno {
  ASFALTO_QUENTE(TipoElemental.FOGO, 0.15, 0.0),
  POCA_CHUVA(TipoElemental.AGUA, 0.1, 0.0),
  CANTEIRO_CENTRAL(TipoElemental.PLANTA, 0.0, 0.05);

  private final TipoElemental tipoElemental;
  private final double bonusDano;
  private final double curaFimDeTurno;

  Terreno(TipoElemental tipoElemental, double bonusDano, double curaFimDeTurno) {
    this.tipoElemental = tipoElemental;
    this.bonusDano = bonusDano;
    this.curaFimDeTurno = curaFimDeTurno;
  }

  public TipoElemental getTipoElemental() {
    return tipoElemental;
  }

  /** Percentual extra de dano para golpes do tipo favorecido, ou 0 se o tipo não é o do terreno. */
  public double bonusDeDano(TipoElemental atacante) {
    if (atacante == tipoElemental) {
      return bonusDano;
    }
    return 0.0;
  }

  /** Percentual do HP máximo recuperado no fim do turno pelo tipo favorecido. */
  public double curaNoFimDoTurno(TipoElemental tipo) {
    if (tipo == tipoElemental) {
      return curaFimDeTurno;
    }
    return 0.0;
  }
}
