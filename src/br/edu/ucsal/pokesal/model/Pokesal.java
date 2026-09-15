package br.edu.ucsal.pokesal.model;

public class Pokesal {

	private String nome;
	private String tipoElemental;
	private int hpAtual;
	private int hpMax;
	private Ataque atk;
	private int def;
	private int spd;
	private String status;
	
	public Pokesal(String nome, String tipoElemental, int hpMax, Ataque atk, int def, int spd) {
        this.nome = nome;
        this.tipoElemental = tipoElemental;
        this.hpMax = hpMax;
        this.hpAtual = hpMax;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.status = "NORMAL";
    }

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoElemental() {
		return tipoElemental;
	}

	public void setTipoElemental(String tipoElemental) {
		this.tipoElemental = tipoElemental;
	}

	public int getHpAtual() {
		return hpAtual;
	}

	public void setHpAtual(int hpAtual) {
		this.hpAtual = hpAtual;
	}

	public int getHpMax() {
		return hpMax;
	}

	public void setHpMax(int hpMax) {
		this.hpMax = hpMax;
	}

	public Ataque getAtk() {
		return atk;
	}

	public void setAtk(Ataque atk) {
		this.atk = atk;
	}

	public int getDef() {
		return def;
	}

	public void setDef(int def) {
		this.def = def;
	}

	public int getSpd() {
		return spd;
	}

	public void setSpd(int spd) {
		this.spd = spd;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
