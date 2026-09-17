package br.edu.ucsal.pokesal.model;

import java.util.List;

import br.edu.ucsal.pokesal.model.enums.Itens;

public class Mochila {
	
	private List<Itens> itens;
	
	public Mochila(List<Itens> itens) {
		this.itens = itens;
	}

	public List<Itens> getItens() {
		return itens;
	}
	
}
