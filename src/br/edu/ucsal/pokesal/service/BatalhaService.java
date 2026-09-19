package br.edu.ucsal.pokesal.service;

import java.util.List;
import java.util.Random;
import java.util.Scanner;

import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.Status;

public class BatalhaService {

	// TODO: valor temporário para teste, voltar para 0.0625 (6,25%)
	private static final double CHANCE_CRITICO = 0.5;
	private static final double MULTIPLICADOR_CRITICO = 1.5;

	private final Treinador treinadorJogador;
	private final Treinador treinadorCpu;
	private final Pokesal pokesalJogador;
	private final Pokesal pokesalCpu;
	private final Random random;
	private final Scanner scanner;

	public BatalhaService(Treinador treinadorJogador, Treinador treinadorCpu, Random random, Scanner scanner) {
		this.treinadorJogador = treinadorJogador;
		this.treinadorCpu = treinadorCpu;
		this.pokesalJogador = treinadorJogador.criarPokesal();
		this.pokesalCpu = treinadorCpu.criarPokesal();
		this.random = random;
		this.scanner = scanner;
	}

	public int calcularDano(Pokesal atacante, Pokesal defensor, boolean critico) {
		double dano = atacante.getAtk() - (defensor.getDef());

		if (critico) {
			dano *= MULTIPLICADOR_CRITICO;
		}

		int danoFinal = (int) dano;
		if (danoFinal < 1) {
			danoFinal = 1;
		}
		return danoFinal;
	}

	public boolean sortearCritico() {
		return random.nextDouble() < CHANCE_CRITICO;
	}

	public void atacar(Pokesal atacante, Pokesal defensor) {
		boolean critico = sortearCritico();

		if (critico) {
			System.out.println("Acerto crítico!");
		}

		int dano = calcularDano(atacante, defensor, critico);
		int danoSofrido = defensor.receberDano(dano);
		System.out.println(nomeComDono(atacante) + " atacou e causou " + danoSofrido + " de dano em "
				+ nomeComDono(defensor) + ". HP: " + defensor.getHpAtual() + "/" + defensor.getHpMax());

		if (defensor.estaDerrotado()) {
			System.out.println(nomeComDono(defensor) + " foi derrotado!");
		}
	}

	public void executarTurnoJogador() {
		System.out.println("\n--- Seu Turno ---");
		System.out.println("1 - Atacar");
		System.out.println("2 - Usar Item");

		int acao = lerOpcao(1, 2);

		if (acao == 1) {
			System.out.println(nomeComDono(pokesalJogador) + " escolheu atacar!");
			atacar(pokesalJogador, pokesalCpu);
		} else if (!treinadorJogador.getMochila().podeUsarItem()) {
			System.out.println("Mochila vazia ou limite de 2 itens por batalha atingido!"
					+ "Você perdeu a chance e atacou mesmo assim.");
			atacar(pokesalJogador, pokesalCpu);
			return;
		}

		Itens itemEscolhido = escolherItemNaMochila();
		if (itemEscolhido != null) {
			aplicarItem(treinadorJogador, pokesalJogador, itemEscolhido);
		} else {
			atacar(pokesalJogador, pokesalCpu);
		}
	}

	public void executarTurnoCpu() {
		System.out.println("\n--- Turno da CPU ---");
		System.out.println(nomeComDono(pokesalCpu) + " escolheu atacar!");
		atacar(pokesalCpu, pokesalJogador);
	}

	private Itens escolherItemNaMochila() {
		List<Itens> itens = treinadorJogador.getMochila().getItens();
		
		System.out.println("\n===== ITENS NA MOCHILA =====");
		for (int i = 0; i < itens.size(); i++) {
			System.out.println((i + 1) + " - " + itens.get(i).getNome());
		}
		System.out.println("0 - Voltar/Atacar");

		int escolha = lerOpcao(0, itens.size());
		if (escolha == 0)
			return null;

		Itens itemSelecionado = itens.get(escolha - 1);

		if (!treinadorJogador.getMochila().validarUsoItem(itemSelecionado, pokesalJogador)) {
			System.out.println("Condição inválida para usar este item! Tente outra ação.");
			return null;
		}

		return itemSelecionado;
	}

	private void aplicarItem(Treinador treinador, Pokesal pokesal, Itens item) {
		treinador.getMochila().registrarUso(item);
		System.out.println(treinador.getNome() + " usou " + item.getNome() + "!");

		if (item == Itens.POTION || item == Itens.SUPERPOTION) {
			int curaEfetiva = pokesal.curar(item.getValorCura());
			System.out.println(pokesal.getNomePK() + " recuperou " + curaEfetiva + " de HP! HP atual: "
					+ pokesal.getHpAtual() + "/" + pokesal.getHpMax());
		} else if (item == Itens.ANTIDOTE) {
			pokesal.setStatus(Status.NORMAL);
			System.out.println(pokesal.getNomePK() + " foi curado do envenenamento!");
		}
	}

	public Treinador iniciarBatalha() {
		System.out.println("\n" + nomeComDono(pokesalJogador) + " contra " + nomeComDono(pokesalCpu) + "!");
		int rodada = 1;

		while (!pokesalJogador.estaDerrotado() && !pokesalCpu.estaDerrotado()) {
			System.out.println("\n=== Rodada " + rodada + " ===");

			boolean jogadorComeca = pokesalJogador.getSpd() >= pokesalCpu.getSpd();
			if (pokesalJogador.getSpd() == pokesalCpu.getSpd()) {
				jogadorComeca = random.nextBoolean();
			}

			if (jogadorComeca) {
				executarTurnoJogador();
				if (pokesalCpu.estaDerrotado()) {
					break;
				}	
				executarTurnoCpu();
			} else {
				executarTurnoCpu();
				if (pokesalJogador.estaDerrotado()) {
					break;
				}
				executarTurnoJogador();
			}

			rodada++;
		}

		if (pokesalJogador.estaDerrotado()) {
			return treinadorCpu;
		}
		return treinadorJogador;
	}

	private String nomeComDono(Pokesal pokesal) {
		Treinador dono;
        if (pokesal == pokesalJogador) {
            dono = treinadorJogador;
        } else {
            dono = treinadorCpu;
        }
        return pokesal.getNomePK() + " (" + dono.getNome() + ")";
	}

	private int lerOpcao(int min, int max) {
		while (true) {
			System.out.print("Escolha uma opção (" + min + " a " + max + "): ");
			if (scanner.hasNextInt()) {
				int num = scanner.nextInt();
				if (num >= min && num <= max) {
					return num;
				}
			} else {
				scanner.next();
			}
			System.out.println("Opção inválida.");
		}
	}
}