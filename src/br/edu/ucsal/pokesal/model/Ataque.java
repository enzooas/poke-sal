package br.edu.ucsal.pokesal.model;

public class Ataque {

	private String nome;
	private String tipo;
	private int danoBase;
	
	public Ataque(String nome, String tipo, int danoBase) {
		this.nome = nome;
		this.tipo = tipo;
		this.danoBase = danoBase;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getDanoBase() {
		return danoBase;
	}

	public void setDanoBase(int danoBase) {
		this.danoBase = danoBase;
	}
	
}
