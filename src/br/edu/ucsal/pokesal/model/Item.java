package br.edu.ucsal.pokesal.model;

public class Item {
	
	private String nome;
	private String tipoEfeito;
	private int valorCura;
	
	public Item(String nome, String tipoEfeito, int valorCura) {
		this.nome = nome;
		this.tipoEfeito = tipoEfeito;
		this.valorCura = valorCura;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getTipoEfeito() {
		return tipoEfeito;
	}

	public void setTipoEfeito(String tipoEfeito) {
		this.tipoEfeito = tipoEfeito;
	}

	public int getValorCura() {
		return valorCura;
	}

	public void setValorCura(int valorCura) {
		this.valorCura = valorCura;
	}
	

}
