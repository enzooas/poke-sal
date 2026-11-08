package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Acao;
import br.edu.ucsal.pokesal.model.Golpe;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.Status;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import br.edu.ucsal.pokesal.model.enums.TipoElemental;
import java.util.List;

/** Conduz uma batalha entre o jogador e a CPU, turno a turno, até que haja um vencedor. */
public class BatalhaService {

  private static final double CHANCE_CRITICO = 0.0625;
  private static final double CHANCE_STATUS = 0.15;
  private static final int DANO_MINIMO = 1;
  private static final int PORCENTAGEM_MAXIMA = 100;
  private static final double CHANCE_CONTRA_ATAQUE = 0.06;
  /** O contra-ataque devolve 50% do dano que o defensor receberia. */
  private static final double FRACAO_CONTRA_ATAQUE = 0.5;

  private final Treinador treinadorJogador;
  private final Treinador treinadorCpu;
  private final Pokesal pokesalJogador;
  private final Pokesal pokesalCpu;
  private final GeradorAleatorio gerador;
  private final CalculadoraDano calculadora;
  private final Iniciativa iniciativa;
  private final EscolhaDeAcao escolhaJogador;
  private final EscolhaDeAcao escolhaCpu;
  private final RegistroBatalha registro;
  private final Terreno terrenoAtual;

  /**
   * Cria a batalha com todos os colaboradores recebidos de fora e sorteia o terreno entre os que o
   * serviço de terreno libera para o horário atual.
   */
  public BatalhaService(
      Treinador treinadorJogador, Treinador treinadorCpu, GeradorAleatorio gerador,
      CalculadoraDano calculadora, Iniciativa iniciativa,
      EscolhaDeAcao escolhaJogador, EscolhaDeAcao escolhaCpu, RegistroBatalha registro,
      ServicoTerreno servicoTerreno) {
    this.treinadorJogador = treinadorJogador;
    this.treinadorCpu = treinadorCpu;
    this.pokesalJogador = treinadorJogador.criarPokesal();
    this.pokesalCpu = treinadorCpu.criarPokesal();
    this.gerador = gerador;
    this.calculadora = calculadora;
    this.iniciativa = iniciativa;
    this.escolhaJogador = escolhaJogador;
    this.escolhaCpu = escolhaCpu;
    this.registro = registro;
    List<Terreno> terrenos = servicoTerreno.terrenosDisponiveis();
    this.terrenoAtual = terrenos.get(gerador.proximoInteiro(terrenos.size()));
  }

  /** Sorteia se o golpe será um acerto crítico. */
  public boolean sortearCritico() {
    return gerador.proximoDecimal() < CHANCE_CRITICO;
  }

  /** Sorteia se o golpe acerta o alvo, com chance igual à sua precisão (0 a 100). */
  public boolean sortearAcerto(Golpe golpe) {
    return gerador.proximoInteiro(PORCENTAGEM_MAXIMA) < golpe.getPrecisao();
  }

  /** Sorteia se o defensor bloqueia o golpe e contra-ataca na hora. */
  public boolean sortearContraAtaque() {
    return gerador.proximoDecimal() < CHANCE_CONTRA_ATAQUE;
  }

  /**
   * Calcula o dano do contra-ataque: 50% do dano que o golpe do atacante causaria no defensor,
   * descartando a parte decimal. O resultado nunca é menor que o dano mínimo.
   */
  public int calcularDanoContraAtaque(Pokesal atacante, Pokesal defensor) {
    int danoQueReceberia = calcularDano(atacante, defensor, false);
    int danoContraAtaque = (int) (danoQueReceberia * FRACAO_CONTRA_ATAQUE);
    return Math.max(DANO_MINIMO, danoContraAtaque);
  }

  public Terreno getTerrenoAtual() {
    return terrenoAtual;
  }

  private int calcularDano(Pokesal atacante, Pokesal defensor, boolean critico) {
    return calculadora.calcular(atacante, defensor, terrenoAtual, critico);
  }

  /**
   * Executa um ataque: sorteia o acerto pela precisão do golpe. Se acertar, o defensor ainda pode
   * bloquear e contra-atacar; senão o dano é aplicado e a condição de status é sorteada. Um golpe
   * que erra causa dano zero.
   */
  public void atacar(Pokesal atacante, Pokesal defensor) {
    registro.ataque(nomeComDono(atacante), nomeComDono(defensor));

    if (!sortearAcerto(atacante.getGolpe())) {
      registro.golpeErrou(nomeComDono(atacante), nomeComDono(defensor));
      return;
    }

    if (sortearContraAtaque()) {
      contraAtacar(defensor, atacante);
      return;
    }

    boolean critico = sortearCritico();
    if (critico) {
      registro.acertoCritico();
    }

    int dano = calcularDano(atacante, defensor, critico);
    int danoSofrido = defensor.receberDano(dano);
    registro.dano(
            nomeComDono(defensor), danoSofrido, defensor.getHpAtual(), defensor.getHpMax());

    if (!defensor.estaDerrotado()) {
      tentarAplicarStatus(atacante, defensor);
    }

    if (defensor.estaDerrotado()) {
      registro.derrotado(nomeComDono(defensor));
    }
  }

