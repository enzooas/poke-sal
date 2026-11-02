package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Mochila;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.Status;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import br.edu.ucsal.pokesal.model.enums.TipoElemental;
import java.util.List;
import java.util.Scanner;

/** Conduz uma batalha entre o jogador e a CPU, turno a turno, até que haja um vencedor. */
public class BatalhaService {

  private static final double CHANCE_CRITICO = 0.0625;
  private static final double CHANCE_STATUS = 0.15;
  private static final int DANO_MINIMO = 1;
  private static final int ACAO_ATACAR = 1;
  private static final int ACAO_USAR_ITEM = 2;

  private final Treinador treinadorJogador;
  private final Treinador treinadorCpu;
  private final Pokesal pokesalJogador;
  private final Pokesal pokesalCpu;
  private final GeradorAleatorio gerador;
  private final Scanner scanner;
  private final CalculadoraDano calculadora;
  private final Iniciativa iniciativa;
  private final Terreno terrenoAtual;

  /** Cria a batalha, instancia os PokéSal dos dois treinadores e sorteia o terreno. */
  public BatalhaService(
      Treinador treinadorJogador, Treinador treinadorCpu, GeradorAleatorio gerador, Scanner scanner,
      CalculadoraDano calculadora, Iniciativa iniciativa) {
    this.treinadorJogador = treinadorJogador;
    this.treinadorCpu = treinadorCpu;
    this.pokesalJogador = treinadorJogador.criarPokesal();
    this.pokesalCpu = treinadorCpu.criarPokesal();
    this.gerador = gerador;
    this.scanner = scanner;
    this.calculadora = calculadora;
    this.iniciativa = iniciativa;
    Terreno[] terrenos = Terreno.values();
    this.terrenoAtual = terrenos[gerador.proximoInteiro(terrenos.length)];
  }

  /** Sorteia se o golpe será um acerto crítico. */
  public boolean sortearCritico() {
    return gerador.proximoDecimal() < CHANCE_CRITICO;
  }

  /** Executa um ataque, aplica o dano e sorteia a condição de status do defensor. */
  public void atacar(Pokesal atacante, Pokesal defensor) {
    boolean critico = sortearCritico();

    if (critico) {
      System.out.println("Acerto crítico!");
    }

    int dano = calculadora.calcular(atacante, defensor, terrenoAtual, critico);
    int danoSofrido = defensor.receberDano(dano);
    System.out.println(nomeComDono(atacante) + " atacou e causou " + danoSofrido + " de dano em "
        + nomeComDono(defensor) + ". HP: " + defensor.getHpAtual() + "/" + defensor.getHpMax());

    if (!defensor.estaDerrotado() && defensor.getStatus() == Status.NORMAL
        && gerador.proximoDecimal() < CHANCE_STATUS) {
      if (atacante.getTipoElemental() == TipoElemental.FOGO) {
        defensor.setStatus(Status.QUEIMADO);
        System.out.println(nomeComDono(defensor) + " foi queimado!");
      } else if (atacante.getTipoElemental() == TipoElemental.PLANTA) {
        defensor.setStatus(Status.ENVENENADO);
        System.out.println(nomeComDono(defensor) + " foi envenenado!");
      } else if (atacante.getTipoElemental() == TipoElemental.AGUA) {
        defensor.setStatus(Status.PARALISADO);
        System.out.println(nomeComDono(defensor) + " foi paralisado!");
      }
    }

    if (defensor.estaDerrotado()) {
      System.out.println(nomeComDono(defensor) + " foi derrotado!");
    }
  }

