package br.edu.ucsal.pokesal.model.enums;

public enum Itens {
    POTION("Potion", TipoEfeito.NENHUM, 20),
    SUPERPOTION("SuperPotion", TipoEfeito.NENHUM, 50),
    ANTIDOTE("Antidote", TipoEfeito.NORMAL, 0);

    private final String nome;
    private final TipoEfeito tipoEfeito;
    private final int valorCura;

    Itens(String nome, TipoEfeito tipoEfeito, int valorCura) {
        this.nome = nome;
        this.tipoEfeito = tipoEfeito;
        this.valorCura = valorCura;
    }

    public String getNome() {
        return nome;
    }

    public TipoEfeito getTipoEfeito() {
        return tipoEfeito;
    }

    public int getValorCura() {
        return valorCura;
    }
}
