package br.edu.ucsal.pokesal.model.enums;

public enum SalDex {
    BULBASAL("BulbaSal", TipoElemental.PLANTA, 45, 49, 49, 45),
    CHARSAL("CharSal", TipoElemental.FOGO, 39, 52, 43, 65),
    SQUIRTSAL("SquirtSal", TipoElemental.AGUA, 44, 48, 65, 43),
    CHIKOSAL("ChikoSal", TipoElemental.PLANTA, 45, 49, 65, 45),
    CYNDASAL("CyndaSal", TipoElemental.FOGO, 39, 52, 43, 65),
    TOTOSAL("TotoSal", TipoElemental.AGUA, 50, 65, 64, 43);

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
    
}
