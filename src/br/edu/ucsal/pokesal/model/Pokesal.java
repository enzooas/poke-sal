package br.edu.ucsal.pokesal.model;

import br.edu.ucsal.pokesal.model.enums.*;

public class Pokesal {

	private final String nomePK;
	private final TipoElemental tipoElemental;
	private final int hpMax;
	private int hpAtual;
	private final int atk;
	private final int def;
	private final int spd;
	private Status status;
	
	public Pokesal(SalDex saldex) {
        this.nomePK = saldex.getNome();
        this.tipoElemental = saldex.getTipoElemental();
        this.hpMax = saldex.getHpMax();
        this.hpAtual = this.hpMax;
        this.atk = saldex.getAtk();
        this.def = saldex.getDef();
        this.spd = saldex.getSpd();
		this.status = Status.NORMAL;
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

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int curar(int valorCura) {
		int hpAntes = hpAtual;
		if (hpAtual + valorCura > hpMax) {
			hpAtual = hpMax;
		} else {
			hpAtual += valorCura;
		}
		return hpAtual - hpAntes;
	}

}
