package br.edu.ucsal.pokesal.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.service.BatalhaService;

public class Main {
	
	private static int totalJogos = 0;
	private static int vitorias = 0;
	private static int derrotas = 0;

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Random random = new Random();
		int opcao;

		System.out.println("--- Bem-vindo ao PokéSal ---");

		SalDex escolhaJogador = escolherPokesal(scanner);
		System.out.println("\nVocê escolheu " + escolhaJogador.getNome() + "!");

		Mochila mochilaJogador = new Mochila(new ArrayList<>(List.of(Itens.POTION, Itens.SUPERPOTION, Itens.ANTIDOTE)));
		Treinador jogador = new Treinador("Você", escolhaJogador, mochilaJogador);

		do {
			System.out.println("\n===== MENU =====");
			System.out.println("1 - Iniciar Batalha");
			System.out.println("2 - Ver Mochila");
			System.out.println("3 - Histórico de Batalhas");
			System.out.println("4 - Sair");

			opcao = lerNumero(scanner, 1, 4);

			switch (opcao) {
				case 1 -> iniciarBatalha(jogador, random, scanner);
				case 2 -> verMochila(jogador);
				case 3 -> exibirHistorico();
				case 4 -> {
					System.out.println("\n===== RESUMO FINAL DO HISTÓRICO =====");
					exibirHistorico();
					System.out.println("Até a próxima!");
				}
			}
		} while (opcao != 4);

		scanner.close();
	}

	private static void iniciarBatalha(Treinador jogador, Random random, Scanner scanner) {
		Treinador cpu = new Treinador("CPU", sortearEscolhaCpu(random), new Mochila(new ArrayList<>()));

		BatalhaService batalha = new BatalhaService(jogador, cpu, random, scanner);
		Treinador vencedor = batalha.iniciarBatalha();

		totalJogos++;
		if (vencedor == jogador) {
			vitorias++;
			System.out.println("\nVocê venceu a batalha!");
			
			Itens recompensa = sortearItemRecompensa(random);
			jogador.getMochila().getItens().add(recompensa);
			System.out.println("Recompensa obtida: " + recompensa.getNome() + " adicionado à mochila!");
		} else {
			derrotas++;
			System.out.println("\nA CPU venceu a batalha!");
		}
	}
	
	private static void exibirHistorico() {
		System.out.println("\n===== HISTÓRICO DE BATALHAS =====");
		if (totalJogos == 0) {
			System.out.println("Nenhuma batalha registrada.");
			return;
		}
		System.out.println("Total de jogos: " + totalJogos);
		System.out.println("Vitórias: " + vitorias);
		System.out.println("Derrotas: " + derrotas);
	}

	private static void verMochila(Treinador jogador) {
		List<Itens> itens = jogador.getMochila().getItens();

		System.out.println("\n===== MOCHILA =====");
		if (itens.isEmpty()) {
			System.out.println("\nMochila vazia.");
			return;
		}

		for (Itens item : itens) {
			System.out.println("- " + item.getNome());
		}
	}
	
	private static Itens sortearItemRecompensa(Random random) {
		Itens[] todosItens = Itens.values();
		return todosItens[random.nextInt(todosItens.length)];
	}    

	private static SalDex sortearEscolhaCpu(Random random) {
		SalDex[] opcoes = SalDex.values();
		int indice = random.nextInt(opcoes.length);
		return opcoes[indice];
	}

	private static SalDex escolherPokesal(Scanner scanner) {
		SalDex[] opcoes = SalDex.values();

		System.out.println("\nEscolha seu PokéSal:");
		for (int i = 0; i < opcoes.length; i++) {
			System.out.println((i + 1) + " - " + opcoes[i].getNome() + " (" + opcoes[i].getTipoElemental() + ")");
		}

		int escolha = lerNumero(scanner, 1, opcoes.length);
		return opcoes[escolha - 1];
	}

	private static int lerNumero(Scanner scanner, int min, int max) {
		while (true) {
			System.out.print("Digite o número (" + min + " a " + max + "): ");
			if (scanner.hasNextInt()) {
				int numero = scanner.nextInt();
				if (numero >= min && numero <= max) {
					return numero;
				}
				System.out.println("Opção inválida.");
			} else {
				System.out.println("Digite apenas números.");
				scanner.next();
			}
		}
	}
}