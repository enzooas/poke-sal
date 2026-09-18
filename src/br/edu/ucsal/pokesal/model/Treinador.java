package br.edu.ucsal.pokesal.model;

import br.edu.ucsal.pokesal.model.enums.SalDex;

public class Treinador {

	private final String nome;
	private final SalDex escolha;
	private final Mochila mochila;

	public Treinador(String nome, SalDex escolha, Mochila mochila) {
		this.nome = nome;
		this.escolha = escolha;
		this.mochila = mochila;
	}

	public String getNome() {
		return nome;
	}

	public SalDex getEscolha() {
		return escolha;
	}

	public Mochila getMochila() {
		return mochila;
	}

	public Pokesal criarPokesal() {
		return new Pokesal(escolha);
	}
}
