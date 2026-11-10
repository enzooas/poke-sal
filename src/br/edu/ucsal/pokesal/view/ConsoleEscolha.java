package br.edu.ucsal.pokesal.view;

import br.edu.ucsal.pokesal.model.Acao;
import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.service.EscolhaDeAcao;
import java.util.List;
import java.util.Scanner;

/** Escolha de ação feita pelo usuario, pelo console. */
public class ConsoleEscolha implements EscolhaDeAcao {

  private static final int ACAO_ATACAR = 1;
  private static final int ACAO_USAR_ITEM = 2;
  private static final int OPCAO_VOLTAR = 0;

  private final Scanner scanner;

  /** Cria a escolha lendo as opções do scanner. */
  public ConsoleEscolha(Scanner scanner) {
    this.scanner = scanner;
  }

  @Override
  public Acao escolher(Treinador treinador, Pokesal pokesal) {
    while (true) {
      System.out.println("\n--- Seu Turno ---");
      System.out.println(ACAO_ATACAR + " - Atacar");
      System.out.println(ACAO_USAR_ITEM + " - Usar Item");

      int opcao = lerOpcao(ACAO_ATACAR, ACAO_USAR_ITEM);
      if (opcao == ACAO_ATACAR) {
        return Acao.atacar();
      }

      Mochila mochila = treinador.getMochila();
      if (!mochila.podeUsarItem()) {
        System.out.println("Limite de " + Mochila.LIMITE_MAXIMO
            + " itens por batalha atingido ou mochila vazia! Escolha outra ação.");
        continue;
      }

      Itens item = escolherItem(mochila, pokesal);
      if (item != null) {
        return Acao.usarItem(item);
      }
      System.out.println("Retornando ao menu do turno...");
    }
  }

  private Itens escolherItem(Mochila mochila, Pokesal pokesal) {
    List<Itens> itens = mochila.getItens();

    System.out.println("\n===== ITENS NA MOCHILA =====");
    for (int i = 0; i < itens.size(); i++) {
      System.out.println((i + 1) + " - " + itens.get(i).getNome());
    }
    System.out.println(OPCAO_VOLTAR + " - Voltar");

    int escolha = lerOpcao(OPCAO_VOLTAR, itens.size());
    if (escolha == OPCAO_VOLTAR) {
      return null;
    }

    Itens selecionado = itens.get(escolha - 1);
    if (!selecionado.podeUsarEm(pokesal)) {
      System.out.println("Condição inválida para usar este item! Tente outra ação.");
      return null;
    }
    return selecionado;
  }

  private int lerOpcao(int min, int max) {
    while (true) {
      System.out.print("Escolha uma opção (" + min + " a " + max + "): \n");
      if (scanner.hasNextInt()) {
        int numero = scanner.nextInt();
        if (numero >= min && numero <= max) {
          return numero;
        }
      } else {
        scanner.next();
      }
      System.out.println("Opção inválida.");
    }
  }
}
