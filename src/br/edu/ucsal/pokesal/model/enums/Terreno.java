package br.edu.ucsal.pokesal.model.enums;

public enum Terreno {
    ASFALTO_QUENTE(TipoElemental.FOGO, 0.15),
    POCA_CHUVA(TipoElemental.AGUA, 0.1),
    CANTEIRO_CENTRAL(TipoElemental.PLANTA, 0.05);

    private final TipoElemental tipoElemental;
    private final double porcentagem;

    Terreno(TipoElemental tipoElemental, double porcentagem) {
        this.tipoElemental = tipoElemental;
        this.porcentagem = porcentagem;
    }

    public TipoElemental getTipoElemental() {
        return tipoElemental;
    }

    public double getPorcentagem() {
        return porcentagem;
    }
}
