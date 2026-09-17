package br.edu.ucsal.pokesal.model;

import br.edu.ucsal.pokesal.model.Itens;

public class Pokesal {

	private final String nomePK;
	private final TipoElemental tipoElemental;
	private final int hpMax;
	private int hpAtual;
	private final int atk;
	private final int def;
	private final int spd;
	private String status;
	
	public Pokesal(SalDex pokesal) {
        this.nomePK = pokesal.getNome();
        this.tipoElemental = pokesal.getTipoElemental();
        this.hpMax = pokesal.getHpMax();
        this.hpAtual = this.hpMax;
        this.atk = pokesal.getAtk();
        this.def = pokesal.getDef();
        this.spd = pokesal.getSpd();
		this.status = "NORMAL";
    }

	public TipoElemental getTipoElemental() {
		return tipoElemental;
	}

	public String getNomePK() {
		return nomePK;
	}

	public int getHpMax() {
		return hpMax;
	}

	public int getAtk() {
		return atk;
	}

	public int getHpAtual() {
		return hpAtual;
	}

	public void setHpAtual(int hpAtual) {
		this.hpAtual = hpAtual;
	}

	public int getDef() {
		return def;
	}

	public int getSpd() {
		return spd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int curar(int valorCura) {
		int hpAntes = getHpAtual();
		if (getHpAtual() + valorCura > getHpMax()) {
			setHpAtual(getHpMax());
		} else {
			setHpAtual(getHpAtual()+valorCura);
		}
		return getHpAtual() - hpAntes;
	}

}
