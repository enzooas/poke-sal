package br.edu.ucsal.pokesal.model;

public enum SalDex {
    BULBASAL("BulbaSal",TipoElemental.PLANTA, 50, 40, 50, 20),
    CHARSAL("CharSal",TipoElemental.FOGO, 50, 40, 50, 20),
    SQUIRTSAL("SquirtSal",TipoElemental.AGUA, 50, 40, 50, 20),
    CHIKOSAL("ChikoSal",TipoElemental.PLANTA, 50, 40, 50, 20),
    CYNDASAL("CyndaSal",TipoElemental.FOGO, 50, 40, 50, 20),
    TOTOSAL("TotoSal", TipoElemental.AGUA, 50, 40, 50, 20);

    private final String nome;
    private final TipoElemental tipoElemental;
    private final int hpMax;
    private final int atk;
    private final int def;
    private final int spd;

    SalDex(String nome, TipoElemental tipoElemental, int hpMax, int atk, int def, int spd) {
        this.nome = nome;
        this.tipoElemental = tipoElemental;
        this.hpMax = hpMax;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
    }

    public String getNome() {
        return nome;
    }

    public int getSpd() {
        return spd;
    }

    public int getDef() {
        return def;
    }

    public int getAtk() {
        return atk;
    }

    public int getHpMax() {
        return hpMax;
    }

    public TipoElemental getTipoElemental() {
        return tipoElemental;
    }
}
