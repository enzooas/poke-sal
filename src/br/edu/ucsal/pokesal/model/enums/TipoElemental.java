package br.edu.ucsal.pokesal.model.enums;

/** Tipos elementais que definem as vantagens e desvantagens de um PokéSal. */
public enum TipoElemental {
  AGUA,
  FOGO,
  PLANTA;

  public static final double SUPER_EFETIVO = 2.0;
  public static final double POUCO_EFETIVO = 0.5;
  public static final double NEUTRO = 1.0;

  /** Devolve o multiplicador de dano deste tipo (atacante) contra o tipo do defensor. */
  public double multiplicadorContra(TipoElemental defensor) {
    if (temVantagemSobre(defensor)) {
      return SUPER_EFETIVO;
    }
    if (defensor.temVantagemSobre(this)) {
      return POUCO_EFETIVO;
    }
    return NEUTRO;
  }

  private boolean temVantagemSobre(TipoElemental outro) {
    return this == FOGO && outro == PLANTA
        || this == AGUA && outro == FOGO
        || this == PLANTA && outro == AGUA;
  }
}
