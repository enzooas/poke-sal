package br.edu.ucsal.pokesal.view;

import br.edu.ucsal.pokesal.model.Historico;
import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.SalDex;
import br.edu.ucsal.pokesal.repository.RepositorioResultados;
import br.edu.ucsal.pokesal.repository.RepositorioResultadosMemoria;
import br.edu.ucsal.pokesal.service.BatalhaService;
import br.edu.ucsal.pokesal.service.CalculadoraDano;
import br.edu.ucsal.pokesal.service.CpuEscolha;
import br.edu.ucsal.pokesal.service.EscolhaDeAcao;
import br.edu.ucsal.pokesal.service.GeradorAleatorio;
import br.edu.ucsal.pokesal.service.GeradorAleatorioPadrao;
import br.edu.ucsal.pokesal.service.Iniciativa;
import br.edu.ucsal.pokesal.service.RecompensaService;
import br.edu.ucsal.pokesal.service.RegistroBatalha;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/** Ponto de entrada do jogo: exibe o menu principal e acumula o histórico de batalhas. */
public class Main {

  private static final int OPCAO_BATALHA_NORMAL = 1;
  private static final int OPCAO_TORNEIO = 2;
  private static final int OPCAO_MOCHILA = 3;
  private static final int OPCAO_HISTORICO = 4;
  private static final int OPCAO_SAIR = 5;

  /** Inicia o jogo, monta o treinador do jogador e mantém o laço do menu principal. */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    GeradorAleatorio gerador = new GeradorAleatorioPadrao(new Random());
    Historico historico = new Historico();
    RecompensaService recompensas = new RecompensaService(gerador);
    RepositorioResultados repo = new RepositorioResultadosMemoria();
    int opcao;

    System.out.println("--- Bem-vindo ao PokéSal ---");

    SalDex escolhaJogador = escolherPokesal(scanner);
    System.out.println("\nVocê escolheu " + escolhaJogador.getNome() + "!");

    Mochila mochilaJogador =
        new Mochila(new ArrayList<>(List.of(Itens.POTION, Itens.SUPERPOTION, Itens.ANTIDOTE)));
    Treinador jogador = new Treinador("Você", escolhaJogador, mochilaJogador);

    do {
      System.out.println("\n===== MENU =====");
      System.out.println("1 - Jogar modo normal");
      System.out.println("2 - Jogar modo torneio");
      System.out.println("3 - Ver mochila");
      System.out.println("4 - Histórico de batalhas");
      System.out.println("5 - Sair");

      opcao = lerNumero(scanner, OPCAO_BATALHA_NORMAL, OPCAO_SAIR);

      switch (opcao) {
        case OPCAO_BATALHA_NORMAL:
          iniciarBatalha(jogador, gerador, scanner, historico, recompensas, repo);
          break;
        case OPCAO_MOCHILA:
          verMochila(jogador);
          break;
        case OPCAO_HISTORICO:
          exibirHistorico(historico);
          break;
        case OPCAO_SAIR:
          System.out.println("\n===== RESUMO FINAL DO HISTÓRICO =====");
          exibirHistorico(historico);
          System.out.println("Até a próxima!");
          break;
        default:
          break;
      }
    } while (opcao != OPCAO_SAIR);

    scanner.close();
  }

  private static void iniciarBatalha(Treinador jogador, GeradorAleatorio gerador, Scanner scanner,
        Historico historico, RecompensaService recompensas, RepositorioResultados repo) {
    Treinador cpu =
        new Treinador("CPU", sortearEscolhaCpu(gerador), new Mochila(new ArrayList<>()));

    CalculadoraDano calculadora = new CalculadoraDano();
    Iniciativa iniciativa = new Iniciativa(gerador);
    EscolhaDeAcao escolhaJogador = new ConsoleEscolha(scanner);
    EscolhaDeAcao escolhaCpu = new CpuEscolha();
    RegistroBatalha registro = new ConsoleRegistro();

    BatalhaService batalha = new BatalhaService(jogador, cpu, gerador,
          calculadora, iniciativa, escolhaJogador, escolhaCpu, registro, repo);

    ResultadoBatalha resultado = batalha.iniciarBatalha();

    if (resultado.venceu(jogador.getNome())) {
      historico.registrarVitoria();
      System.out.println("\nVocê venceu a batalha!");
      Itens recompensa = recompensas.concederRecompensa(jogador);
      System.out.println("Recompensa obtida: " + recompensa.getNome()
             + " adicionado à mochila!");
    } else {
      historico.registrarDerrota();
      System.out.println("\nA CPU venceu a batalha!");
    }
  }

  private static void exibirHistorico(Historico historico) {
    System.out.println("\n===== HISTÓRICO DE BATALHAS =====");
    if (historico.estaVazio()) {
      System.out.println("Nenhuma batalha registrada.");
      return;
    }
    System.out.println("Total de jogos: " + historico.getTotalJogos());
    System.out.println("Vitórias: " + historico.getVitorias());
    System.out.println("Derrotas: " + historico.getDerrotas());
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

  private static SalDex sortearEscolhaCpu(GeradorAleatorio gerador) {
    SalDex[] opcoes = SalDex.values();
    int indice = gerador.proximoInteiro(opcoes.length);
    return opcoes[indice];
  }

  private static SalDex escolherPokesal(Scanner scanner) {
    SalDex[] opcoes = SalDex.values();

    System.out.println("\nEscolha seu PokéSal:");
    for (int i = 0; i < opcoes.length; i++) {
      System.out.println(
          (i + 1) + " - " + opcoes[i].getNome() + " (" + opcoes[i].getTipoElemental() + ")");
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
