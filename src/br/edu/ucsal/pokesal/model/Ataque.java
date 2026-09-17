package br.edu.ucsal.pokesal.model;

public class Ataque {

	private final TipoElemental tipoElemental;
	private final int danoBase;
	
	public Ataque(TipoElemental tipoElemental, int danoBase) {
		this.tipoElemental = tipoElemental;
		this.danoBase = danoBase;
	}

	public TipoElemental getTipoElemental() {
		return tipoElemental;
	}

	public int getDanoBase() {
		return danoBase;
	}

}