  private void contraAtacar(Pokesal defensor, Pokesal atacante) {
    registro.contraAtaque(nomeComDono(defensor), nomeComDono(atacante));

    int dano = calcularDanoContraAtaque(atacante, defensor);
    int danoSofrido = atacante.receberDano(dano);
    registro.dano(
        nomeComDono(atacante), danoSofrido, atacante.getHpAtual(), atacante.getHpMax());

    if (atacante.estaDerrotado()) {
      registro.derrotado(nomeComDono(atacante));
    }
  }

  private void tentarAplicarStatus(Pokesal atacante, Pokesal defensor) {
    if (defensor.getStatus() != Status.NORMAL
        || gerador.proximoDecimal() >= CHANCE_STATUS) {
      return;
    }
    if (atacante.getTipoElemental() == TipoElemental.FOGO) {
      defensor.setStatus(Status.QUEIMADO);
      registro.efeitoStatus(nomeComDono(defensor) + " foi queimado!");
    } else if (atacante.getTipoElemental() == TipoElemental.PLANTA) {
      defensor.setStatus(Status.ENVENENADO);
      registro.efeitoStatus(nomeComDono(defensor) + " foi envenenado!");
    } else {
      defensor.setStatus(Status.PARALISADO);
      registro.efeitoStatus(nomeComDono(defensor) + " foi paralisado!");
    }
  }

  private void executarTurno(
      Treinador treinador, Pokesal pokesal, Pokesal alvo, EscolhaDeAcao escolha) {
    Acao acao = escolha.escolher(treinador, pokesal);
    if (acao.ehAtaque()) {
      atacar(pokesal, alvo);
    } else {
      aplicarItem(treinador, pokesal, acao.getItem());
    }
  }

  private void aplicarItem(Treinador treinador, Pokesal pokesal, Itens item) {
    treinador.getMochila().registrarUso(item);
    String efeito = item.aplicarEm(pokesal);
    registro.usoItem(treinador.getNome() + " usou " + item.getNome() + "! " + efeito);
  }

  private void processarFimDeTurno(Pokesal pokesal) {
    if (pokesal.estaDerrotado()) {
      return;
    }

    Status statusAtual = pokesal.getStatus();
    double percentualStatus = statusAtual.danoNoTurno(pokesal.getTurnosComStatus());
    if (percentualStatus > 0.0) {
      int danoStatus = (int) (pokesal.getHpMax() * percentualStatus);

      if (danoStatus < DANO_MINIMO) {
        danoStatus = DANO_MINIMO;
      }

      pokesal.receberDano(danoStatus);
      registro.efeitoStatus(nomeComDono(pokesal) + " sofreu " + danoStatus
          + " de dano devido ao status " + statusAtual + "!");
    }

    if (statusAtual != Status.NORMAL) {
      pokesal.avancarTurnoDeStatus();
      if (statusAtual.expira()
          && pokesal.getTurnosComStatus() >= statusAtual.getDuracaoTurnos()) {
        pokesal.setStatus(Status.NORMAL);
        registro.efeitoStatus(nomeComDono(pokesal) + " se recuperou de " + statusAtual + "!");
      }
    }

    if (pokesal.estaDerrotado()) {
      registro.derrotado(nomeComDono(pokesal));
      return;
    }

    double percentualCura = terrenoAtual.curaNoFimDoTurno(pokesal.getTipoElemental());
    if (percentualCura > 0.0) {
      int curado = pokesal.curar((int) (pokesal.getHpMax() * percentualCura));
      if (curado > 0) {
        registro.efeitoTerreno(
            nomeComDono(pokesal) + " recuperou " + curado + " de HP pelo terreno!");
      }
    }

    registro.fimDeTurno(nomeComDono(pokesal), pokesal.getHpAtual(), pokesal.getHpMax());
  }

  /** Roda as rodadas até alguém ser derrotado e devolve o treinador vencedor. */
  public Treinador iniciarBatalha() {
    treinadorJogador.getMochila().resetarUsoBatalha();
    treinadorCpu.getMochila().resetarUsoBatalha();

    registro.inicioBatalha(
        nomeComDono(pokesalJogador) + " contra " + nomeComDono(pokesalCpu),
        terrenoAtual.toString());

    int rodada = 1;
    while (!pokesalJogador.estaDerrotado() && !pokesalCpu.estaDerrotado()) {
      registro.inicioRodada(rodada);

      boolean jogadorComeca = iniciativa.primeiroAtacaAntes(pokesalJogador, pokesalCpu);
      registro.ordemDoTurno(
          nomeComDono(jogadorComeca ? pokesalJogador : pokesalCpu),
          iniciativa.velocidadeEfetiva(pokesalJogador),
          iniciativa.velocidadeEfetiva(pokesalCpu),
          iniciativa.haEmpate(pokesalJogador, pokesalCpu));

      if (jogadorComeca) {
        executarTurno(treinadorJogador, pokesalJogador, pokesalCpu, escolhaJogador);
        if (pokesalCpu.estaDerrotado()) {
          break;
        }
        executarTurno(treinadorCpu, pokesalCpu, pokesalJogador, escolhaCpu);
      } else {
        executarTurno(treinadorCpu, pokesalCpu, pokesalJogador, escolhaCpu);
        if (pokesalJogador.estaDerrotado()) {
          break;
        }
        executarTurno(treinadorJogador, pokesalJogador, pokesalCpu, escolhaJogador);
      }

      processarFimDeTurno(pokesalJogador);
      processarFimDeTurno(pokesalCpu);
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
}