  /** Lê a ação do jogador e repete o menu até que uma ação válida seja concluída. */
  public void executarTurnoJogador() {
    boolean acaoConcluida = false;

    while (!acaoConcluida) {
      System.out.println("\n--- Seu Turno ---");
      System.out.println("1 - Atacar");
      System.out.println("2 - Usar Item");

      int acao = lerOpcao(ACAO_ATACAR, ACAO_USAR_ITEM);

      if (acao == ACAO_ATACAR) {
        System.out.println(nomeComDono(pokesalJogador) + " escolheu atacar!");
        atacar(pokesalJogador, pokesalCpu);
        acaoConcluida = true;
      } else {
        if (!treinadorJogador.getMochila().podeUsarItem()) {
          System.out.println(
              "Limite de " + Mochila.LIMITE_MAXIMO + " itens por batalha atingido ou mochila vazia!"
                  + "Escolha outra ação.");
          continue;
        }

        Itens itemEscolhido = escolherItemNaMochila();
        if (itemEscolhido != null) {
          aplicarItem(treinadorJogador, pokesalJogador, itemEscolhido);
          acaoConcluida = true;
        } else {
          System.out.println("Retornando ao menu do turno...");
        }
      }
    }
  }

  /** Executa o turno da CPU, que sempre ataca. */
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
    if (escolha == 0) {
      return null;
    }

    Itens itemSelecionado = itens.get(escolha - 1);

    if (!itemSelecionado.podeUsarEm(pokesalJogador)) {
      System.out.println("Condição inválida para usar este item! Tente outra ação.");
      return null;
    }

    return itemSelecionado;
  }

  private void aplicarItem(Treinador treinador, Pokesal pokesal, Itens item) {
    treinador.getMochila().registrarUso(item);
    System.out.println(treinador.getNome() + " usou " + item.getNome() + "!");
    System.out.println(item.aplicarEm(pokesal));
  }

  private void processarFimDeTurno(Pokesal pokesal) {
    if (pokesal.estaDerrotado()) {
      return;
    }

    Status statusAtual = pokesal.getStatus();
    if (statusAtual.danoNoTurno(pokesal.getTurnosComStatus()) > 0.0) {
      int danoStatus = (int) (pokesal.getHpMax()
          * statusAtual.danoNoTurno(pokesal.getTurnosComStatus()));

      if (danoStatus < DANO_MINIMO) {
        danoStatus = DANO_MINIMO;
      }

      pokesal.receberDano(danoStatus);
      System.out.println(nomeComDono(pokesal) + " sofreu " + danoStatus
          + " de dano devido ao status " + statusAtual + "!");
    }

    if (statusAtual != Status.NORMAL) {
      pokesal.avancarTurnoDeStatus();
      if (statusAtual.expira() && pokesal.getTurnosComStatus() >= statusAtual.getDuracaoTurnos()) {
        pokesal.setStatus(Status.NORMAL);
        System.out.println(nomeComDono(pokesal) + " se recuperou de " + statusAtual + "!");
      }
    }

    if (pokesal.estaDerrotado()) {
      return;
    }

    double percentualCura = terrenoAtual.curaNoFimDoTurno(pokesal.getTipoElemental());
    if (percentualCura > 0.0) {
      int curado = pokesal.curar((int) (pokesal.getHpMax() * percentualCura));
      if (curado > 0) {
        System.out.println(
            nomeComDono(pokesal) + " recuperou " + curado + " de HP pelo terreno!");
      }
    }
  }

  /** Roda as rodadas até alguém ser derrotado e devolve o treinador vencedor. */
  public Treinador iniciarBatalha() {
    treinadorJogador.getMochila().resetarUsoBatalha();
    treinadorCpu.getMochila().resetarUsoBatalha();

    System.out.println(
        "\n" + nomeComDono(pokesalJogador) + " contra " + nomeComDono(pokesalCpu) + "!");
    System.out.println("Terreno da batalha: " + terrenoAtual);

    int rodada = 1;

    while (!pokesalJogador.estaDerrotado() && !pokesalCpu.estaDerrotado()) {
      System.out.println("\n=== Rodada " + rodada + " ===");

      boolean jogadorComeca = iniciativa.primeiroAtacaAntes(pokesalJogador, pokesalCpu);

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

      processarFimDeTurno(pokesalJogador);
      if (pokesalCpu.estaDerrotado() || pokesalJogador.estaDerrotado()) {
        break;
      }

      processarFimDeTurno(pokesalCpu);
      if (pokesalCpu.estaDerrotado() || pokesalJogador.estaDerrotado()) {
        break;
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
    return pokesal.getNomeP() + " (" + dono.getNome() + ")";
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
