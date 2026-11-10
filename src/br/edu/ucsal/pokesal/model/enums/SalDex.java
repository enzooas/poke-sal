package br.edu.ucsal.pokesal.model.enums;

import br.edu.ucsal.pokesal.model.Golpe;

/** Catálogo com os atributos base e o golpe de cada PokéSal disponível para escolha. */
public enum SalDex {
  BULBASAL("BulbaSal", TipoElemental.PLANTA, 45, 49, 49, 45, new Golpe(90)),
  CHARSAL("CharSal", TipoElemental.FOGO, 39, 52, 43, 65, new Golpe(85)),
  SQUIRTSAL("SquirtSal", TipoElemental.AGUA, 44, 48, 65, 43, new Golpe(95)),
  CHIKOSAL("ChikoSal", TipoElemental.PLANTA, 45, 49, 65, 45, new Golpe(90)),
  CYNDASAL("CyndaSal", TipoElemental.FOGO, 39, 52, 43, 65, new Golpe(85)),
  TOTOSAL("TotoSal", TipoElemental.AGUA, 50, 65, 64, 43, new Golpe(75));

  private final String nome;
  private final TipoElemental tipoElemental;
  private final int hpMax;
  private final int atk;
  private final int def;
  private final int spd;
  private final Golpe golpe;

  SalDex(String nome, TipoElemental tipoElemental, int hpMax, int atk, int def, int spd,
      Golpe golpe) {
    this.nome = nome;
    this.tipoElemental = tipoElemental;
    this.hpMax = hpMax;
    this.atk = atk;
    this.def = def;
    this.spd = spd;
    this.golpe = golpe;
  }

  public String getNome() {
    return nome;
  }

  public TipoElemental getTipoElemental() {
    return tipoElemental;
  }

  public int getHpMax() {
    return hpMax;
  }

  public int getAtk() {
    return atk;
  }

  public int getDef() {
    return def;
  }

  public int getSpd() {
    return spd;
  }

  public Golpe getGolpe() {
    return golpe;
  }
}
