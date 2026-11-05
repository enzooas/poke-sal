package br.edu.ucsal.pokesal.service;

import br.edu.ucsal.pokesal.model.Acao;
import br.edu.ucsal.pokesal.model.Pokesal;
import br.edu.ucsal.pokesal.model.ResultadoBatalha;
import br.edu.ucsal.pokesal.model.Treinador;
import br.edu.ucsal.pokesal.model.enums.Itens;
import br.edu.ucsal.pokesal.model.enums.Status;
import br.edu.ucsal.pokesal.model.enums.Terreno;
import br.edu.ucsal.pokesal.model.enums.TipoElemental;
import br.edu.ucsal.pokesal.repository.RepositorioResultados;

/** Conduz uma batalha entre o jogador e a CPU, turno a turno, até que haja um vencedor. */
public class BatalhaService {

  private static final double CHANCE_CRITICO = 0.0625;
  private static final double CHANCE_STATUS = 0.15;
  private static final int DANO_MINIMO = 1;

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
  private final RepositorioResultados repo;
  private final Terreno terrenoAtual;

  /** Cria a batalha com todos os colaboradores recebidos de fora e sorteia o terreno. */
  public BatalhaService(Treinador treinadorJogador, Treinador treinadorCpu,
        GeradorAleatorio gerador, CalculadoraDano calculadora, Iniciativa iniciativa,
        EscolhaDeAcao escolhaJogador, EscolhaDeAcao escolhaCpu, RegistroBatalha registro,
        RepositorioResultados repo) {
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
    this.repo = repo;
    Terreno[] terrenos = Terreno.values();
    this.terrenoAtual = terrenos[gerador.proximoInteiro(terrenos.length)];
  }

  /** Sorteia se o golpe será um acerto crítico. */
  public boolean sortearCritico() {
    return gerador.proximoDecimal() < CHANCE_CRITICO;
  }

  /** Executa um ataque, aplica o dano e sorteia a condição de status do defensor. */
  public void atacar(Pokesal atacante, Pokesal defensor) {
    registro.ataque(nomeComDono(atacante), nomeComDono(defensor));

    boolean critico = sortearCritico();
    if (critico) {
      registro.acertoCritico();
    }

    int dano = calculadora.calcular(atacante, defensor, terrenoAtual, critico);
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

  private void tentarAplicarStatus(Pokesal atacante, Pokesal defensor) {
    if (defensor.getStatus() != Status.NORMAL
        || gerador.proximoDecimal() >= CHANCE_STATUS) {
      return;
    }
    if (atacante.getTipoElemental() == TipoElemental.FOGO) {
      defensor.setStatus(Status.QUEIMADO);
      registro.efeitoStatus(nomeComDono(defensor) + " foi queimado!\n");
    } else if (atacante.getTipoElemental() == TipoElemental.PLANTA) {
      defensor.setStatus(Status.ENVENENADO);
      registro.efeitoStatus(nomeComDono(defensor) + " foi envenenado!\n");
    } else {
      defensor.setStatus(Status.PARALISADO);
      registro.efeitoStatus(nomeComDono(defensor) + " foi paralisado!\n");
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
          + " de dano devido ao status " + statusAtual + "!\n");
    }

    if (statusAtual != Status.NORMAL) {
      pokesal.avancarTurnoDeStatus();
      if (statusAtual.expira()
          && pokesal.getTurnosComStatus() >= statusAtual.getDuracaoTurnos()) {
        pokesal.setStatus(Status.NORMAL);
        registro.efeitoStatus(nomeComDono(pokesal) + " se recuperou de " + statusAtual + "!\n");
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
            nomeComDono(pokesal) + " recuperou " + curado + " de HP pelo terreno!\n");
      }
    }
    registro.fimDeTurno(nomeComDono(pokesal), pokesal.getHpAtual(), pokesal.getHpMax());
  }

  /** Roda as rodadas até alguém ser derrotado e devolve o resultado da batalha. */
  public ResultadoBatalha iniciarBatalha() {
    treinadorJogador.getMochila().resetarUsoBatalha();
    treinadorCpu.getMochila().resetarUsoBatalha();

    registro.inicioBatalha(
        nomeComDono(pokesalJogador) + " contra " + nomeComDono(pokesalCpu),
        terrenoAtual.toString());

    int rodada = 0;
    while (!pokesalJogador.estaDerrotado() && !pokesalCpu.estaDerrotado()) {
      rodada++;
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
    }

    boolean jogadorVenceu = !pokesalJogador.estaDerrotado();
    ResultadoBatalha resultado = montarResultado(jogadorVenceu, rodada);
    salvarResultado(resultado);
    return resultado;
  }

  private ResultadoBatalha montarResultado(boolean jogadorVenceu, int rodadas) {
    Treinador vencedor = jogadorVenceu ? treinadorJogador : treinadorCpu;
    Treinador perdedor = jogadorVenceu ? treinadorCpu : treinadorJogador;
    Pokesal pokesalVencedor = jogadorVenceu ? pokesalJogador : pokesalCpu;
    Pokesal pokesalPerdedor = jogadorVenceu ? pokesalCpu : pokesalJogador;

    return new ResultadoBatalha(vencedor.getNome(), perdedor.getNome(), rodadas,
        pokesalVencedor.getNomeP(), pokesalPerdedor.getNomeP());
  }

  private void salvarResultado(ResultadoBatalha resultado) {
    try {
      repo.salvar(resultado);
    } catch (RuntimeException e) {
      registro.erro("Falha ao salvar o resultado da batalha: " + e.getMessage());
    }
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
