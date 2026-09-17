package br.edu.ucsal.pokesal.model;

public enum Itens {
    POTION("Potion", "NENHUM", 20),
    SUPERPOTIO("SuperPotion", "NENHUM", 40),
    ANTIDOTO("Antidoto", "NORMAL", 0);

    private final String nome;
    private final String tipoEfeito;
    private final int valorCura;

    Itens(String nome, String tipoEfeito, int valorCura){
        this.nome = nome;
        this.tipoEfeito = tipoEfeito;
        this.valorCura = valorCura;
    }

    public String getNome() {
        return nome;
    }

    public String getTipoEfeito() {
        return tipoEfeito;
    }

    public int getValorCura() {
        return valorCura;
    }
}
